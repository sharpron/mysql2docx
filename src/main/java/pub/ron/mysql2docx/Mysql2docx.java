package pub.ron.mysql2docx;

import com.lowagie.text.*;
import com.lowagie.text.rtf.RtfWriter2;

import java.awt.*;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.util.Arrays;
import java.util.List;

/**
 * @author ron
 * 2021.09.09
 */
public class Mysql2docx {

    /**
     * 查询所有表的sql语句
     * 包含变量 TABLE_SCHEMA 数据库名
     */
    private static final String SQL_GET_TABLES = "select TABLE_NAME,TABLE_COMMENT from INFORMATION_SCHEMA.tables where TABLE_SCHEMA=? and TABLE_TYPE='BASE TABLE'";

    /**
     * 查询所有字段的sql语句
     * 包含以下变量:
     * TABLE_NAME 表名
     * TABLE_SCHEMA 数据库名
     */
    private static final String SQL_GET_TABLE_COLUMNS = "select COLUMN_NAME, DATA_TYPE, CHARACTER_OCTET_LENGTH, IS_NULLABLE, COLUMN_COMMENT from information_schema.`COLUMNS` where TABLE_NAME=? and TABLE_SCHEMA=?";

    /**
     * 表头
     */
    private static final List<String> HEADERS = Arrays.asList("列名", "类型", "长度", "可为空", "备注");

    /**
     * 构建文档并输出
     *
     * @param jdbcUrl  jdbcUrl
     * @param username username
     * @param password password
     * @param database database
     * @param output   output
     */
    public void build(String jdbcUrl, String username, String password, String database, String output) {

        //初始化word文档
        Document document = new Document(PageSize.A4);
        try {
            RtfWriter2.getInstance(document, new FileOutputStream(output));
        } catch (FileNotFoundException e) {
            throw new AssertionError(e);
        }
        document.open();


        //查询开始
        Connection conn = DBHelper.getConnection(jdbcUrl, username, password);
        try {
            //获取所有表
            List<String[]> tables = DBHelper.queryDataBySQL(conn, SQL_GET_TABLES, database);
            for (String[] table : tables) {
                String tableName = table[0];
                addTableMetaData(document, String.format("%s", tableName));
                List<String[]> columns = DBHelper.queryDataBySQL(conn, SQL_GET_TABLE_COLUMNS, tableName, database);
                document.add(createTable(HEADERS, columns));
                addBlank(document);
            }
        } catch (DocumentException e) {
            e.printStackTrace();
        } finally {
            DBHelper.closeConnection(conn);
            document.close();
        }
    }

    /**
     * 增加表概要信息
     *
     * @param document document
     * @param summary  summary
     * @throws DocumentException maybe throw
     */
    public static void addTableMetaData(Document document, String summary) throws DocumentException {
        Paragraph ph = new Paragraph(summary);
        ph.setAlignment(Paragraph.ALIGN_LEFT);
        document.add(ph);
    }


    /**
     * 添加一个空行
     *
     * @param document document
     * @throws DocumentException e
     */
    public static void addBlank(Document document) throws DocumentException {
        Paragraph ph = new Paragraph("");
        ph.setAlignment(Paragraph.ALIGN_LEFT);
        document.add(ph);
    }

    /**
     * 添加包含字段详细信息的表格
     *
     * @param headers headers
     * @param rows    rows
     * @throws BadElementException e
     */
    public static Table createTable(List<String> headers,
                                    List<String[]> rows) throws BadElementException {
        Table table = new Table(headers.size());
        table.setWidth(100f);//表格 宽度100%
        table.setBorderWidth(1);
        table.setBorderColor(Color.BLACK);
        table.setPadding(0);
        table.setSpacing(0);

        for (String header : headers) {
            Cell headerCell = new Cell(header);// 单元格
            headerCell.setHeader(true);
            headerCell.setHorizontalAlignment(Cell.ALIGN_CENTER);
            headerCell.setBackgroundColor(Color.gray);
            table.addCell(headerCell);
        }
        table.endHeaders();

        for (String[] row : rows) {
            for (String s : row) {
                Cell dataCell = new Cell(s);
                dataCell.setHorizontalAlignment(Cell.ALIGN_CENTER);
                table.addCell(dataCell);
            }
        }

        return table;
    }
}
