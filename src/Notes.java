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



	//THINGS TO CHECK :
// - check if insert into adds empty strings if user doesn't supply enough strings, or it just denies the request.

	//TO DO SIMPLE/AESTHETIC :
// - 	separate more methods from their exception statements to allow testing
//  - 	unpick some of the weird things we've done with booleans?

	//TO DO DB CLASSES :
//	- decouple DBStore from DBModel
//  - decide whether to implement 'if there is no databaseFiles, create one'
//  - clean up CMDfactory?

//TO DO CMD CLASSES :
//  - maybe think about/ worry about empty cells?
//  - CREATE shouldn't automatically set USE- change that.
//  - sort out LIKE in SELECT
//	- implement JOIN
//  - consider fixing extra comma err on lists
//  - fix program crashing when empty command is entered
//  - stop users updating/changing ID column
//  - split into more classes/ decouple from model
//  - break any cyclic dependency
//  - finish unfinished CMDs
//  - decouple!!

//TO DO REGEX :
//  - string literals need some refining in terms of what's allowed - fix that. ANYTHING APART FROM TABS AND SINGLE QUOTES ALLOWED
//  - add maths operators to tokeniser split

//TO DO TESTING/GENERAL :
//  - investigate automated testing?
//  - use methods in combination with one another- build a test strategy

//  - delete testing classes before submitting
//  - check all intellij warnings
