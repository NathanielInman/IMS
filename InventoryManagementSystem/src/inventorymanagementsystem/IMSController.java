/*
 * This is the base class that the other controllers will extend.
 * It should have methods for displaying various data types, such
 * as text, pictures, and numbers.
 */
package inventorymanagementsystem;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;

/**
 *
 * 
 */
public class IMSController extends JPanel{
    // The height of each row, in pixels
    private static int MAXIMUM_ROW_HEIGHT = 50;
    // These are internal codes for all of the controllers so that they know
    // what kind of data they're handling in each column
    protected static int CODE_NUMBER = 0;
    protected static int CODE_STRING = 1;
    private static Border fieldBorder = BorderFactory.createBevelBorder(BevelBorder.RAISED);
    // Each row is a string-based ArrayList, so the rows object is an ArrayList
    // based on string-based ArrayLists
    protected ArrayList<ArrayList<String>> rows = new ArrayList<>();
    // Each subclass will have their own row code configuration
    public static final int[] ROW_CODES = {};
    protected GridBagLayout displayLayout = new GridBagLayout();
    public IMSController(){
        super();
        this.setLayout(displayLayout);
    }
    /**
     * This method simply removes all display objects.
     */
    protected void clearInventory(){
        this.removeAll();
    }
    /**
     * This creates a basic component building block for a single cell.
     * 
     * Other methods may use this as a starting point before adding text or
     * pictures and the like.
     * 
     * @return JPanel   The newly created display cell is returned.
     */
    protected JPanel newComponent(){
        JPanel comp = new JPanel();
        comp.setPreferredSize(new Dimension(this.getWidth(),MAXIMUM_ROW_HEIGHT));
        return comp;
    }
    /**
     * This function returns the GridBagConstraints appropriate for the row
     * number and data type of a cell.
     * 
     * GridBagConstraints are sort of like glue that dictate spacing in the
     * GridBagLayout.
     * 
     * @param row                   The row of the cell in question.
     * @param field                 The cell's IMSController data type.
     * @return GridBagConstraints   The newly created GridBagConstraints.
     */
    protected GridBagConstraints rowConstraint(int row, int field){
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.5;
        c.gridx = field;
        c.gridy = row;
        return c;
    }
    /**
     * This function returns the last GridBagConstraints, which differs slightly
     * from the others.
     * 
     * Without using a special one here to fill space, the existing rows stretch
     * to the bottom if there aren't enough to fill the pane.
     * 
     * @return GridBagConstraints
     */
    protected GridBagConstraints endConstraint(){
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1;
        c.weighty = 1;
        c.gridx = 0;
        c.gridy = rows.size();
        return c;
    }
    /**
     * This creates a text cell.
     * 
     * It uses the newComponent function as a base.
     * 
     * @param text  The text to appear in the cell.
     * @param c     The GridBagConstraints to position it properly.
     */
    protected void addTextField(String text, GridBagConstraints c){
        JPanel textField = newComponent();
        textField.add(new JLabel(text));
        textField.setBorder(fieldBorder);
        this.add(textField, c);
    }
    /**
     * This returns the IMSController data code of a given column.
     * 
     * It's called ROW_CODES because it's all the codes in a single row. In
     * hindsight, that probably doesn't make as much sense as COLUMN_ROWS.
     * 
     * @param field     The column index to check.
     * @return int      The data type of the column.
     */
    private int getRowCode(int field){
        if(field<0 || field>=ROW_CODES.length){
            // ERROR!
            return 0;
        }
        return ROW_CODES[field];
    }
    /**
     * This sorts the rows array based on a particular column.
     * 
     * We used Bubble Sort.
     * 
     * @param field     The column index to sort on.
     */
    protected void sortRowsBy(int field){
        int j;
        int comparison=0;
        boolean flag = true;
        ArrayList<String> temp;
        while(flag){
            flag = false;
            for(j=0; j<rows.size()-1;j++){
                if (getRowCode(field)==IMSController.CODE_NUMBER){
                    if(Double.parseDouble(rows.get(j).get(field)) > Double.parseDouble(rows.get(j+1).get(field))){
                        comparison = 1;
                    }else{
                        comparison = 0;
                    }
                }else if(getRowCode(field)==IMSController.CODE_STRING){
                    comparison = rows.get(j).get(field).compareToIgnoreCase(rows.get(j+1).get(field));
                }
                if (comparison > 0 ){
                    temp = rows.get(j);
                    rows.set(j,rows.get(j+1));
                    rows.set(j+1,temp);
                    flag = true;
                }
            }
        }

    }
}