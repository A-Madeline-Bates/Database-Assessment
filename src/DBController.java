public class DBController {
    DBModel model;

    DBController(DBModel model) {
        this.model = model;
    }

    public void handleIncomingCommand(String command) {
        System.out.println(command);
        //this has been handed the instructions from DBClient
    }

    public String getUserMessage(){
        return "hi there";
    }

    //Any file which is committed to memory should have an ID column, as we should have
    //created it. So it's not something we need to worry about when doing a straight-
    //forward file read like we are doing here.
    //When initialising a table we must add ID column automatically
    //Any file in our database would therefore contain an ID column
    //The same thing applies to 'creating' file names
}
