/*
 * This is the base class that the other controllers will extend.
 * It should have methods for displaying various data types, such
 * as text, pictures, and numbers.
 */
package inventorymanagementsystem;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;

/**
 *
 * 
 */
public class IMSController extends JPanel implements MouseListener{
    // The height of each row, in pixels
    private static int MAXIMUM_ROW_HEIGHT = 50;
    // These are internal codes for all of the controllers so that they know
    // what kind of data they're handling in each column
    protected static int CODE_NUMBER = 0;
    protected static int CODE_STRING = 1;
    private static Border fieldBorder = BorderFactory.createBevelBorder(BevelBorder.RAISED);
    private static Border selectedBorder = BorderFactory.createBevelBorder(BevelBorder.LOWERED);
    // Each row is a string-based ArrayList, so the rows object is an ArrayList
    // based on string-based ArrayLists
    protected ArrayList<ArrayList<String>> rows = new ArrayList<>();
    protected GridBagLayout displayLayout = new GridBagLayout();
    protected static JPopupMenu itemMenu = new JPopupMenu();
    protected static JMenuItem sortItem = new JMenuItem();
    protected static JMenuItem editItem = new JMenuItem();
    private static int activeColumn = -1;
    private static int activeRow = -1;
    public IMSController(){
        super();
        sortItem.setText("Sort by this column");
        sortItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                sortItemActionPerformed(e);
            }
        });
        editItem.setText("Edit value");
        editItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                editItemActionPerformed(e);
            }
        });
        itemMenu.add(sortItem);
        itemMenu.add(editItem);
        this.setLayout(displayLayout);
        this.addMouseListener(this);
    }
    protected int[] getRowCodes(){
        int[] rowCodes = {};
        return rowCodes;
    }
    protected String[] getColumnNames(){
        String[] columnNames = {};
        return columnNames;
    }
    public void editItemActionPerformed(java.awt.event.ActionEvent e){
        String oldValue = rows.get(activeRow).get(activeColumn);
        String s = (String)JOptionPane.showInputDialog(this.getRootPane(),"New value:","Edit",JOptionPane.PLAIN_MESSAGE,null,null,oldValue);
        if ((s != null) && (s.length() > 0)) {
            // This does not actually change the database yet..!!
            // It also does not validate data types.
            rows.get(activeRow).set(activeColumn,s);
            showInventory();
            return;
        }
    }
    public void sortItemActionPerformed(java.awt.event.ActionEvent e){
        this.sortRowsBy(activeColumn);
        this.showInventory();
    }
    public void showInventory(){
        // This exists solely to be overwritten.
    }
    public void resetActivePosition(){
        activeRow = -1;
        activeColumn = -1;
    }
    @Override
    public void mouseClicked(MouseEvent e){
        if(SwingUtilities.isRightMouseButton(e)){
            if(hasPermissionToEdit()){
                editItem.setEnabled(true);
            }else{
                editItem.setEnabled(false);
            }
            resetActivePosition();
            // Row number
            int j = e.getY()/MAXIMUM_ROW_HEIGHT;
            // Don't show the menu if you click below all the rows
            if(j<rows.size()){
                // Column number
                int i = 0;
                int totalWidth = displayLayout.getLayoutDimensions()[0][i];
                while(totalWidth <= e.getX()){
                    i += 1;
                    totalWidth += displayLayout.getLayoutDimensions()[0][i];
                }
                activeRow = j;
                activeColumn = i;
                sortItem.setText("Sort by "+getColumnName(i));
                itemMenu.show(e.getComponent(),e.getX(), e.getY());
            }
        }
    }
    /** This function returns the name of a column.
     * 
     * @param index     The index of the column (left is 0)
     * @return String   The name of the column
     */
    public String getColumnName(int index){
        if(index<0 || index>=this.getColumnNames().length){
            // ERROR!
            return "ERROR";
        }else{
            return this.getColumnNames()[index];
        }
    }
    /**
     * This method determines whether the current user can edit data.
     * 
     * Right now, it's just a placeholder function that doesn't actually check
     * anything.
     * 
     * @return boolean  True if you can edit, false if not.
     */
    private boolean hasPermissionToEdit(){
        return true;
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
        //comp.setPreferredSize(new Dimension(this.getWidth(),MAXIMUM_ROW_HEIGHT));
        comp.setPreferredSize(new Dimension(comp.getPreferredSize().width,MAXIMUM_ROW_HEIGHT));
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
        if(field<0 || field>=this.getRowCodes().length){
            // ERROR!
            return 0;
        }
        return this.getRowCodes()[field];
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

    @Override
    public void mousePressed(MouseEvent e) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void mouseExited(MouseEvent e) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }
}