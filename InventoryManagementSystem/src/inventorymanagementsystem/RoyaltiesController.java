/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package inventorymanagementsystem;

import java.util.ArrayList;
import javax.swing.JPanel;

/**
 *
 * @author Mike
 */
public class RoyaltiesController extends IMSController{
    public RoyaltiesController(JPanel displayPanel){
        super(displayPanel);
    }
    @Override
    protected int[] getRowCodes(){
        int[] rowCodes = {IMSController.CODE_NUMBER, IMSController.CODE_STRING, IMSController.CODE_NUMBER, IMSController.CODE_NUMBER, IMSController.CODE_STRING, IMSController.CODE_STRING, IMSController.CODE_STRING, IMSController.CODE_STRING, IMSController.CODE_PRICE};
        return rowCodes;
    }
    @Override
    protected String[] getColumnNames(){
        String[] columnNames = {"ID","Name","Phone","Extension","Address","Website","E-mail","PPOC","Royalty"};
        return columnNames;
    }
    @Override
    protected Double[] getColumnWeights(){
        Double[] columnWeights = {0.1,0.3,0.3,0.2,0.4,0.4,0.3,0.4,0.2};
        return columnWeights;
    }
    @Override
    protected ArrayList getByValue(String value){
        return db.getRoyalties();
    }
}
