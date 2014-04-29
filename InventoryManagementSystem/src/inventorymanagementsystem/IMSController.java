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
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import java.util.ArrayList;
import java.util.Iterator;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;

/**
 *
 * 
 */
public class IMSController extends JPanel implements MouseListener{
    // These are to tell the database controller which list to search for the search function
    public static int TYPE_INVENTORY = 0;
    public static int TYPE_VENDOR = 1;
    public static int TYPE_ROYALTIES = 2;
    public static int TYPE_USER = 3;
    // The height of each row, in pixels
    private static int MAXIMUM_ROW_HEIGHT = 50;
    // These are internal codes for all of the controllers so that they know
    // what kind of data they're handling in each column
    protected static int CODE_NUMBER = 0;
    protected static int CODE_STRING = 1;
    protected static int CODE_PRICE = 2;
    protected static int CODE_PICTURE = 3;
    private Border fieldBorder = BorderFactory.createBevelBorder(BevelBorder.RAISED);
    private Border selectedBorder = BorderFactory.createBevelBorder(BevelBorder.LOWERED);
    // Each row is a string-based ArrayList, so the rows object is an ArrayList
    // based on string-based ArrayLists
    protected ArrayList<ArrayList<String>> rows = new ArrayList<>();
    protected GridBagLayout displayLayout = new GridBagLayout();
    protected GridBagLayout rowLayout = new GridBagLayout();
    protected JPopupMenu itemMenu = new JPopupMenu();
    protected JMenuItem sortItem = new JMenuItem();
    protected JMenuItem editItem = new JMenuItem();
    protected JMenuItem deleteItem = new JMenuItem();
    protected JMenuItem addItem = new JMenuItem();
    protected JPanel rowDisplay = new JPanel();
    protected JScrollPane rowScroll;
    protected JPanel columnLabels = new JPanel();
    private int activeColumn = -1;
    private int activeRow = -1;
    public static DatabaseController db = new DatabaseController();
    public static boolean loggedIn = false;
    public static String activeUser = null;
    private JPanel displayPanel;
    public IMSController(JPanel displayPanel){
        super();
        sortItem.setText("Sort by this column");
        sortItem.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                sortItemActionPerformed(e);
            }
        });
        editItem.setText("Edit value");
        editItem.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                editItemActionPerformed(e);
            }
        });
        addItem.setText("Add new item");
        addItem.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                addItemActionPerformed(e);
            }
        });
        deleteItem.setText("Delete item");
        deleteItem.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                deleteItemActionPerformed(e);
            }
        });
        itemMenu.add(sortItem);
        itemMenu.add(editItem);
        itemMenu.add(deleteItem);
        itemMenu.add(addItem);
        this.setLayout(displayLayout);
        rowDisplay.setLayout(rowLayout);
        rowDisplay.addMouseListener(this);
        rowScroll = new JScrollPane(rowDisplay);
        rowScroll.setLayout(new ScrollPaneLayout());
        // Setting the scroll bar's unit increment makes for faster mouse wheel scrolling
        rowScroll.getVerticalScrollBar().setUnitIncrement(16);
        this.displayPanel = displayPanel;
    }
    
    public void search(String term){
        setRows(db.search(term, getType()));
        showInventory();
    }
    
    public int getType(){
        return -1;
    }
    
    protected void setRows(ArrayList categoryList) {
        rows.clear();
        Iterator itr = categoryList.iterator();
        ArrayList currentRow;
        while(itr.hasNext()){
            currentRow = new ArrayList<>();
            for(int i=0; i<getRowCodes().length; i++){
                currentRow.add(itr.next());
            }
            rows.add(new ArrayList());
            setRow(rows.size()-1,currentRow);
        }
    }
    protected void setRow(int row, ArrayList content){
        rows.get(row).clear();
        Iterator itr = content.iterator();
        for(int i=0; i<getRowCodes().length; i++){
            try{
                rows.get(row).add(itr.next().toString());
            }catch(NullPointerException npe){
                if(getRowCodes()[i]==IMSController.CODE_NUMBER || getRowCodes()[i]==IMSController.CODE_PRICE){
                    rows.get(row).add("0");
                }else{
                    rows.get(row).add("-");
                }
            }
        }
    }
    public void filterByCategory(String category){
        setRows(getByValue(category));
        this.showInventory();
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
    protected String[] getColumnDatabaseNames(){
        String[] columnNames = {};
        return columnNames;
    }
    public static void logIn(String user){
        loggedIn = true;
        activeUser = user;
    }
    public static void logOut(){
        loggedIn = false;
        activeUser = null;
    }
    private String getColumnDatabaseName(int column){
        return this.getColumnDatabaseNames()[column];
    }
    public void editItemActionPerformed(java.awt.event.ActionEvent e){
        if(this.getRowCode(activeColumn)==IMSController.CODE_PICTURE){
            // Not sure how to do this just yet.
            return;
        }
        String oldValue = rows.get(activeRow).get(activeColumn);
        String s = (String)JOptionPane.showInputDialog(this.getRootPane(),"New value:","Edit",JOptionPane.PLAIN_MESSAGE,null,null,oldValue);
        if ((s != null) && (s.length() > 0)) {
            db.changeData(this.getType(),Integer.parseInt(rows.get(activeRow).get(0)),getColumnDatabaseName(activeColumn),s);
            setRow(activeRow, db.getRowByID(Integer.parseInt(rows.get(activeRow).get(0)), this.getType()));
            showInventory();
        }
    }
    public void addItemActionPerformed(java.awt.event.ActionEvent e){
        
    }
    public void deleteItemActionPerformed(java.awt.event.ActionEvent e){
        
    }
    public void sortItemActionPerformed(java.awt.event.ActionEvent e){
        this.sortRowsBy(activeColumn);
        this.showInventory();
    }
    private byte[] getImageAt(int rowIndex){
        return db.getImageFromID(Integer.parseInt(rows.get(rowIndex).get(0)));
    }
    /**
     * This function displays the rows of data.
     * 
     */
    public void showInventory(){
        clearInventory();
        prepareInventory();
        int j;
        for(int i=0; i<rows.size(); i++){
            for(j=0; j<rows.get(i).size(); j++){
                if(getRowCode(j)==IMSController.CODE_PICTURE){
                    try{
                        addImageIcon(getImageAt(i), rowConstraint(i,j));
                    }catch(Exception e){
                        e.printStackTrace();
                        addTextField("Picture",rowConstraint(i,j));
                    }
                }else{
                    addTextField(rows.get(i).get(j), rowConstraint(i, j));
                }
            }
        }
        this.setColumnLabels();
        rowDisplay.add(new JPanel(),endConstraint());
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
    }
    protected void prepareInventory(){
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.PAGE_START;
        c.gridy = 0;
        c.gridx = 0;
        c.weighty = 0;
        c.weightx = 1;
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
        int textWidth = (int)(displayPanel.getWidth()*c.weightx/(sumArray(getColumnWeights())+1));
        textArea.setPreferredSize(new Dimension(textWidth, Short.MAX_VALUE));
        FontMetrics fm = textArea.getFontMetrics(textArea.getFont());
        int textHeight = countLines(textArea, textWidth)*fm.getHeight();
        textArea.setPreferredSize(new Dimension(textWidth, textHeight));
        textField.add(textArea);
        rowDisplay.add(textField, c);
    }
    protected void addImageIcon(byte[] blob, GridBagConstraints c) throws IOException{
        //JPanel imageIcon = newComponent();
        BufferedImage imageBlob = ImageIO.read(new ByteArrayInputStream(blob));
        JPanel imageIcon = new DrawCanvas(imageBlob);
        //imageIcon.add(new DrawCanvas(imageBlob));
        //imageIcon.createImage(50, 50);
        imageIcon.prepareImage(imageBlob,this);
        //imageIcon.imageUpdate(imageBlob, 0, 0, Image., 50, 50);
        rowDisplay.add(imageIcon,c);
        //imageIcon.getGraphics().drawImage(imageBlob,0,0,50,50,this);
    }
    private class DrawCanvas extends JPanel {
        private BufferedImage img;
        public DrawCanvas(BufferedImage img){
            this.img = img;
        }
        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(img,0,0,50,50,null);
        }
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
        int comparison;
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
                    comparison = (rows.get(j).get(field)).compareToIgnoreCase(rows.get(j+1).get(field));
                }else if(getRowCode(field)==IMSController.CODE_PICTURE){
                    comparison = 0;
                }else{
                    System.out.println("Unrecognized row code: "+getRowCode(field)+". Aborting sort...");
                    return;
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