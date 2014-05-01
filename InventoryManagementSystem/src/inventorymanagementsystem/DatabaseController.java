/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package inventorymanagementsystem;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Nate
 */
public class DatabaseController {
   static final String JDBC_DRIVER = "org.apache.derby.jdbc.ClientDriver";
   static final String DB_URL = "jdbc:derby://localhost:1527/KCC"; 
   static final String USER = "KCC";
   static final String PASS = "KCC";
   
   DatabaseController(){}
   
   private ArrayList getResultSet(PreparedStatement stmt, String sql, int controllerType, String[] strings){
       System.out.println("[getResultSet] Started");
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
           } //end for
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
           }else if(controllerType == IMSController.TYPE_VENDOR) {
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
           }else if(controllerType == IMSController.TYPE_ROYALTIES){
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
           }else if(controllerType == IMSController.TYPE_USER){
               try (ResultSet rs = stmt.executeQuery()){
                   while(rs.next()){
                        returnResults.add(rs.getInt("id"));
                        returnResults.add(rs.getString("name"));
                        returnResults.add(rs.getString("password"));
                        //returnResults.add(rs.getInt("Level"));
                        returnResults.add(1);
                        
                   } //end while
               } //end try
           } //end if
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
       System.out.println(returnResults+"\n[getResultSet] Finished");
       return returnResults;
   } //end getResultSet()
   
   public ArrayList getRowByID(int id, int type){ 
       System.out.println("[getRowByID] Started");
       ArrayList returnResults = new ArrayList<>();
       PreparedStatement stmt = null;
       String sql;
       String tableName = this.tableIntToString(type);
       sql = "SELECT * FROM "+tableName+" WHERE id="+id;
       String[] stringsArray = {};
       returnResults = getResultSet(stmt, sql, type, stringsArray);
       System.out.println(returnResults+"\n[getRowByID] Finished");
       return returnResults;
   } //end getRowByID()
   
   public byte[] getImageFromID(int id){
       System.out.println("[getImageFromID] Started");
       Connection conn;
       String sql = "SELECT picture FROM Inventory WHERE ID="+id;
       PreparedStatement stmt = null;
       byte[] bytes = null;
       try{
           Class.forName(JDBC_DRIVER);
           conn = DriverManager.getConnection(DB_URL,USER,PASS);
           stmt = conn.prepareStatement(sql);
            try (ResultSet rs = stmt.executeQuery()){
                while(rs.next()){
                    Blob blob = rs.getBlob("picture");
                    if(blob==null){
                        return new byte[0];
                    }
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
       System.out.println("[getImageFromID] Finished");
       return bytes;
   } //end getImageFromID()
   
   public void changeData(int table, int id, String column, String newData){
       System.out.println("[changeData] Started/n"+">>UPDATE "+tableIntToString(table)+" SET "+column+"='"+newData+"' WHERE id="+id);
       Connection conn;
       Statement stmt = null;
       String sql;
       try{
           Class.forName(JDBC_DRIVER);
           conn = DriverManager.getConnection(DB_URL,USER,PASS);
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
       System.out.println("[changeData] Finished");
   } //end changeData()
   
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
       return ""; // Error!
   } //end tableIntToString()
   
   public ArrayList search(String term, int type){
        System.out.println("[search] Started");
        ArrayList returnResults = new ArrayList<>();
        PreparedStatement stmt = null;
        String sql;
        term = term.replaceAll("'", "_");
        String tableName = "";
        if(type==IMSController.TYPE_INVENTORY){
            tableName = "Inventory";
        }else if(type==IMSController.TYPE_ROYALTIES){
            tableName = "Royalties";
        } //end if
        sql = "SELECT * FROM "+tableName+" WHERE Lower(Name) LIKE '%"+term.toLowerCase()+"%'";
        String[] stringsArray = {};
        returnResults = getResultSet(stmt, sql, type, stringsArray);
        System.out.println(returnResults+"[search] Finished");
        return returnResults;
   } //end search()
   
   /*
    * This method will search a key term from the inventory list of a given
    * vendor specification id
    */
   public ArrayList vendorSearch(String term, int vendorID){
        System.out.println("[vendorSearch] Started");
        ArrayList returnResults;
        PreparedStatement stmt = null;
        String sql;
        term = term.replaceAll("'", "_");
        sql = "SELECT * FROM Inventory WHERE vendor_id = "+vendorID+" AND Lower(Name) LIKE '%"+term.toLowerCase()+"%'";
        String[] stringsArray = {};
        returnResults = getResultSet(stmt, sql, IMSController.TYPE_INVENTORY, stringsArray);
        System.out.println(returnResults+"[vendorSearch] Finished");
        return returnResults;
   } //end vendorSearch()
   
   /*
    * This method will search a key term from the inventory list of a given royalty
    * specification id
    */
   public ArrayList royaltySearch(String term, int royaltyID){
        System.out.println("[royaltySearch] Started");
        ArrayList returnResults;
        PreparedStatement stmt = null;
        String sql;
        term = term.replaceAll("'", "_");
        sql = "SELECT * FROM Inventory WHERE royalty_id = "+royaltyID+" AND Lower(Name) LIKE '%"+term.toLowerCase()+"%'";
        String[] stringsArray = {};
        returnResults = getResultSet(stmt, sql, IMSController.TYPE_INVENTORY, stringsArray);
        System.out.println(returnResults+"\n[royaltySearch] Finished");
        return returnResults;
   } //end royaltySearch()
   
   /*
    * This method will get all inventory items by the specified category name.
    */
   public ArrayList getInventoryByColumn(String column, String value, int dataType){
        System.out.println("[getInventoryByColumn] Started");
        ArrayList returnResults = new ArrayList<>();
        PreparedStatement stmt = null;
        String sql;
        ArrayList<String> strings = new ArrayList();
        if(dataType==IMSController.CODE_NUMBER){
            sql = "SELECT * FROM Inventory WHERE "+column+"="+Integer.parseInt(value);
        }else{
            strings.add(value);
            sql = "SELECT * FROM Inventory WHERE "+column+"= ?";
        } //end if
        String[] stringsArray;
        stringsArray = new String[strings.size()];
        stringsArray = strings.toArray(stringsArray);
        returnResults = getResultSet(stmt, sql, IMSController.TYPE_INVENTORY, stringsArray);
        System.out.println(returnResults+"\n[getInventoryByColumn] Finished");
        return returnResults;
   } //end getCategory()
   
   /*
    * This method will get a list of all the categories
    */
   public ArrayList getCategoryList(String category, String controller){
       System.out.println("[getCategoryList] Started");
       ArrayList returnResults = new ArrayList<>();
        Statement stmt = null;
        Connection conn;
        try{
           Class.forName(JDBC_DRIVER);
           conn = DriverManager.getConnection(DB_URL,USER,PASS);
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
       System.out.println(returnResults+"[getCategoryList] Finished.");
       return returnResults;
   } //end getCategoryList()
   
   /*
    * This method will send a request name, where the result will be that request names password if it exists
    * and 'Invalid' if that request name doesn't exist. The handling logic will indicate that the username does
    * not exist or credentials are invalid. 
    */
   public String checkCredentials(String name){
       System.out.println("[checkCredentials] Started");
       String returnResult = "Invalid";
        Statement stmt = null;
        Connection conn;
        try{
           Class.forName(JDBC_DRIVER);
           conn = DriverManager.getConnection(DB_URL,USER,PASS);
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
       System.out.println(returnResult+"\n[checkCredentials] Finished");
       return returnResult; //return the password if successful, or 'Invalid' if not
   } //end checkCredentials()
   
   /*
    * This method will get a list of all the vendors
    */
   public ArrayList getVendorsByName(String name){
       System.out.println("[getVendorsByName] Started");
       ArrayList returnResults = new ArrayList<>();
       PreparedStatement stmt = null;
       String sql = "SELECT * FROM vendors WHERE name = ?";
       String[] vars = {name};
       returnResults = getResultSet(stmt, sql, IMSController.TYPE_VENDOR, vars);
       System.out.println(returnResults+"\n[getVendorsByName] Finished");
       return returnResults;
   } //end getVendorsByName()
   
   /*
    * This method will get a list of all the royalties
    */
   public ArrayList getRoyaltiesByName(String name){
       System.out.println("[getRoyaltiesByName] Started");
       ArrayList returnResults = new ArrayList<>();
       PreparedStatement stmt = null;
       String sql = "SELECT * FROM royalties WHERE name = ?";
       String[] vars = {name};
       returnResults = getResultSet(stmt, sql, IMSController.TYPE_ROYALTIES,vars);
       System.out.println(returnResults+"\n[getRoyaltiesByName] Finished");
       return returnResults;
   } ///end getRoyaltiesByName()
   
   /*
    * This method will get a list of all the royalties
    */
   public ArrayList getRoyalties(){
        System.out.println("[getRoyalties] Started");
        ArrayList returnResults = new ArrayList<>();
        PreparedStatement stmt = null;
        String sql;
        sql = "SELECT * FROM Royalties";
        String[] strings = {};
        returnResults = getResultSet(stmt, sql, IMSController.TYPE_ROYALTIES, strings);
        System.out.println(returnResults+"\n[getRoyalties] Finished");
        return returnResults;
   } //end getCategory()
   
   public ArrayList getUsers(){
        System.out.println("[getUsers] Started");
        ArrayList returnResults = new ArrayList<>();
        PreparedStatement stmt = null;
        String sql;
        sql = "SELECT * FROM Users"; 
        String[] strings = {};
        returnResults = getResultSet(stmt, sql, IMSController.TYPE_USER, strings);
        System.out.println(returnResults+"\n[getUsers] Finished.");
        return returnResults;
   } //end getUsers()
   
   public ArrayList getUser(String user){
        System.out.println("[getUser] Started");
        ArrayList returnResults = new ArrayList<>();
        PreparedStatement stmt = null;
        String sql;
        sql = "SELECT * FROM Users WHERE name=?";
        String[] strings = {user};
        returnResults = getResultSet(stmt, sql, IMSController.TYPE_USER, strings);
        System.out.println(returnResults+"\n[getUser] Finished");
        return returnResults;
   } //end getUser()
   public void deleteRow(int table, int id){
       System.out.println("[deleteRow] Started");
       Connection conn;
       Statement stmt = null;
       String sql;
       try{
           Class.forName(JDBC_DRIVER);
           conn = DriverManager.getConnection(DB_URL,USER,PASS);
           sql = "DELETE FROM "+tableIntToString(table)+" WHERE id="+id;
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
       System.out.println("[deleteRow] Finished");
   }
   public boolean addToDatabase(int table, String[] data, File picture) {
       byte[] bytes;
        try {
            bytes = Files.readAllBytes(picture.toPath());
        } catch (Exception ex) {
            bytes = null;
        }
       System.out.println("[addToDatabase] Started");
       for(int i = 0; i<data.length; i++){
           if(data[i].length()==0){
               data[i] = null;
           }
       }
       Connection conn;
       PreparedStatement stmt = null;
       String sql;
       try{
           Class.forName(JDBC_DRIVER);
           conn = DriverManager.getConnection(DB_URL,USER,PASS);
           sql = "INSERT INTO "+tableIntToString(table)+" VALUES (";
           for(int i = 0; i<data.length; i++){
               if(i>0){
                   sql += ",";
               }
               sql += "?";
           }
           sql += ")";
           stmt = conn.prepareStatement(sql);
           if(table==IMSController.TYPE_INVENTORY){
                stmt.setInt(1,intOrZero(data[0]));
                stmt.setString(2,data[1]);
                stmt.setDouble(3,doubleOrZero(data[2]));
                stmt.setDouble(4,doubleOrZero(data[3]));
                if(data[4]==null){
                    data[4] = "Uncategorized";
                }
                stmt.setString(5,data[4]);
                stmt.setString(6,data[5]);
                stmt.setString(7,data[6]);
                stmt.setString(8,data[7]);
                stmt.setString(9,data[8]);
                Blob blob;
                if(bytes==null){
                    blob = null;
                }else{
                    blob = conn.createBlob();
                    blob.setBytes(1, bytes);
                }
                stmt.setBlob(10,blob);
           }
           stmt.executeUpdate();
           stmt.close();
           conn.close();
           System.out.println("[addToDatabase] Finished");
           return true;
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
       System.out.println("[addToDatabase] Finished");
       return false;
   }
   private int intOrZero(String string){
       if(string == null){
        return 0;
       }
       if(string.length() == 0){
           return 0;
       }
       else {
           return Integer.parseInt(string);
       }
   }
   
      private Double doubleOrZero(String string){
          if(string == null){
              return 0.0;
          }
       if(string.length() == 0){
           return 0.0;
       }
       else {
           return Double.parseDouble(string);
       }
   }
} //end class