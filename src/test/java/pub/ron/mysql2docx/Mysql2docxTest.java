package pub.ron.mysql2docx;


import org.junit.Test;

/**
 * mysql2docx 测试
 *
 * @author ron
 * 2021.09.09
 */
public class Mysql2docxTest {

    @Test
    public void testBuild() {
        new Mysql2docx().build(
                "jdbc:mysql://localhost:3306", "root", "123456",
                "test", "target/word.doc"
        );
    }
}
