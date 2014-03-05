/*
 * This is our inventory controller. It will use the methods of IMSController
 * to display and sort the data it pulls using the DatabaseController.
 */
package inventorymanagementsystem;

/**
 *
 * @author Mike
 */
public class InventoryController extends IMSController{
    public InventoryController(){
        // Just an example...
        addRow();
        addRow();
        addRow();
    }
}
