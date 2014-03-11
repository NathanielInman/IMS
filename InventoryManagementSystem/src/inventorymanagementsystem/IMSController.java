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
    private static int MAXIMUM_ROW_HEIGHT = 50;
    protected static int CODE_NUMBER = 0;
    protected static int CODE_STRING = 1;
    private static Border fieldBorder = BorderFactory.createBevelBorder(BevelBorder.RAISED);
    protected ArrayList<ArrayList<String>> rows = new ArrayList<>();
    public static final int[] ROW_CODES = {};
    protected GridBagLayout displayLayout = new GridBagLayout();
    public IMSController(){
        super();
        this.setLayout(displayLayout);
    }
    protected void clearInventory(){
        this.removeAll();
    }
    protected JPanel newComponent(){
        JPanel comp = new JPanel();
        comp.setPreferredSize(new Dimension(this.getWidth(),MAXIMUM_ROW_HEIGHT));
        return comp;
    }
    protected GridBagConstraints rowConstraint(int row, int field){
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.5;
        c.gridx = field;
        c.gridy = row;
        return c;
    }
    protected GridBagConstraints endConstraint(){
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1;
        c.weighty = 1;
        c.gridx = 0;
        c.gridy = rows.size();
        return c;
    }
    protected void addTextField(String text, GridBagConstraints c){
        JPanel textField = newComponent();
        textField.add(new JLabel(text));
        textField.setBorder(fieldBorder);
        this.add(textField, c);
    }
    private int getRowCode(int field){
        if(field<0 || field>=ROW_CODES.length){
            // ERROR!
            return 0;
        }
        return ROW_CODES[field];
    }
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