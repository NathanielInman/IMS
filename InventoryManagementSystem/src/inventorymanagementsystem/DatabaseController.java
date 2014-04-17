/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package inventorymanagementsystem;

import java.sql.*;
import java.util.ArrayList;

/**
 *
 * @author Nate
 */
public class DatabaseController {
   static final String JDBC_DRIVER = "org.apache.derby.jdbc.ClientDriver";
   static final String DB_URL = "jdbc:derby://localhost:1527/KCC"; 
   static final String USER = "KCC";
   static final String PASS = "KCC";
   
   DatabaseController(){}
   
   /*
    * This method will get all inventory items by the specified category name.
    */
   public ArrayList getInventoryByColumn(String column, String value, int dataType){
        ArrayList returnResults = new ArrayList<>();
        Statement stmt = null;
        Connection conn;
        try{
           Class.forName(JDBC_DRIVER);
           System.out.println("Connecting to database...");
           conn = DriverManager.getConnection(DB_URL,USER,PASS);
           System.out.println("Creating statement...");
           stmt = conn.createStatement();
           String sql;
           if(dataType==IMSController.CODE_NUMBER){
                sql = "SELECT * FROM Inventory WHERE "+column+"="+Integer.parseInt(value);
           }else{
                sql = "SELECT * FROM Inventory WHERE "+column+"='"+value+"'";
           }
           try (ResultSet rs = stmt.executeQuery(sql)){
                while(rs.next()){
                    returnResults.add(rs.getInt("id"));
                    returnResults.add(rs.getString("name"));
                    returnResults.add(rs.getDouble("price"));
                    returnResults.add(rs.getDouble("wholesale"));
                    returnResults.add(rs.getString("category"));
                    returnResults.add(rs.getString("vendor_id"));
                    returnResults.add(rs.getString("royalty_id"));
                    returnResults.add(rs.getString("description"));
                    returnResults.add(rs.getBlob("picture"));
                    returnResults.add(rs.getString("preferred_stock"));
                } //end while
           }//end try
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
       return returnResults;
   } //end getCategory()
   /*
    * This method will get a list of all the categories
    */
   public ArrayList getCategoryList(String category, String controller){
        ArrayList returnResults = new ArrayList<>();
        Statement stmt = null;
        Connection conn;
        try{
           Class.forName(JDBC_DRIVER);
           System.out.println("Connecting to database...");
           conn = DriverManager.getConnection(DB_URL,USER,PASS);
           System.out.println("Creating statement...");
           stmt = conn.createStatement();
           String sql;
           sql = "SELECT DISTINCT "+category+" FROM "+controller;
           try (ResultSet rs = stmt.executeQuery(sql)){
                while(rs.next()){
                    returnResults.add(rs.getString(category));
                } //end while
           } //end try
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
       return returnResults;
   } //end getCategory()
   /*
    * This method will send a request name, where the result will be that request names password if it exists
    * and 'Invalid' if that request name doesn't exist. The handling logic will indicate that the username does
    * not exist or credentials are invalid. 
    */
   public String checkCredentials(String name){
        String returnResult = "Invalid";
        Statement stmt = null;
        Connection conn;
        try{
           Class.forName(JDBC_DRIVER);
           System.out.println("Connecting to database...");
           conn = DriverManager.getConnection(DB_URL,USER,PASS);
           System.out.println("Creating statement...");
           stmt = conn.createStatement();
           String sql;
           sql = "SELECT password FROM users where name='"+name+"'";
           try (ResultSet rs = stmt.executeQuery(sql)){
                while(rs.next()){
                    returnResult=rs.getString("password"); //regardless of how many users there are, just return the last one
                } //end while
           } //end try
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
       return returnResult; //return the password if successful, or 'Invalid' if not
   } //end checkCredentials()
   /*
    * This method will get a list of all the vendors
    */
   public ArrayList getVendorsByName(String name){
        ArrayList returnResults = new ArrayList<>();
        PreparedStatement stmt = null;
        Connection conn;
        try{
           Class.forName(JDBC_DRIVER);
           System.out.println("Connecting to database...");
           conn = DriverManager.getConnection(DB_URL,USER,PASS);
           System.out.println("Creating statement...");
           String sql;
           sql = "SELECT * FROM vendors WHERE name = ?";
           stmt = conn.prepareStatement(sql);
           stmt.setString(1, name);
           try (ResultSet rs = stmt.executeQuery()){
                while(rs.next()){
                    returnResults.add(rs.getString("id"));
                    returnResults.add(rs.getString("name"));
                    returnResults.add(rs.getString("phone"));
                    returnResults.add(rs.getString("extension"));
                    returnResults.add(rs.getString("address"));
                    returnResults.add(rs.getString("website"));
                    returnResults.add(rs.getString("email"));
                    returnResults.add(rs.getString("ppoc"));
                } //end while
           } //end try
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
       return returnResults;
   } //end getVendorsByName()
   
   /*
    * This method will get a list of all the royalties
    */
   public ArrayList getRoyaltyList(){
        ArrayList returnResults = new ArrayList<>();
        Statement stmt = null;
        Connection conn;
        try{
           Class.forName(JDBC_DRIVER);
           System.out.println("Connecting to database...");
           conn = DriverManager.getConnection(DB_URL,USER,PASS);
           System.out.println("Creating statement...");
           stmt = conn.createStatement();
           String sql;
           sql = "SELECT * FROM Royalties";
           try (ResultSet rs = stmt.executeQuery(sql)){
                while(rs.next()){
                    System.out.print(rs.getString("id")+",");
                    System.out.print(rs.getString("name")+",");
                    System.out.print(rs.getString("phone")+",");
                    System.out.print(rs.getString("extension")+",");
                    System.out.print(rs.getString("address")+",");
                    System.out.print(rs.getString("website")+",");
                    System.out.print(rs.getString("email")+",");
                    System.out.print(rs.getString("ppoc")+",");
                    System.out.println(rs.getString("royalty"));
                } //end while
           } //end try
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
       return returnResults;
   } //end getCategory()
} //end class