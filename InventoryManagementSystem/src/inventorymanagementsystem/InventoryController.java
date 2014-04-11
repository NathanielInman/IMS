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
 * @author Mike
 */
public class InventoryController extends IMSController{
    public static final int[] ROW_CODES = {IMSController.CODE_STRING, IMSController.CODE_NUMBER};
    public InventoryController(){
        DatabaseController db = new DatabaseController();
        ArrayList categoryList = db.getCategory("Trophy");
        Iterator itr = categoryList.iterator();
        while(itr.hasNext()){       
            rows.add(new ArrayList<>(Arrays.asList(itr.next().toString(),itr.next().toString())));
        }
        this.sortRowsBy(0);
        showInventory();
    }
    private void showInventory(){
        clearInventory();
        int j;
        for(int i=0; i<rows.size(); i++){
            for(j=0; j<rows.get(i).size(); j++){
                addTextField(rows.get(i).get(j), rowConstraint(i, j));
            }
        }
        this.add(new JPanel(),endConstraint());
    }
}
