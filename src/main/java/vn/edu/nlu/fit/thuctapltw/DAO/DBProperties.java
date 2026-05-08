package vn.edu.nlu.fit.thuctapltw.DAO;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class DBProperties {
    private static final Properties prop = new Properties();

    static {
        String env = System.getProperty("app.env", "local");
        String fileName = "db-" + env + ".properties";

        try (InputStream input = DBProperties.class.getClassLoader().getResourceAsStream(fileName)) {
            if (input == null) {
                throw new RuntimeException("Không tìm thấy file cấu hình DB: " + fileName);
            }
            prop.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Không thể đọc file cấu hình DB: " + fileName, e);
        }
    }

    public static String host() {
        return prop.getProperty("db.host");
    }

    public static int port() {
        try {
            return Integer.parseInt(prop.getProperty("db.port"));
        } catch (NumberFormatException e) {
            return 3306;
        }
    }

    public static String user() {
        return prop.getProperty("db.user");
    }

    public static String password() {
        return prop.getProperty("db.pass");
    }

    public static String dbname() {
        return prop.getProperty("db.name");
    }
}