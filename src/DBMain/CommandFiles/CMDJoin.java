package DBMain.CommandFiles;
import DBMain.*;
import DBMain.ModelFiles.DBModelData;
import DBMain.ModelFiles.DBModelPath;
import DBMain.ParseExceptions.DomainType;
import DBMain.ParseExceptions.ParseExceptions;

import java.io.IOException;

public class CMDJoin extends CMDType {
	//this is a second 'temporary' version of the model that can be used to check data without running the risk of
	//storing the data you're working on (or wiping a file)
	protected DBModelData temporaryDataModel = new DBModelData();
	protected DBModelPath temporaryPathModel = new DBModelPath();


//	The JOIN query should perform an inner join on the two specified tables
// You need not store the combined table on the filesystem, but just return the complete table to the user
// This is equivalent to performing SELECT * on the combined table
// The entry IDs should be generated for this table (they are NOT the IDs from the original two tables)
// Note: generated attribute names are just examples, but you might like to use those suggested

//	JOIN: performs an inner join on two tables (returning all permutations of all matching entities)
//<Join>           ::=  JOIN <TableName> AND <TableName> ON <AttributeName> AND <AttributeName>


	public void transformModel() throws ParseExceptions, IOException {
		String firstCommand = getTokenSafe(DomainType.TABLENAME);
		if(doesTableExist(firstCommand)){

		}
	}

	public void setExitMessage(){

	}
}
