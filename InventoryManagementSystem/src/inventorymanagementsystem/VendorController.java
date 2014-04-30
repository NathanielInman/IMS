/*
 * This is our vendor controller. It will use the methods of IMSController
 * to display and sort the data it pulls using the DatabaseController.
 */
package inventorymanagementsystem;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author Mike
 */
public class VendorController extends IMSController{
    public static String[] VCColumnNames = {"ID","Name","Phone","Ext","Address","Website","E-mail","PPOC"};
    public static String[] VCColumnDatabaseNames = {"ID","Name","Phone","Extension","Address","Website","Email","PPOC"};
    private JPanel vendorInfo = new JPanel();
    private ArrayList<String> activeVendor = new ArrayList<String>();
    public VendorController(JPanel displayPanel){
        super(displayPanel);
        showInventory(); //show the inventory specific to the selected vendor
    }
    public void setActiveVendor(String vendorName){
        activeVendor = db.getVendorsByName(vendorName);
    }
    @Override
    protected int[] getRowCodes(){
        return InventoryController.ICRowCodes;
    }
    @Override
    protected String[] getColumnNames(){
        return InventoryController.ICColumnNames;
    }
    @Override
    protected String[] getColumnDatabaseNames(){
        return InventoryController.ICColumnDatabaseNames;
    }
    @Override
    protected Double[] getColumnWeights(){
        return InventoryController.ICColumnWeights;
    }
    @Override
    protected ArrayList getByValue(String value){
        setActiveVendor(value);
        return db.getInventoryByColumn("vendor_id",activeVendor.get(0),IMSController.CODE_NUMBER);
    }
    @Override
    public void search(String term){
        setRows(db.vendorSearch(term, Integer.parseInt(activeVendor.get(0))));
        showInventory();
    }
    /**
     * This method resets the display panes.
     */
    @Override
    protected void prepareInventory(){
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.PAGE_START;
        c.gridy = 0;
        c.gridx = 0;
        c.weighty = 0;
        c.weightx = 1;
        this.add(vendorInfo, c);
        updateVendorInfo();
        vendorInfo.setMinimumSize(new Dimension(0,vendorInfo.getPreferredSize().height));
        rowScroll.setColumnHeaderView(columnLabels);
        c.fill = GridBagConstraints.BOTH;
        c.gridy = 1;
        c.weighty = 1;
        this.add(rowScroll,c);
    }
    
    private void updateVendorInfo(){
        if(activeVendor.isEmpty()){
            return;
        }
        vendorInfo.removeAll();
        vendorInfo.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.FIRST_LINE_START;
        c.weightx = .5;
        c.weighty = .5;
        c.gridx = 0;
        c.gridy = 0;
        for(int i=0; i<VCColumnNames.length; i++){
            vendorInfo.add(new JLabel(VCColumnNames[i]+": "+activeVendor.get(i)),c);
            c.gridx++;
            if(c.gridx > 2){
                c.gridy++;
                c.gridx = 0;
            }
        }
    }
    
    @Override
    public int getType() {
        return IMSController.TYPE_VENDOR;
    }
}
