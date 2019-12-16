package source;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBconnection {
    Connection conn=null;
    private final String URL = "org.sqlite.JDBC";
    private final String databaselocation = "jdbc:sqlite:users.db";

    public Connection connectDatabase() throws ClassNotFoundException {
        Class.forName(URL);
        try {
            conn= DriverManager.getConnection(databaselocation);
            System.out.println("Sucessfully started DataBase");
            return conn;

        } catch (SQLException e) {

         System.out.println(e.getMessage());
        }
        return null;
    }
}
