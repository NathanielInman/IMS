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
        //int[] rowCodes = {IMSController.CODE_NUMBER, IMSController.CODE_STRING, IMSController.CODE_STRING, IMSController.CODE_STRING, IMSController.CODE_STRING, IMSController.CODE_STRING, IMSController.CODE_STRING, IMSController.CODE_STRING};
        int[] rowCodes = {IMSController.CODE_NUMBER,
                          IMSController.CODE_STRING,
                          IMSController.CODE_PRICE,
                          IMSController.CODE_PRICE,
                          IMSController.CODE_STRING,
                          IMSController.CODE_NUMBER,
                          IMSController.CODE_NUMBER,
                          IMSController.CODE_STRING,
                          IMSController.CODE_PICTURE,
                          IMSController.CODE_NUMBER};
        return rowCodes;
    }
    @Override
    protected String[] getColumnNames(){
        String[] columnNames = {"ID","Name","Price","Wholesale","Category","V. ID","R. ID","Description","Picture","Preferred Stock"};
        return columnNames;
    }
    @Override
    protected String[] getColumnDatabaseNames(){
        String[] columnNames = {"ID","Name","Price","Wholesale","Category","vendor_ID","Royalty_ID","Description","Picture","Preferred_Stock"};
        return columnNames;
    }
    @Override
    protected Double[] getColumnWeights(){
        Double[] columnWeights = {0.1,0.4,0.2,0.2,0.3,0.1,0.1,0.8,0.3,0.2};
        return columnWeights;
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
        String[] columnNames = {"ID","Name","Phone","Ext","Address","Website","E-mail","PPOC"};
        vendorInfo.removeAll();
        vendorInfo.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.FIRST_LINE_START;
        c.weightx = .5;
        c.weighty = .5;
        c.gridx = 0;
        c.gridy = 0;
        for(int i=0; i<columnNames.length; i++){
            vendorInfo.add(new JLabel(columnNames[i]+": "+activeVendor.get(i)),c);
            c.gridx+=1;
            if(c.gridx > 2){
                c.gridy+=1;
                c.gridx = 0;
            }
        }
    }
    
    @Override
    public int getType() {
        return IMSController.TYPE_VENDOR;
    }
}
