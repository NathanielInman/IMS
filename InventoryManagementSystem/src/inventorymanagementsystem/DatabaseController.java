/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package inventorymanagementsystem;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class DatabaseController {
    private Connection connect = null;
    private Statement statement = null;
    private PreparedStatement preparedStatement = null;
    private ResultSet resultSet = null;
    public void readDatabase() throws Exception{
        try{
            Class.forName("com.mysql.jdbc.Driver"); //load mysql driver
            connect = DriverManager.getConnection(
                    "jdbc:mysql://db517952926.db.1and1.com/feedback?" +
                    "user=dbo517952926&password=KerleyCopyCenter1!");
            statement = connect.createStatement();
            resultSet = statement.executeQuery("select * from Inventory");
        }catch(Exception e){
            throw e;
        }finally{
            try{
                if(resultSet!=null)resultSet.close();
                if(statement!=null)statement.close();
                if(connect!=null)connect.close();
            }catch(Exception e){throw e;}
        } //end try
    } //end readDatabase()
} //end DatabaseController()
