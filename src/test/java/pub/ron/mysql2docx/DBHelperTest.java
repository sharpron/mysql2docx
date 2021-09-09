package pub.ron.mysql2docx;

import org.junit.Assert;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

/**
 * db helper 测试
 *
 * @author ron
 * 2021.09.09
 */
public class DBHelperTest {

    private Connection getTestConnection() {
        return DBHelper.getConnection("jdbc:mysql://localhost:3306/", "root", "123456");
    }

    @Test
    public void testGetConnection() {
        Connection connection = getTestConnection();
        Assert.assertNotNull(connection);
    }

    @Test
    public void testCloseConnection() throws SQLException {
        Connection connection = getTestConnection();
        DBHelper.closeConnection(connection);
        Assert.assertTrue(connection.isClosed());
    }

    @Test
    public void testQueryDataBySQL() throws SQLException {
        Connection connection = getTestConnection();

        List<String[]> result = DBHelper.queryDataBySQL(connection, "select 1");
        List<String[]> expected = Collections.singletonList(new String[]{"1"});
        assertListArrayEquals(expected, result);

        List<String[]> result2 = DBHelper.queryDataBySQL(connection, "select 1 from dual where 1 = ?", 1);
        assertListArrayEquals(expected, result2);

        List<String[]> result3 = DBHelper.queryDataBySQL(connection, "select 1 from dual where 1 = ?", 0);
        Assert.assertEquals(0, result3.size());


        DBHelper.closeConnection(connection);
        Assert.assertTrue(connection.isClosed());
    }

    private static void assertListArrayEquals(List<String[]> expected, List<String[]> result) {
        Assert.assertEquals(expected.size(), result.size());
        for (int i = 0; i < result.size(); i++) {
            Assert.assertArrayEquals(expected.get(i), result.get(i));
        }
    }
}
