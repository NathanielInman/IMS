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
   
   private ArrayList getResultSet(PreparedStatement stmt, String sql, int controllerType, String[] strings){
      
       ArrayList returnResults = new ArrayList<>();
       Connection conn;
       try{
           Class.forName(JDBC_DRIVER);
           System.out.println("Connecting to database...");
           conn = DriverManager.getConnection(DB_URL,USER,PASS);
           System.out.println("Creating statement...");
           stmt = conn.prepareStatement(sql);
           for(int i=0; i<strings.length; i++){
               
               stmt.setString(i+1, strings[i]);
           }
           if(controllerType == IMSController.TYPE_INVENTORY){
                try (ResultSet rs = stmt.executeQuery()){
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
                    }
                }
           } 
           else if(controllerType == IMSController.TYPE_VENDOR) {
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
                    }
                }
           }
           else if(controllerType == IMSController.TYPE_ROYALTIES){
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
                        returnResults.add(rs.getString("royalty"));
                   } //end while
               } //end try
           }
           else if(controllerType == IMSController.TYPE_USER){
               try (ResultSet rs = stmt.executeQuery()){
                   while(rs.next()){
                        returnResults.add(rs.getInt("id"));
                        returnResults.add(rs.getString("name"));
                        returnResults.add(rs.getString("password"));
                        //returnResults.add(rs.getInt("Level"));
                        returnResults.add(1);
                        
                   } //end while
               } //end try
           }
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
        return returnResults;
   }
   public ArrayList getRowByID(int id, int type){
       ArrayList returnResults = new ArrayList<>();
        PreparedStatement stmt = null;
        String sql;
        String tableName = this.tableIntToString(type);
        sql = "SELECT * FROM "+tableName+" WHERE id="+id;
        String[] stringsArray = {};
       returnResults = getResultSet(stmt, sql, type, stringsArray);
       System.out.println("Finished.");
       return returnResults;
   }
   public byte[] getImageFromID(int id){
       Connection conn;
       String sql = "SELECT picture FROM Inventory WHERE ID="+id;
       PreparedStatement stmt = null;
       byte[] bytes = null;
       try{
           Class.forName(JDBC_DRIVER);
           System.out.println("Connecting to database...");
           conn = DriverManager.getConnection(DB_URL,USER,PASS);
           System.out.println("Creating statement...");
           stmt = conn.prepareStatement(sql);
            try (ResultSet rs = stmt.executeQuery()){
                while(rs.next()){
                    Blob blob = rs.getBlob("picture");
                    bytes = blob.getBytes(1, (int)blob.length());
                }
            }
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
       return bytes;
   }
   public void changeData(int table, int id, String column, String newData){
       Connection conn;
       Statement stmt = null;
       String sql;
       try{
           Class.forName(JDBC_DRIVER);
           System.out.println("Connecting to database...");
           conn = DriverManager.getConnection(DB_URL,USER,PASS);
           System.out.println("Creating statement...");
           sql = "UPDATE "+tableIntToString(table)+" SET "+column+"='"+newData+"' WHERE id="+id;
           stmt = conn.createStatement();
           stmt.executeUpdate(sql);
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
   }
   private String tableIntToString(int table){
       if(table==IMSController.TYPE_INVENTORY){
           return "Inventory";
       }else if(table==IMSController.TYPE_VENDOR){
           return "Vendors";
       }else if(table==IMSController.TYPE_ROYALTIES){
           return "Royalties";
       }else if(table==IMSController.TYPE_USER){
           return "Users";
       }
       // Error!
       return "";
   }
   public ArrayList search(String term, int type){
       ArrayList returnResults = new ArrayList<>();
        PreparedStatement stmt = null;

        String sql;
        term = term.replaceAll("'", "_");
        String tableName = "";
        if(type==IMSController.TYPE_INVENTORY){
            tableName = "Inventory";
        }else if(type==IMSController.TYPE_ROYALTIES){
            tableName = "Royalties";
        }
        sql = "SELECT * FROM "+tableName+" WHERE Lower(Name) LIKE '%"+term.toLowerCase()+"%'";
        String[] stringsArray = {};
       returnResults = getResultSet(stmt, sql, type, stringsArray);
       System.out.println("Finished.");
       return returnResults;
   }
   
   public ArrayList vendorSearch(String term, int vendorID){
        ArrayList returnResults = new ArrayList<>();
        PreparedStatement stmt = null;

        String sql;
        term = term.replaceAll("'", "_");
        String tableName = "";
       sql = "SELECT * FROM Inventory WHERE vendor_id = "+vendorID+" AND Lower(Name) LIKE '%"+term.toLowerCase()+"%'";
       String[] stringsArray = {};
       returnResults = getResultSet(stmt, sql, IMSController.TYPE_INVENTORY, stringsArray);
       System.out.println("Finished.");
       return returnResults;
   }
   
   /*
    * This method will get all inventory items by the specified category name.
    */
   public ArrayList getInventoryByColumn(String column, String value, int dataType){
        ArrayList returnResults = new ArrayList<>();
        PreparedStatement stmt = null;

        String sql;
        ArrayList<String> strings = new ArrayList();
        if(dataType==IMSController.CODE_NUMBER){
             sql = "SELECT * FROM Inventory WHERE "+column+"="+Integer.parseInt(value);
        }else{
            strings.add(value);
             sql = "SELECT * FROM Inventory WHERE "+column+"= ?";
        }
        String[] stringsArray;
        stringsArray = new String[strings.size()];
        stringsArray = strings.toArray(stringsArray);
       returnResults = getResultSet(stmt, sql, IMSController.TYPE_INVENTORY, stringsArray);
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
      //  Connection conn;
      /*  try{
           Class.forName(JDBC_DRIVER);
           System.out.println("Connecting to database...");
           conn = DriverManager.getConnection(DB_URL,USER,PASS);
           System.out.println("Creating statement...");*/
           String sql;
           sql = "SELECT * FROM vendors WHERE name = ?";
           //stmt = conn.prepareStatement(sql);
           //stmt.setString(1, name);
           /*try (ResultSet rs = stmt.executeQuery()){
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
       } //end try*/
           String[] vars = {name};
       returnResults = getResultSet(stmt, sql, IMSController.TYPE_VENDOR, vars);
       System.out.println("Finished.");
       return returnResults;
   } //end getVendorsByName()
   
   /*
    * This method will get a list of all the royalties
    */
   public ArrayList getRoyalties(){
        ArrayList returnResults = new ArrayList<>();
        PreparedStatement stmt = null;
     
           String sql;
           sql = "SELECT * FROM Royalties";
        
           String[] strings = {};
       returnResults = getResultSet(stmt, sql, IMSController.TYPE_ROYALTIES, strings);
       System.out.println("Finished.");
       return returnResults;
   } //end getCategory()
   public ArrayList getUsers(){
        ArrayList returnResults = new ArrayList<>();
        PreparedStatement stmt = null;
     
           String sql;
           sql = "SELECT * FROM Users";
        
           String[] strings = {};
       returnResults = getResultSet(stmt, sql, IMSController.TYPE_USER, strings);
       System.out.println("Finished.");
       return returnResults;
    
   }
} //end class