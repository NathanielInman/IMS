/*
 * This is our inventory controller. It will use the methods of IMSController
 * to display and sort the data it pulls using the DatabaseController.
 */
package inventorymanagementsystem;

import static inventorymanagementsystem.IMSController.db;
import java.util.ArrayList;
import javax.swing.JPanel;

/**
 *
 * @author Mike and Nate
 */
public class UserController extends IMSController{
    public UserController(JPanel displayPanel){
        super(displayPanel);
        
    }
    @Override
    protected int[] getRowCodes(){
        int[] rowCodes = {IMSController.CODE_NUMBER, IMSController.CODE_STRING, IMSController.CODE_STRING, IMSController.CODE_NUMBER};
        return rowCodes;
    }
    @Override
    protected String[] getColumnNames(){
        String[] columnNames = {"ID","User Name","Password","Level"};
        return columnNames;
    }
    @Override
    protected Double[] getColumnWeights(){
        Double[] columnWeights = {0.05,0.8,0.8,0.05};
        return columnWeights;
    }
    @Override
    protected ArrayList getByValue(String value){
        return db.getUsers();
    }
    
    @Override
    public int getType() {
        return IMSController.TYPE_USER;
    }
}
