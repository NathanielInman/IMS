
package inventorymanagementsystem;

import javax.swing.UIManager;

/**
 *
 * @author Mike
 */
public class InventoryManagementSystem {

    /**
     * @param args the command line arguments
     */
    public static InventoryController inventoryController;
    public static VendorController vendorController;
    public static void main(String[] args) throws Exception {
        try {
            UIManager.setLookAndFeel("com.jtattoo.plaf.hifi.HiFiLookAndFeel");
         } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(IMSGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(IMSGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(IMSGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(IMSGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        // Controllers must be instantiated after look and feel
        inventoryController = new InventoryController();
        vendorController = new VendorController();
        IMSGUI imsGui = new IMSGUI();
        imsGui.setLocationRelativeTo(null);
        imsGui.setVisible(true);
    }
    public static InventoryController getInventoryController(){
        return inventoryController;
    }
    public static VendorController getVendorController(){
        return vendorController;
    }
}
