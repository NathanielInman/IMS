/*
 * This is our inventory controller. It will use the methods of IMSController
 * to display and sort the data it pulls using the DatabaseController.
 */
package inventorymanagementsystem;

import java.util.ArrayList;

/**
 *
 * @author Mike and Nate
 */
public class InventoryController extends IMSController{
    public InventoryController(){
        //showInventory();
        //filterByCategory("Trophy");
        //this.sortRowsBy(0);
    }
    @Override
    protected int[] getRowCodes(){
        int[] rowCodes = {IMSController.CODE_NUMBER, IMSController.CODE_STRING, IMSController.CODE_PRICE, IMSController.CODE_PRICE, IMSController.CODE_STRING, IMSController.CODE_NUMBER, IMSController.CODE_NUMBER, IMSController.CODE_STRING, IMSController.CODE_PICTURE, IMSController.CODE_NUMBER};
        return rowCodes;
    }
    @Override
    protected String[] getColumnNames(){
        String[] columnNames = {"ID","Name","Price","Wholesale","Category","V. ID","R. ID","Description","Picture","Preferred Stock"};
        return columnNames;
    }
    @Override
    protected Double[] getColumnWeights(){
        Double[] columnWeights = {0.1,0.4,0.2,0.2,0.3,0.1,0.1,0.8,0.3,0.2};
        return columnWeights;
    }
    @Override
    protected ArrayList getByValue(String value){
        return db.getInventoryByColumn("category", value,IMSController.CODE_STRING);
    }
}
