/*
 * This is our inventory controller. It will use the methods of IMSController
 * to display and sort the data it pulls using the DatabaseController.
 */
package inventorymanagementsystem;

import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.JPanel;

/**
 *
 * @author Mike
 */
public class InventoryController extends IMSController{
    public static final int[] ROW_CODES = {IMSController.CODE_STRING, IMSController.CODE_NUMBER};
    public InventoryController(){
        // Just an example...
        for(int i=0; i<20; i++){
            rows.add(new ArrayList<>(Arrays.asList("TestText","100")));
            rows.add(new ArrayList<>(Arrays.asList("Lalala","42")));
            rows.add(new ArrayList<>(Arrays.asList("Seven","7")));
            rows.add(new ArrayList<>(Arrays.asList("Eighty-five","85")));
        }
        this.sortRowsBy(1);
        showInventory();
        showInventory();
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
