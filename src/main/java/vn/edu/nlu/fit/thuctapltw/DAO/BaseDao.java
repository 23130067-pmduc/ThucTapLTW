package vn.edu.nlu.fit.thuctapltw.DAO;

import com.mysql.cj.jdbc.MysqlDataSource;
import org.jdbi.v3.core.Jdbi;

import java.sql.SQLException;

public class BaseDao {

    private Jdbi jdbi;

    protected Jdbi getJdbi() {
        if (jdbi == null) connect();
        return jdbi;
    }

    protected void connect() {
        MysqlDataSource dataSource = new MysqlDataSource();

        String url = "jdbc:mysql://" + DBProperties.host() + ":" + DBProperties.port() + "/" + DBProperties.dbname();

        System.out.println("DB URL: " + url);
        System.out.println("DB User: " + DBProperties.user());

        dataSource.setURL(url);
        dataSource.setUser(DBProperties.user());
        dataSource.setPassword(DBProperties.password());

        try {
            dataSource.setUseCompression(true);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        jdbi = Jdbi.create(dataSource);
    }
}