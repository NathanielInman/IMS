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
   public ArrayList getInventoryByCategory(String name){
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
           sql = "SELECT * FROM Inventory WHERE category='"+name+"'";
           try (ResultSet rs = stmt.executeQuery(sql)){
                while(rs.next()){
                    returnResults.add(rs.getInt("id"));
                    returnResults.add(rs.getString("name"));
                    System.out.print(rs.getDouble("price")+",");
                    System.out.print(rs.getDouble("wholesale")+",");
                    System.out.print(rs.getString("category")+",");
                    System.out.print(rs.getString("vendor_id")+",");
                    System.out.print(rs.getString("royalty_id")+",");
                    System.out.print(rs.getString("description")+",");
                    System.out.print(rs.getBlob("picture")+",");
                    System.out.println(rs.getString("preferred_stock"));
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
    * This method will get a list of all the categories
    */
   public ArrayList getCategoryList(){
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
           sql = "SELECT DISTINCT category FROM Inventory";
           try (ResultSet rs = stmt.executeQuery(sql)){
                while(rs.next()){
                    returnResults.add(rs.getString("category"));
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
    * This method will get a list of all the vendors
    */
   public ArrayList getVendorList(){
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
           sql = "SELECT * FROM vendors";
           try (ResultSet rs = stmt.executeQuery(sql)){
                while(rs.next()){
                    System.out.print(rs.getString("id")+",");
                    System.out.print(rs.getString("name")+",");
                    System.out.print(rs.getString("phone")+",");
                    System.out.print(rs.getString("extension")+",");
                    System.out.print(rs.getString("address")+",");
                    System.out.print(rs.getString("website")+",");
                    System.out.print(rs.getString("email")+",");
                    System.out.println(rs.getString("ppoc"));
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