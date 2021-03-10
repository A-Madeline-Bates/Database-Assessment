import java.util.ArrayList;
import java.util.Arrays;

public class DBModel {
    //consider altering to work as a 'factory'?
    final ArrayList<String> tableData = new ArrayList<>();

    public void setColumnNames(String command) {
        ArrayList<String> columnValues = new ArrayList<>(Arrays.asList(command.split("\\s+")));
        for (int i = 0; i < 3; i++) {
            System.out.println(columnValues.get(i) + "*");
        }
    }

    public void setDataArray(String command){
        tableData.addAll(Arrays.asList(command.split("\\s+")));
    }

    public ArrayList<String> getDataArray() {
        return tableData;
    }

//    public void printTable() {
//        for (int i = 0; i < 11; i++) {
//            System.out.println(tableData.get(i) + "*");
//        }
//    }
}
