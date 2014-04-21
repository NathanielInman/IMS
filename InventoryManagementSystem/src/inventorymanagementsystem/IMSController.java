/*
 * This is the base class that the other controllers will extend.
 * It should have methods for displaying various data types, such
 * as text, pictures, and numbers.
 */
package inventorymanagementsystem;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.font.FontRenderContext;
import java.awt.font.LineBreakMeasurer;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.*;
import javax.swing.SpringLayout.Constraints;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.text.Style;

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
        rowDisplay.addMouseListener(this);
        rowScroll = new JScrollPane(rowDisplay);
        rowScroll.setLayout(new ScrollPaneLayout());
        // Setting the scroll bar's unit increment makes for faster mouse wheel scrolling
        rowScroll.getVerticalScrollBar().setUnitIncrement(16);
    }
    public void filterByCategory(String category){
        rows = new ArrayList<ArrayList<String>>();
        ArrayList categoryList = getByValue(category);
        Iterator itr = categoryList.iterator();
        ArrayList currentRow;
        while(itr.hasNext()){
            currentRow = new ArrayList<>();
            for(int i=0; i<getRowCodes().length; i++){
                try{
                    currentRow.add(itr.next().toString());
                }catch(NullPointerException npe){
                    if(getRowCodes()[i]==IMSController.CODE_NUMBER || getRowCodes()[i]==IMSController.CODE_PRICE){
                        currentRow.add("0");
                    }else{
                        currentRow.add("-");
                    }
                }
                //rows.add(new ArrayList<>(Arrays.asList(itr.next().toString(),itr.next().toString(),itr.next().toString(),itr.next().toString(),itr.next().toString(),itr.next().toString(),itr.next().toString(),itr.next().toString(),itr.next().toString())));
            }
            rows.add(currentRow);
        }
        showInventory();
    }
    protected ArrayList getByValue(String value){
        return new ArrayList();
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
        }
    }
    public void sortItemActionPerformed(java.awt.event.ActionEvent e){
        this.sortRowsBy(activeColumn);
        this.showInventory();
    }
    /**
     * This function displays the rows of data.
     * 
     * It uses the IMSController's functionality. Instead of adding all text
     * fields, we should have it check the ROW_CODES to determine what kind
     * of data to add.
     */
    public void showInventory(){
        clearInventory();
        int j;
        for(int i=0; i<rows.size(); i++){
            for(j=0; j<rows.get(i).size(); j++){
                addTextField(rows.get(i).get(j), rowConstraint(i, j));
            }
        }
        rowDisplay.add(new JPanel(),endConstraint());
        this.setColumnLabels();
        this.revalidate();
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
            // I decided to do it this way instead of putting a listener on every
            //  single entry. I assume it's faster this way.
            // Row number
            int j = 0;
            int rdY = e.getYOnScreen()-rowDisplay.getLocationOnScreen().y;
            int rdX = e.getXOnScreen()-rowDisplay.getLocationOnScreen().x;
            while(rdY>rowDisplay.getComponent(j).getY()+rowDisplay.getComponent(j).getHeight()){
                j += this.getRowCodes().length;
            }
            // Don't show the menu if you click below all the rows
            if(j/this.getRowCodes().length<rows.size()&&j>=0){
                // Column number
                int i = 0;
                int totalWidth = rowDisplay.getComponent(j).getWidth();
                while(totalWidth <= rdX){
                    i += 1;
                    totalWidth += rowDisplay.getComponent(j+i).getWidth();
                }
                activeRow = j/this.getRowCodes().length;
                activeColumn = i;
                sortItem.setText("Sort by "+getColumnName(i));
                itemMenu.show(e.getComponent(),e.getXOnScreen()-e.getComponent().getLocationOnScreen().x, e.getYOnScreen()-e.getComponent().getLocationOnScreen().y);
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
        rowDisplay.removeAll();
        this.removeAll();
        this.repaint();
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.PAGE_START;
        c.gridy = 0;
        c.gridx = 0;
        c.weighty = 0;
        c.weightx = 1;
        //rowScroll.add(columnLabels, c);
        rowScroll.setColumnHeaderView(columnLabels);
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
        //comp.setPreferredSize(new Dimension(this.getWidth(),comp.getPreferredSize().height));
        //comp.setPreferredSize(new Dimension(comp.getPreferredSize().width,MAXIMUM_ROW_HEIGHT));
        comp.setBackground(Color.getHSBColor(0, 0, .1f));
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
        c.fill = GridBagConstraints.BOTH;
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
        textField.setBorder(fieldBorder);
        JTextArea textArea = new JTextArea();
        textArea.setText(text);
        textArea.setEditable(false);
        textArea.setOpaque(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.addMouseListener(this);
        textField.setAlignmentX(LEFT_ALIGNMENT);
        rowDisplay.add(textField, c);
        textField.add(textArea);
        int textWidth = (int)(rowScroll.getWidth()*c.weightx/(sumArray(getColumnWeights())+1));
        textArea.setPreferredSize(new Dimension(textWidth, Short.MAX_VALUE));
        FontMetrics fm = textArea.getFontMetrics(textArea.getFont());
        int textHeight = countLines(textArea, textWidth)*fm.getHeight();
        textArea.setPreferredSize(new Dimension(textArea.getPreferredSize().width, textHeight));
    }
    private static int countLines(JTextArea textArea, int textWidth) {
        if(textArea.getText().length()==0){
            return 1;
        }
        AttributedString text = new AttributedString(textArea.getText());
        FontRenderContext frc = textArea.getFontMetrics(textArea.getFont()).getFontRenderContext();
        AttributedCharacterIterator charIt = text.getIterator();
        LineBreakMeasurer lineMeasurer = new LineBreakMeasurer(charIt, frc);
        float formatWidth = (float)textWidth;
        lineMeasurer.setPosition(charIt.getBeginIndex());
        int noLines = 1;
        while (lineMeasurer.getPosition() < charIt.getEndIndex()) {
            lineMeasurer.nextLayout(formatWidth);
            noLines++;
        }
        return noLines;
    }
    private Double sumArray(Double[] arr){
        Double sum = 0.0;
        for(int i=0; i<arr.length; i++){
            sum += arr[i];
        }
        return sum;
    }
    protected void setColumnLabels(){
        GridBagConstraints c;
        columnLabels.removeAll();
        JPanel textField;
        JTextArea textLabel;
        columnLabels.setLayout(rowLayout);
        //columnLabels.setLayout(new BoxLayout(columnLabels, BoxLayout.X_AXIS));
        try{
            for(int i=0; i<this.getColumnNames().length; i++){
                textField = new JPanel();
                textLabel = new JTextArea(this.getColumnName(i));
                textLabel.setEditable(false);
                textLabel.setForeground(Color.WHITE);
                textLabel.setAlignmentX(LEFT_ALIGNMENT);
                textLabel.setOpaque(false);
                textField.setLayout(new BoxLayout(textField, BoxLayout.X_AXIS));
                textField.add(textLabel);
                textField.setBorder(fieldBorder);
                textField.setBackground(Color.DARK_GRAY);
                textField.setPreferredSize(new Dimension(rowDisplay.getComponent(i).getPreferredSize().width,textField.getPreferredSize().height));
                textField.setLocation(rowDisplay.getComponent(i).getX(),0);
                c = rowLayout.getConstraints(rowDisplay.getComponent(i));
                columnLabels.add(textField, c);
            }
        }catch(ArrayIndexOutOfBoundsException aioobe){
            // The controller is currently empty.
            columnLabels.removeAll();
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