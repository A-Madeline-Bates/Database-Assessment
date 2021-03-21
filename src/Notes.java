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
// - check whether we need to close files? readers/writers
// - whether alphanum can contain special characters
// - make sure there are no case (ie lower/upper) problems
// - check if insert into adds empty strings if user doesn't supply enough strings, or it just denies the request.
// - check everything is in camelcase
// - are we passing things that are global to the class?
// - is it a better idea to always create temporary data models to load to rather than
//       having continuous data + clearing it? - turn make temp model into its own method
// - got through every equals and make sure IgnoreCase is selected
// - check domaintypes are correct
// - the string recognition methods could all be made into one

	//TO DO :
//  - separate more methods from their exception statements to allow testing
//	- find a fix to take dbstore out of parse try block
//	- decouple DBStore from DBModel
//	- sort exceptions in DBstore
//	- fix DBPrint
//	- fix control structure in DBServer
//  - SPLIT cmdtype further down the line
//  - investigate automated testing?
//  - maybe think about/ worry about empty cells?
//  - CREATE shouldn't automatically set USE- change that.
//  - break up excessively large methods
//  - string literals need some refining in terms of what's allowed - fix that
//  - shorten method names
//  - make sure comma separation is completely secure
//  - use methods in combination with one another- build a test strategy
//  - unpick some of the weird things we've done with booleans?
//  - add maths operators to tokeniser split
//  - sort out LIKE in SELECT
//  - move enum classes?
//  - consider fixing extra comma err on lists
//  - fix program crashing when empty command is entered
//  - stop users updating/changing ID column
//  - fix WHERE bracket problem!

//	- add ID column to all new files !!
//Any file which is committed to memory should have an ID column, as we should have
//created it.
