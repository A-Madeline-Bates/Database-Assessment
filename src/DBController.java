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
        //add data to 2d arraylist of strings to hold other data
        //write this back into a file
    }

    //Any file which is committed to memory should have an ID column, as we should have
    //created it. So it's not something we need to worry about when doing a straight-
    //forward file read like we are doing here.
    //When initialising a table we must add ID column automatically
    //Any file in our database would therefore contain an ID column
    //The same thing applies to 'creating' file names

    public void handleFirstCommand(String command) {
        model.setColumnNames(command);
    }
}
