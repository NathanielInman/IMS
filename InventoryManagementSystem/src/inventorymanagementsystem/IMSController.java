/*
 * This is the base class that the other controllers will extend.
 * It should have methods for displaying various data types, such
 * as text, pictures, and numbers.
 */
package inventorymanagementsystem;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;

/**
 *
 * 
 */
public class IMSController extends JPanel{
    private static int MAXIMUM_ROW_HEIGHT = 50;
    protected static int CODE_NUMBER = 0;
    protected static int CODE_STRING = 1;
    private static Border fieldBorder = BorderFactory.createBevelBorder(BevelBorder.RAISED);
    public IMSController(){
        super();
        BoxLayout yLayout = new BoxLayout(this,BoxLayout.Y_AXIS);
        this.setLayout(yLayout);
    }
    protected JPanel addTextField(String text){
        JPanel textField = new JPanel();
        textField.add(new JLabel(text));
        textField.setBorder(fieldBorder);
        return textField;
    }
    protected JPanel addRow(){
        JPanel row = new JPanel();
        row.setBorder(BorderFactory.createLineBorder(Color.black));
        BoxLayout rowLayout = new BoxLayout(row, BoxLayout.X_AXIS);
        row.setLayout(rowLayout);
        return row;
    }
    protected void updatePanel(){
        Component rows[] = this.getComponents();
        for(int i=0; i<rows.length; i++){
            rows[i].setMaximumSize(new Dimension(this.getWidth(),MAXIMUM_ROW_HEIGHT));
        }
    }
}