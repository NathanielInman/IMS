/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
 * @author Mike & Nate
 */
public class RoyaltiesController extends IMSController{
    public static String[] RCColumnNames = {"ID","Name","Phone","Ext","Address","Website","E-mail","PPOC", "Royalty"};
    public static String[] RCColumnDatabaseNames = {"ID","Name","Phone","Extension","Address","Website","Email","PPOC", "Royalty"};
    private JPanel royaltyInfo = new JPanel();
    private ArrayList<String> activeRoyalty = new ArrayList<String>();
    public RoyaltiesController(JPanel displayPanel, IMSGUI gui){
        super(displayPanel, gui);
        showInventory(); //show the inventory specific to the selected royalty
    }
    public void setActiveRoyalty(String royaltyName){
        activeRoyalty = db.getRoyaltiesByName(royaltyName);
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
        setActiveRoyalty(value);
        return db.getInventoryByColumn("royalty_id",activeRoyalty.get(0),IMSController.CODE_NUMBER);
    }
    @Override
    public void search(String term){
        setRows(db.royaltySearch(term,Integer.parseInt(activeRoyalty.get(0))));
    }
    /*
     * This method resets the display panes
     */
    @Override
    protected void prepareInventory(){
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.PAGE_START;
        c.gridy = 0;
        c.gridx = 0;
        c.weighty=0;
        c.weightx=1;
        this.add(royaltyInfo,c);
        updateRoyaltyInfo();
        royaltyInfo.setMinimumSize(new Dimension(0,royaltyInfo.getPreferredSize().height));
        rowScroll.setColumnHeaderView(columnLabels);
        c.fill=GridBagConstraints.BOTH;
        c.gridy=1;
        c.weighty=1;
        this.add(rowScroll,c);
    }

    private void updateRoyaltyInfo(){
        if(activeRoyalty.isEmpty()){
            return;
        }
        royaltyInfo.removeAll();
        royaltyInfo.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.FIRST_LINE_START;
        c.weightx=0.5;
        c.weighty=0.5;
        c.gridx=0;
        c.gridy=0;
        for(int i=0;i<RCColumnNames.length;i++){
            royaltyInfo.add(new JLabel(RCColumnNames[i]+": "+activeRoyalty.get(i)),c);
            c.gridx++;
            if(c.gridx>2){
                c.gridy++;
                c.gridx=0;
            } //end if
        } //end for
    } //end updateRoyaltyInfo()
        
    @Override
    public int getType() {
        return IMSController.TYPE_ROYALTIES;
    } //end getType()
}
