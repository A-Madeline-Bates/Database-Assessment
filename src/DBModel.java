import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DBModel {
    //consider altering to work as a 'factory'?
    final List<List<String>> tableData = new ArrayList<List<String>>();
    final ArrayList<String> columnNames = new ArrayList<>();
    private int row = 0;

    public DBModel(){
    }

    public void setColumnNames(String command) {
        columnNames.addAll(Arrays.asList(command.split("\\s+")));
    }

    public ArrayList<String> getColumnNames() {
        return columnNames;
    }

    //By finding the number of column headers, we can see how many columns we have
    public int getColumnNumber(){
        return columnNames.size();
    }

    //Row number is determined by the number of times setDataArray() has been called
    public int getRowNumber(){
        return row;
    }

    public void setDataArray(String command){
        int columnNo = getColumnNumber();
        initialiseRow(columnNo);
        tableData.get(row).addAll(Arrays.asList(command.split("\\s+")));
        row++;
    }

    public List<List<String>> getDataArray() {
        return tableData;
    }

    private void initialiseRow(int columnNo){
        List<String> rowData = new ArrayList<>();
        for (int j = 0; j < columnNo; j++){
            rowData.add(null);
        }
        tableData.add(rowData);
    }

}
