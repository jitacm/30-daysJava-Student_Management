package DB;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBconnection {

    static Connection con; //this is connection object

    // This method return the connection object i.e con
    public static Connection createConnection() {

        try {
           Class.forName("com.mysql.cj.jdbc.Driver");
           String user ="root";
           String pass="student";
           String url="jdbc:mysql://localhost:3306/SMA_DB?autoReconnect=true&useSSL=false";
        
           con=DriverManager.getConnection(url, user, pass);
        } 
        catch (Exception ex) {
            ex.printStackTrace();

        }
        return con; 
    }

}