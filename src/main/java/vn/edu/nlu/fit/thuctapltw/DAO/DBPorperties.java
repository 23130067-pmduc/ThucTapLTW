package vn.edu.nlu.fit.thuctapltw.DAO;

import java.util.Properties;

public class DBPorperties {
    private static Properties prop = new Properties();
    static {
        try{
            prop.load(DBPorperties.class.getClassLoader().getResourceAsStream("db.properties"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static String host() {return prop.get("db.host").toString();}
    public static int port(){
        try {
            return Integer.parseInt(prop.get("db.port").toString());
        } catch (NumberFormatException e) {
            return 3306;
        }
    }
    public static String user() {return prop.get("db.username").toString();}
    public static String password() {return prop.get("db.password").toString();}
    public static String dbname() {return prop.get("dbname").toString();}
    public static String option() {return prop.get("db.option").toString();}

    public static void main(String[] args) {
        System.out.println(dbname());
    }
}