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

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import java.sql.SQLException;

public class DatabaseController {
    static int lport;
    static String rhost;
    static int rport;
    public static void go(){
        String user = "u68221731";
        String password = "P#n3l0p@";
        String host = "plainsofsedia.com";
        int port=22;
        try{
            JSch jsch = new JSch();
            Session session = jsch.getSession(user, host, port);
            lport = 4321;
            rhost = "localhost";
            rport = 3306;
            session.setPassword(password);
            session.setConfig("StrictHostKeyChecking", "no");
            System.out.println("Establishing Connection...");
            session.connect();
            int assinged_port=session.setPortForwardingL(lport, rhost, rport);
            System.out.println("localhost:"+assinged_port+" -> "+rhost+":"+rport);
        }catch(Exception e){System.err.print(e);}
    } //end go()
    public static void main(String[] args) {
        try{
            go();
        }catch(Exception ex){}
        System.out.println("An example for updating a Row from Mysql Database!");
        Connection con = null;
        String driver = "com.mysql.jdbc.Driver";
        String url = "jdbc:mysql://" + rhost +":" + lport + "/";
        String db = "db517952926.db.1and1.com";
        String dbUser = "dbo517952926";
        String dbPasswd = "KerleyCopyCenter1!";
        try{
            Class.forName(driver);
            con = DriverManager.getConnection(url+db, dbUser, dbPasswd);
            /*try{
                Statement st = con.createStatement();
                String sql = "UPDATE MyTableName " +
                    "SET email = 'ripon.wasim@smile.com' WHERE email='peace@happy.com'";
                int update = st.executeUpdate(sql);
                if(update >= 1){
                    System.out.println("Row is updated.");
                }else{
                    System.out.println("Row is not updated.");
                } //end if
            }catch (SQLException s){
                System.out.println("SQL statement is not executed!");
            } //end try */
        }catch (Exception e){
            e.printStackTrace();
        } //end try
    } //end main
} //end class