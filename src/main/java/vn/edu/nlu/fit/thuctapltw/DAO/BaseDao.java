package vn.edu.nlu.fit.thuctapltw.DAO;

import com.mysql.cj.jdbc.MysqlDataSource;
import vn.edu.nlu.fit.thuctapltw.model.User;
import org.jdbi.v3.core.Jdbi;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

public class BaseDao {

    private Jdbi jdbi;

    protected Jdbi getJdbi() {
        if (jdbi == null) connect();
        return jdbi;
    }

    protected  void connect() {
        MysqlDataSource dataSource = new MysqlDataSource();
        System.out.println("jdbc:mysql://"+DBProperties.host+":"+DBProperties.port+"/"+DBProperties.name);
        dataSource.setURL("jdbc:mysql://"+DBProperties.host+":"+DBProperties.port+"/"+DBProperties.name);
        dataSource.setUser(DBProperties.user);
        dataSource.setPassword(DBProperties.password);
        try {
            dataSource.setUseCompression(true);
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
        jdbi = Jdbi.create(dataSource);
    }

    public static class DBProperties {
        private static Properties prop = new Properties();

        static {
            try {
                File file = new File("..\\src\\main\\resources\\db.properties");
                if (file.exists()) {
                    prop.load(new FileInputStream(file));
                } else {
                    prop.load(DBProperties.class.getClassLoader().getResourceAsStream("db.properties"));
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        public static String host = prop.getProperty("db.host");
        public static String port = prop.getProperty("db.port");
        public static String user = prop.getProperty("db.user");
        public static String password = prop.getProperty("db.pass");
        public static String name = prop.getProperty("db.name");

    }
    public static void main(String[] args) {
        BaseDao baseDao = new BaseDao();
        Jdbi jdbi=baseDao.getJdbi();
        List<User> lu =jdbi.withHandle(handle -> {
            return handle.createQuery("select * from users").mapToBean(User.class).list();
        });
        System.out.println(lu);

    }
}