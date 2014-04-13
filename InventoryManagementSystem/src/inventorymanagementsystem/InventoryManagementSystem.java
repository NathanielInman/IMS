
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
        InventoryController inventoryController = new InventoryController();
        IMSGUI imsGui = new IMSGUI();
        imsGui.setController(inventoryController);
        imsGui.setLocationRelativeTo(null);
        imsGui.setVisible(true);
    }
}
