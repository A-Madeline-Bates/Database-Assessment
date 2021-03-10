import java.util.ArrayList;
import java.util.Arrays;

public class DBController {
    DBModel model;
    final ArrayList<String> tableData = new ArrayList<>();

    DBController(DBModel model) {
        this.model = model;
        //it might be useful to build a more detailed constructor at some point
    }

    public void handleIncomingCommand(String command) {
        //this command is just dealing with table input. Future iterations will need to
        // understand which database we're looking at
        setDataArray(command);
        //tableData.addAll(Arrays.asList(command.split("\\s+")));
        //add data to 2d arraylist of strings to hold other data
        //add id column (ignore user's, if exists)
        //set tablename and DB name (which will be files and folders)
        //write this back into a file
    }

    //CONSIDER MOVING THIS TO MORE OF A MODEL CLASS
    public void setColNameArray(String command) {
        ArrayList<String> columnValues = new ArrayList<>(Arrays.asList(command.split("\\s+")));
        for (int i = 0; i < 3; i++) {
            System.out.println(columnValues.get(i) + "*");
        }
    }

    //CONSIDER MOVING THIS TO MORE OF A MODEL CLASS
    public void setDataArray(String command){
        tableData.addAll(Arrays.asList(command.split("\\s+")));
    }

    public void printTable() {
        for (int i = 0; i < 11; i++) {
            System.out.println(tableData.get(i) + "*");
        }
    }
}
