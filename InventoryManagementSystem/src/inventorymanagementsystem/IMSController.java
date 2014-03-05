/*
 * This is the base class that the other controllers will extend.
 * It should have methods for displaying various data types, such
 * as text, pictures, and numbers.
 */
package inventorymanagementsystem;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * 
 */
public class IMSController extends JPanel{
    public IMSController(){
        super();
        BoxLayout yLayout = new BoxLayout(this,BoxLayout.Y_AXIS);
        this.setLayout(yLayout);
        this.setSize(200, 200);
    }
    protected void addRow(){
        JPanel row = new JPanel();
        BoxLayout rowLayout = new BoxLayout(row,BoxLayout.X_AXIS);
        row.setLayout(rowLayout);
        row.add(new JTextField("test",15));
        this.add(row);
    }
}