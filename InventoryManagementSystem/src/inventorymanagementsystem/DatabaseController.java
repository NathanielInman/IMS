/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package inventorymanagementsystem;

import java.sql.*;

public class DatabaseController {
   static final String JDBC_DRIVER = "org.apache.derby.jdbc.ClientDriver";
   static final String DB_URL = "jdbc:derby://localhost:1527/KCC"; //jdbc:derby://localhost:1527/KCC [KerleyCopyCenter on KERLEYCOPYCENTER]
   static final String USER = "KCC";
   static final String PASS = "KCC";
   public static void main(String[] args){
       Connection conn = null;
       Statement stmt = null;
       try{
           Class.forName(JDBC_DRIVER);
           System.out.println("Connecting to database...");
           conn = DriverManager.getConnection(DB_URL,USER,PASS);
           System.out.println("Creating statement...");
           stmt = conn.createStatement();
           String sql;
           sql = "SELECT * FROM Inventory";
           ResultSet rs = stmt.executeQuery(sql);
           while(rs.next()){
               int id= rs.getInt("id");
               String name=rs.getString("name");
               System.out.println(id+":"+name);
           } //end while
           rs.close();
           stmt.close();
           conn.close();
       }catch(SQLException se){ //errors in the SQL processing
           se.printStackTrace();
       }catch(Exception e){ //errors relating to Class.forName
           e.printStackTrace();
       }finally{ //clean up regardless of success
            try{
                if(stmt!=null)stmt.close();
            }catch(SQLException se){
                se.printStackTrace();
            } //end try
       } //end try
       System.out.println("Finished.");
    } //end main
} //end class