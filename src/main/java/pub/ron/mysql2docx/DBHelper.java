package pub.ron.mysql2docx;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ron
 * 2021.09.09
 */
public class DBHelper {

    static {
        try {
            // 加载MySQL驱动
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new AssertionError(e);
        }
    }

    private DBHelper() {

    }

    /**
     * 获取数据库连接
     *
     * @return 连接
     */
    public static Connection getConnection(String url, String username, String password) {
        try {
            return DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 关闭数据库连接
     */
    public static void closeConnection(Connection connection) {
        try {
            if (connection.isClosed()) {
                return;
            }
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 把SQL语句查询出列表
     *
     * @param sql  sql
     * @param conn conn
     * @return data
     */
    public static List<String[]> queryDataBySQL(Connection conn, String sql, Object... args) {
        List<String[]> result = new ArrayList<>();

        try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            for (int i = 0; i < args.length; i++) {
                preparedStatement.setObject(i + 1, args[i]);
            }
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                String[] arr = new String[rs.getMetaData().getColumnCount()];
                for (int i = 0; i < arr.length; i++) {
                    arr[i] = rs.getString(i + 1);
                }
                result.add(arr);
            }
            rs.close();
            return result;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
