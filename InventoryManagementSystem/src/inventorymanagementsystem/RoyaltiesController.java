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
    private JPanel royaltyInfo = new JPanel();
    private ArrayList<String> activeRoyalty = new ArrayList<String>();
    public RoyaltiesController(JPanel displayPanel){
        super(displayPanel);
        showInventory(); //show the inventory specific to the selected royalty
    }
    public void setActiveRoyalty(String royaltyName){
        activeRoyalty = db.getRoyaltiesByName(royaltyName);
    }
    @Override
    protected int[] getRowCodes(){
        int[] rowCodes = {IMSController.CODE_NUMBER,
                          IMSController.CODE_STRING,
                          IMSController.CODE_NUMBER,
                          IMSController.CODE_NUMBER,
                          IMSController.CODE_STRING,
                          IMSController.CODE_STRING,
                          IMSController.CODE_STRING,
                          IMSController.CODE_STRING,
                          IMSController.CODE_PRICE};
        return rowCodes;
    }
    @Override
    protected String[] getColumnNames(){
        String[] columnNames = {"ID","Name","Phone","Extension","Address","Website","E-mail","PPOC","Royalty"};
        return columnNames;
    }
        @Override
    protected String[] getColumnDatabaseNames(){
        String[] columnNames = {"ID","Name","phone","extension","address","website","email","ppoc","royalty"};
        return columnNames;
    }
    @Override
    protected Double[] getColumnWeights(){
        Double[] columnWeights = {0.1,0.3,0.3,0.2,0.4,0.4,0.3,0.4,0.2};
        return columnWeights;
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
        String[] columnNames = {"ID","Name","Phone","Ext","Address","Website","E-mail","PPOC", "Royalty"};
        royaltyInfo.removeAll();
        royaltyInfo.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.FIRST_LINE_START;
        c.weightx=0.5;
        c.weighty=0.5;
        c.gridx=0;
        c.gridy=0;
        for(int i=0;i<columnNames.length;i++){
            royaltyInfo.add(new JLabel(columnNames[i]+": "+activeRoyalty.get(i)),c);
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
