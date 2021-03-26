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




	//TO DO :
//	- decouple

//  - extra JOIN features
//  - use methods in combination with one another- build a test strategy

//  - clean up CMDfactory?
//  - consider fixing extra comma err on lists
//  -   remove quote marks in print?

