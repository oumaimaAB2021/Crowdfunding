
package utils;


import java.sql.*;


public class ConnectionUtil {
   Connection conn=null;
   
   public static Connection conBD(){
            try {
           Class.forName("com.mysql.jdbc.Driver");
           Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/crowdfunding", "root", "");
           System.out.println("Connexion établie avec succées");
           return con;
       } catch (ClassNotFoundException | SQLException ex) {
           ex.printStackTrace();
           return null;
       }       
    }
}
