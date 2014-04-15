/*
 * This is the base class that the other controllers will extend.
 * It should have methods for displaying various data types, such
 * as text, pictures, and numbers.
 */
package inventorymanagementsystem;

import java.awt.Dimension;
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
    protected static int CODE_PRICE = 2;
    protected static int CODE_PICTURE = 3;
    private static Border fieldBorder = BorderFactory.createBevelBorder(BevelBorder.RAISED);
    private static Border selectedBorder = BorderFactory.createBevelBorder(BevelBorder.LOWERED);
    // Each row is a string-based ArrayList, so the rows object is an ArrayList
    // based on string-based ArrayLists
    protected ArrayList<ArrayList<String>> rows = new ArrayList<>();
    protected GridBagLayout displayLayout = new GridBagLayout();
    protected GridBagLayout rowLayout = new GridBagLayout();
    protected static JPopupMenu itemMenu = new JPopupMenu();
    protected static JMenuItem sortItem = new JMenuItem();
    protected static JMenuItem editItem = new JMenuItem();
    protected JPanel rowDisplay = new JPanel();
    protected JScrollPane rowScroll;
    protected JPanel columnLabels = new JPanel();
    private static int activeColumn = -1;
    private static int activeRow = -1;
    public static DatabaseController db = new DatabaseController();
    public static boolean loggedIn = false;
    public static String activeUser = null;
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
        rowDisplay.setLayout(rowLayout);
        //columnLabels.setLayout(rowLayout);
        this.addMouseListener(this);
        rowScroll = new JScrollPane(rowDisplay);
        rowScroll.setLayout(new ScrollPaneLayout());
        // Setting the scroll bar's unit increment makes for faster mouse wheel scrolling
        rowScroll.getVerticalScrollBar().setUnitIncrement(16);
        //rowScroll.add(rowDisplay);
    }
    protected int[] getRowCodes(){
        int[] rowCodes = {};
        return rowCodes;
    }
    protected String[] getColumnNames(){
        String[] columnNames = {};
        return columnNames;
    }
    protected Double[] getColumnWeights(){
        Double[] columnWeights = {};
        return columnWeights;
    }
    public static void logIn(String user){
        loggedIn = true;
        activeUser = user;
    }
    public static void logOut(){
        loggedIn = false;
        activeUser = null;
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
            int j = (e.getY()-columnLabels.getHeight())/MAXIMUM_ROW_HEIGHT;
            // Don't show the menu if you click below all the rows
            if(j<rows.size()&&j>=0){
                // Column number
                int i = 0;
                int totalWidth = rowLayout.getLayoutDimensions()[j][i];
                while(totalWidth <= e.getX()){
                    i += 1;
                    totalWidth += rowLayout.getLayoutDimensions()[j][i];
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
        return loggedIn;
    }
    /**
     * This method resets the display panes.
     */
    protected void clearInventory(){
        //rowDisplay.removeAll();
        this.removeAll();
        //rowScroll.removeAll();
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.PAGE_START;
        c.gridy = 0;
        c.gridx = 0;
        c.weighty = 0;
        c.weightx = 1;
        this.add(columnLabels,c);
        //columnLabels.setMinimumSize(new Dimension(0,20));
        c.fill = GridBagConstraints.BOTH;
        c.gridy = 1;
        c.weighty = 1;
        this.add(rowScroll,c);
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
        //comp.setPreferredSize(new Dimension(comp.getPreferredSize().width,MAXIMUM_ROW_HEIGHT));
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
        c.weightx = this.getColumnWeights()[field];
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
        c.weightx = 0;
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
        rowDisplay.add(textField, c);
    }
    protected void setColumnLabels(){
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.LINE_START;
        columnLabels.removeAll();
        JPanel textField;
        JTextArea textLabel;
        for(int i=0; i<this.getColumnNames().length; i++){
            c.weightx = this.getColumnWeights()[i];
            textField = new JPanel();
            textLabel = new JTextArea(this.getColumnName(i));
            textLabel.setEditable(false);
            textLabel.setLineWrap(true);
            //textLabel.setBounds(0, 0, 10, 10);
            textLabel.setSize(rowDisplay.getComponent(i).getSize());
            textField.add(textLabel);
            textField.setBorder(fieldBorder);
            //textField.setSize(rowDisplay.getComponent(i).getSize());
            textField.setSize(rowDisplay.getComponent(i).getSize());
            //textField.setMaximumSize(rowDisplay.getComponent(i).getMaximumSize());
            //textField.setMinimumSize(rowDisplay.getComponent(i).getMinimumSize());
            columnLabels.add(textField,c);
            /*JLabel label = new JLabel(this.getColumnName(i));
            label.setLocation(this.getX()+(int)(this.getWidth()*this.getColumnWeights()[i]), 0);
            columnLabels.add(label);*/
        }
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
                if (getRowCode(field)==IMSController.CODE_NUMBER || getRowCode(field)==IMSController.CODE_PRICE){
                    if(Double.parseDouble(rows.get(j).get(field)) > Double.parseDouble(rows.get(j+1).get(field))){
                        comparison = 1;
                    }else{
                        comparison = 0;
                    }
                }else if(getRowCode(field)==IMSController.CODE_STRING){
                    comparison = rows.get(j).get(field).compareToIgnoreCase(rows.get(j+1).get(field));
                }else if(getRowCode(field)==IMSController.CODE_PICTURE){
                    comparison = 0;
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