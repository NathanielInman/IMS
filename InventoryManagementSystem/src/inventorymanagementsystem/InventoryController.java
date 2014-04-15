/*
 * This is our inventory controller. It will use the methods of IMSController
 * to display and sort the data it pulls using the DatabaseController.
 */
package inventorymanagementsystem;

import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.JPanel;

/**
 *
 * @author Mike and Nate
 */
public class InventoryController extends IMSController{
    public InventoryController(){
        DatabaseController db = new DatabaseController();
        ArrayList categoryList = db.getInventoryByCategory("Trophy");
        Iterator itr = categoryList.iterator();
        db.getCategoryList();
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
            }
            rows.add(currentRow);
            //rows.add(new ArrayList<>(Arrays.asList(itr.next().toString(),itr.next().toString(),itr.next().toString(),itr.next().toString(),itr.next().toString(),itr.next().toString(),itr.next().toString(),itr.next().toString(),itr.next().toString())));
        }
        this.sortRowsBy(0);
    }
    /**
     * This function displays the rows of data.
     * 
     * It uses the IMSController's functionality. Instead of adding all text
     * fields, we should have it check the ROW_CODES to determine what kind
     * of data to add.
     */
    @Override
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
        this.validate();
    }
    @Override
    protected int[] getRowCodes(){
        int[] rowCodes = {IMSController.CODE_NUMBER, IMSController.CODE_STRING, IMSController.CODE_PRICE, IMSController.CODE_PRICE, IMSController.CODE_STRING, IMSController.CODE_NUMBER, IMSController.CODE_NUMBER, IMSController.CODE_STRING, IMSController.CODE_PICTURE, IMSController.CODE_NUMBER};
        return rowCodes;
    }
    @Override
    protected String[] getColumnNames(){
        String[] columnNames = {"ID","Name","Price","Wholesale","Category","V. ID","R. ID","Description","Picture","Preferred Stock"};
        return columnNames;
    }
    @Override
    protected Double[] getColumnWeights(){
        Double[] columnWeights = {0.1,0.3,0.2,0.2,0.3,0.1,0.1,0.5,0.3,0.2};
        return columnWeights;
    }
}
