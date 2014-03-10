
package inventorymanagementsystem;

/**
 *
 * @author Mike
 */
public class InventoryManagementSystem {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        /*testing database
        DatabaseController databaseController = new DatabaseController();
        databaseController.readDatabase();
        */
        InventoryController inventoryController = new InventoryController();
        IMSGUI imsGui = new IMSGUI();
        imsGui.setController(inventoryController);
        imsGui.setVisible(true);
    }
}
