import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DBModel {
    //consider altering to work as a 'factory'?
    final List<List<String>> tableData = new ArrayList<List<String>>();
    final ArrayList<String> columnNames = new ArrayList<>();
    private int row = 0;
    private String filename;

    public DBModel(){
    }

    public void setColumnNames(String command) {
        columnNames.addAll(Arrays.asList(command.split("\\t")));
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
        return row - 1;
    }

    public void setDataArray(String command){
        int columnNo = getColumnNumber();
        initialiseRow(columnNo);
        tableData.get(row).addAll(Arrays.asList(command.split("\\t")));
        row++;
    }

    //TEST THIS?
    //setDataArray() is called row by row, and we are exploiting this to initialise
    //each row as we go
    private void initialiseRow(int columnNo){
        List<String> rowData = new ArrayList<>();
        tableData.add(rowData);
    }

    public List<List<String>> getDataArray() {
        return tableData;
    }

    public void setFilename(String filename){
        this.filename = filename;
    }

    public String getFilename(){
        return filename;
    }
}
