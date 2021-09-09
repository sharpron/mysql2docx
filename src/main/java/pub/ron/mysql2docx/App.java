package pub.ron.mysql2docx;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;

/**
 * @author ron
 * 2021.09.09
 */
public class App {

    /**
     * 命令行参数封装
     */
    @SuppressWarnings("unused")
    static class Args {
        @Parameter(names = "-url", description = "jdbc url.", required = true, order = 1)
        private String url;

        @Parameter(names = "-username", description = "jdbc username.", required = true, order = 2)
        private String username;

        @Parameter(names = "-password", description = "jdbc password.", required = true, order = 3)
        private String password;

        @Parameter(names = "-database", description = "jdbc database name.", required = true, order = 4)
        private String database;

        @Parameter(names = "-output", description = "output file, default mysql.doc.", required = true, order = 5)
        private String output;

        @Parameter(names = "--help", help = true, order = 6)
        private boolean help;

        @Parameter(names = "--version", description = "mysql2docx tool version", help = true, order = 7)
        private boolean v;
    }

    /**
     * 程序入口
     *
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        Args argsObject = new Args();
        JCommander jCommander = JCommander.newBuilder().addObject(argsObject).build();
        jCommander.parse(args);

        if (argsObject.v) {
            jCommander.getConsole().println("mysql2docx 1.0.0");
            return;
        }
        if (argsObject.help) {
            jCommander.setProgramName("mysql2docx -url 'jdbc:mysql://localhost:3306' -username xxx -password xxx -database xxx");
            jCommander.usage();
            return;
        }

        String output = argsObject.output == null || argsObject.output.isBlank() ? "mysql.doc" : argsObject.output;
        new Mysql2docx().build(argsObject.url, argsObject.username, argsObject.password, argsObject.database, output);
    }

}
