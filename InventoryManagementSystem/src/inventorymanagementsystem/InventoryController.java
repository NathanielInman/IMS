/*
 * This is our inventory controller. It will use the methods of IMSController
 * to display and sort the data it pulls using the DatabaseController.
 */
package inventorymanagementsystem;

import java.awt.Dimension;
import javax.swing.JPanel;

/**
 *
 * @author Mike
 */
public class InventoryController extends IMSController{
    public InventoryController(){
        // Just an example...
        for(int i=0; i<2; i++){
            showInventory();
        }
    }
    private void showInventory(){
        JPanel row = addRow();
        JPanel firstThing = this.addTextField("test text");
        row.add(firstThing);
        row.add(addTextField("Second thing lalala test text #2"));
        this.add(row);
    }
}
