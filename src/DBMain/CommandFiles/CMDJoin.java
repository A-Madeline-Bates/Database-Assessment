package DBMain.CommandFiles;
import DBMain.*;
import DBMain.ModelFiles.DBModelData;
import DBMain.ModelFiles.DBModelPath;
import DBMain.ParseExceptions.DoesNotExistAttribute;
import DBMain.ParseExceptions.DomainType;
import DBMain.ParseExceptions.ParseExceptions;

import java.io.IOException;

public class CMDJoin extends CMDType {
	//this is a second 'temporary' version of the model that can be used to check data without running the risk of
	//storing the data you're working on (or wiping a file)
	private DBModelData temporaryDataModel2 = new DBModelData();
	private DBModelPath temporaryPathModel2 = new DBModelPath();
	//a separate model to create our 'join' table
	private DBModelData joinModel = new DBModelData();

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
			setTemporaryModel(firstCommand, temporaryPathModel, temporaryDataModel);
			String secondCommand = getTokenSafe(DomainType.AND);
			if(stringMatcherTHROW("AND", secondCommand, "JOIN [tablename]")){
				String thirdCommand = getTokenSafe(DomainType.TABLENAME);
				if(doesTableExist(thirdCommand)) {
					setTemporaryModel(thirdCommand, temporaryPathModel2, temporaryDataModel2);
					String fourthCommand = getTokenSafe(DomainType.ON);
					if (stringMatcherTHROW("ON", fourthCommand, "JOIN [tablename] AND [tablename]")) {
						findColMatches();
					}
				}
			}
		}
	}

	private void findColMatches() throws ParseExceptions {
		String attributeCommand = getTokenSafe(DomainType.ATTRIBUTENAME);
		int firstMatch = findAttribute(attributeCommand, temporaryDataModel);
		//attempt to find attribute in our first table
		if(firstMatch>= 0) {
			//this passes on which coordinate gave the match, and which table it was in
			findSecondMatch(firstMatch, 1);
		}
		//if it's not found in table 1, search in table 2
		else{
			firstMatch = findAttribute(attributeCommand, temporaryDataModel2);
			//attempt to find attribute in second table
			if(firstMatch>= 0) {
				//this passes on which coordinate gave the match, and which table it was in
				findSecondMatch(firstMatch, 2);
			}
			else{
				throw new DoesNotExistAttribute(attributeCommand, temporaryPathModel.getFilename(),
						temporaryPathModel2.getFilename());
			}
		}
	}

	private void findSecondMatch(int firstMatch, int whichTable) throws ParseExceptions{
		String nextCommand = getTokenSafe(DomainType.AND);
		if(stringMatcherTHROW("AND", nextCommand, "ON [attribute]")) {
			nextCommand = getTokenSafe(DomainType.ATTRIBUTENAME);
			//if our first match was in table 1, search table 2 for the next attribute
			if (whichTable == 1) {
				int secondMatch = findAttributeTHROW(nextCommand, temporaryPathModel2, temporaryDataModel2);
				processMatches(whichTable, firstMatch, secondMatch);
			} //if our first match wasn't in table 1, it must be table 2- look in table 1 for next attribute.
			else {
				int secondMatch = findAttributeTHROW(nextCommand, temporaryPathModel, temporaryDataModel);
				processMatches(whichTable, firstMatch, secondMatch);
			}
		}
	}

	//whichTable identifies which table the first match belongs to
	private void processMatches(int whichTable, int firstMatch, int secondMatch){
		//take first id
		//check against every value in corresponding id table
		//if it appears multiple times, record that
			//maybe we don't need this interim step
		//continue for all rows
		//create a 2d array, with 1 cell for each column. If cell !=n/a, add the appropriate row from table 1 and
		//appropriate row from table 2 together into our join model (i.e a loop in a loop)
		//then just return the data in the join model for printing
	}


	//print just columns 1-whatever, but the 'thinking' part can operate on anything
	public void setExitMessage(){

	}
}
