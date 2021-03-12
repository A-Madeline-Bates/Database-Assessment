public class DBController {
    DBModelFileData modelData;
    DBModelFilePath modelPath;

    DBController(DBModelFileData modelData, DBModelFilePath modelPath) {
        this.modelData = modelData;
        this.modelPath = modelPath;
    }

    public void handleIncomingCommand(String command) {
        System.out.println(command);
        //break this command into individual words so it can be parsed

        //this has been handed the instructions from DBClient
        //The idea is that this class will call model methods, DBStore and DBLoad as appropriate-
        //although this might change
    }

    public String getUserMessage(){
        return "hello there";
        //return printMessage.stringifyTable();
    }

    //Any file which is committed to memory should have an ID column, as we should have
    //created it. So it's not something we need to worry about when doing a straight-
    //forward file read like we are doing here.
    //When initialising a table we must add ID column automatically
    //Any file in our database would therefore contain an ID column
    //The same thing applies to 'creating' file names

//    USE: changes the database against which the following queries will be run
//    -needs to be able to alter the file we are looking at
//    ALTER MODEL

//    CREATE: constructs a new database or table (depending on the provided parameters)
//    -creates a new database/table
//    -if attributes attached to the table, then set columns
//    ALTER MODEL

//    INSERT: adds a new entity to an existing table
//    -needs to be able to add values to the end of the table
//    ALTER MODEL

//    SELECT: searches for entities that match the given condition
//    -selects one column, multiple columns or all columns from table
//    -if 'where' is selcted, needs to be able to refine
//    PRINT TABLE

//    UPDATE: changes the data contained within a table
//    -needs to be able to set one column or multiple columns equal to a value
//    -if 'where' is selected, needs to be able to refine
//    ALTER MODEL

//    ALTER: changes the structure (rows) of an existing table
//    -needs to be able to add or remove columns
//    ALTER MODEL

//    DELETE: removes entities that match the given condition from an existing table
//    -needs to be able to delete rows from a table where certain conditions are met
//    ALTER MODEL

//    DROP: removes a specified table from a database, or removes the entire database
//    -needs to be able to delete a table or database (whether is table or DB is specified)
//    SOMETHING A BIT DIFFERENT

//    JOIN: performs an inner join on two tables (returning all permutations of all matching entities)
//    -needs to be able to read 2 tables, match a column which they both share and display every row
//    where there's a shared value in the shared column
//    PRINT TABLE

//    build a structure which populates model with exactly what we want to see in the table
//    then build a print method which is able to print it all out
//    perhaps separate treatment of instructions which alter the model and which print the table

//    maybe printing should be put back in a 'view' context (although obviously things are not
//    always printed) - something is always printed with 'yes' or 'error'

//    implement an instruction factory or tree?

//    print and store could be singleton classes??

//    we need a new instance of the model for each new file we're looking at'
}
