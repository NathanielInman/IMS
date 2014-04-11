/*
 * This is our inventory controller. It will use the methods of IMSController
 * to display and sort the data it pulls using the DatabaseController.
 */
package inventorymanagementsystem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import javax.swing.JPanel;

/**
 *
 * @author Mike and Nate
 */
public class InventoryController extends IMSController{
    public InventoryController(){
        DatabaseController db = new DatabaseController();
        ArrayList categoryList = db.getInventoryByCategory("Trophy");
        Iterator itr = categoryList.iterator();
        db.getCategoryList();
        while(itr.hasNext()){       
            rows.add(new ArrayList<>(Arrays.asList(itr.next().toString(),itr.next().toString())));
        }
        this.sortRowsBy(0);
        showInventory();
    }
    /**
     * This function displays the rows of data.
     * 
     * It uses the IMSController's functionality. Instead of adding all text
     * fields, we should have it check the ROW_CODES to determine what kind
     * of data to add.
     */
    @Override
    public void showInventory(){
        clearInventory();
        int j;
        for(int i=0; i<rows.size(); i++){
            for(j=0; j<rows.get(i).size(); j++){
                addTextField(rows.get(i).get(j), rowConstraint(i, j));
            }
        }
        this.add(new JPanel(),endConstraint());
        this.validate();
    }
    @Override
    protected int[] getRowCodes(){
        int[] rowCodes = {IMSController.CODE_STRING, IMSController.CODE_NUMBER};
        return rowCodes;
    }
    @Override
    protected String[] getColumnNames(){
        String[] columnNames = {"Name","Number"};
        return columnNames;
    }
}
