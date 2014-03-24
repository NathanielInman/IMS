/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package inventorymanagementsystem;

import java.sql.*;

public class DatabaseController {
   static final String JDBC_DRIVER = "org.apache.derby.jdbc.ClientDriver";
   static final String DB_URL = "jdbc:derby://localhost:1527/KCC"; //jdbc:derby://localhost:1527/KCC [KerleyCopyCenter on KERLEYCOPYCENTER]
   static final String USER = "KerleyCopyCenter";
   static final String PASS = "KerleyCopyCenter";
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
               int id= rs.getInt("ID");
               String name=rs.getString("NAME");
               Long price=rs.getLong("PRICE");
               Long wholesale=rs.getLong("WHOLESALE");
               String categories=rs.getString("CATEGORIES");
               int vendor=rs.getInt("VENDORID");
               int royalty=rs.getInt("ROYALTYID");
               String description=rs.getString("DESCRIPTION");
               //image = rs.getBlob("PICTURE");
               int stockpreferred=rs.getInt("STOCKPREFERRED");
               int stockcurrent=rs.getInt("STOCKCURRENT");
               System.out.println(id+":"+name+":"+price+":"+wholesale+":"+categories+":"+vendor+":"+royalty+":"+description+":"+stockpreferred+":"+stockcurrent);
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