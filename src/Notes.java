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
// - 	check everything is in camelcase
//  - 	shorten method names - prioritise short method names over perfect ones

// - 	the string recognition methods could all be made into one
// - 	separate more methods from their exception statements to allow testing
//  - 	break up excessively large methods
//  - 	unpick some of the weird things we've done with booleans?
//  - 	make some of 'where' methods private

	//TO DO DB CLASSES :
//	- find a fix to take dbstore out of parse try block
//	- decouple DBStore from DBModel
//	- sort exceptions in DBstore
//	- fix DBPrint
//	- fix control structure in DBServer
//  - decide whether to implement 'if there is no databaseFiles, create one'

//  - make CMDCondition

//TO DO CMD CLASSES :
//  - SPLIT cmdtype further down the line
//  - maybe think about/ worry about empty cells?
//  - CREATE shouldn't automatically set USE- change that.
//  - sort out LIKE in SELECT
//	- implement JOIN
//  - consider fixing extra comma err on lists
//  - fix program crashing when empty command is entered
//  - stop users updating/changing ID column
//  - split into more classes/ decouple from model
//  - break any cyclic dependency

//TO DO REGEX :
//  - string literals need some refining in terms of what's allowed - fix that. ANYTHING APART FROM TABS AND SINGLE QUOTES ALLOWED
//  - add maths operators to tokeniser split

//TO DO TESTING/GENERAL :
//  - investigate automated testing?
//  - use methods in combination with one another- build a test strategy

//  - look again at good practice code rules from reading week

