/*
 * This is our inventory controller. It will use the methods of IMSController
 * to display and sort the data it pulls using the DatabaseController.
 */
package inventorymanagementsystem;

import java.util.ArrayList;
import javax.swing.JPanel;

/**
 *
 * @author Mike and Nate
 */
public class InventoryController extends IMSController{
    public static int[] ICRowCodes={IMSController.CODE_NUMBER, IMSController.CODE_STRING, IMSController.CODE_PRICE, IMSController.CODE_PRICE, IMSController.CODE_STRING, IMSController.CODE_NUMBER, IMSController.CODE_NUMBER, IMSController.CODE_STRING, IMSController.CODE_PICTURE, IMSController.CODE_NUMBER};
    public static String[] ICColumnNames = {"ID","Name","Price","Wholesale","Category","V. ID","R. ID","Description","Picture","Preferred Stock"};
    public static Double[] ICColumnWeights = {0.1,0.4,0.2,0.2,0.3,0.1,0.1,0.8,0.3,0.2};
    public static String[] ICColumnDatabaseNames = {"ID","Name","Price","Wholesale","Category","vendor_ID","Royalty_ID","Description","Picture","Preferred_Stock"};
    public InventoryController(JPanel displayPanel, IMSGUI gui){
        super(displayPanel, gui);
    }
    @Override
    protected int[] getRowCodes(){
        return ICRowCodes;
    }
    @Override
    protected String[] getColumnNames(){ 
        return ICColumnNames;
    }
    @Override
    protected String[] getColumnDatabaseNames(){
        return ICColumnDatabaseNames;
    }
    @Override
    protected Double[] getColumnWeights(){
        return ICColumnWeights;
    }
    @Override
    protected ArrayList getByValue(String value){
        return db.getInventoryByColumn("category", value,IMSController.CODE_STRING);
    }
    
    @Override
    public int getType() {
        return IMSController.TYPE_INVENTORY;
    }
}
