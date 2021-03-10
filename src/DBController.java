import java.util.ArrayList;
import java.util.Arrays;

public class DBController {
    DBModel model;

    DBController(DBModel model) {
        this.model = model;
        //it might be useful to build a more detailed constructor at some point
    }

    public void handleIncomingCommand(String command) {
        //this command is just dealing with table input. Future iterations will need to
        // understand which database we're looking at
        model.setDataArray(command);
        //tableData.addAll(Arrays.asList(command.split("\\s+")));
        //add data to 2d arraylist of strings to hold other data
        //add id column (ignore user's, if exists)
        //set tablename and DB name (which will be files and folders)
        //write this back into a file
    }

    public void handleFirstCommand(String command) {
        model.setColumnNames(command);
    }

}
