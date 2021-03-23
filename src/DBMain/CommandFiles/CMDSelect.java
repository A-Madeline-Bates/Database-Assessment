package DBMain.CommandFiles;
import DBMain.*;
import DBMain.ModelFiles.*;
import DBMain.ParseExceptions.*;

import java.util.ArrayList;

public class CMDSelect extends CMDWhere {
	private ArrayList<Integer> requestedColumns = new ArrayList<>();

	public void transformModel() throws ParseExceptions {
		String firstCommand = getTokenSafe(DomainType.ATTRIBUTENAME);
		findTable();
		processAttributes(firstCommand);
		//calling getNewTokenSafe to step past the table name, which we've already processed
		getTokenSafe(DomainType.TABLENAME);
		processWhere(this);
	}

	/******************************************************
	 *************  METHOD TO IDENTIFY FILE  **************
	 *****************************************************/

	private void findTable() throws ParseExceptions {
		//we need to know table name before we can identify if our attribute is valid, so we start by 'peaking'
		//forwards at it.
		for(int i=1; ; i++) {
			//we search the command line for FROM- if we hit the end of the line without finding it,
			//peakTokenSafe will throw an error
			String peakOne = peakTokenSafe(i, DomainType.FROM);
			if (stringMatcher("FROM", peakOne)) {
				//if we find FROM, check if the next command is a valid table name. If it isn't,
				//doesTableExist will throw an error
				String peakTwo = peakTokenSafe(i+1, DomainType.TABLENAME);
				if (doesTableExist(peakTwo)) {
					// nothing will be stored at the end of this command's execution, so rather than using our storage
					// models, we've instantiating temporary models which we can use without running the risk of
					// creating messy data and it being stored. We will use these models for the rest of the operation.
					setTemporaryPath(peakTwo);
					setTemporaryData();
					return;
				}
			}
		}
	}

	/******************************************************
	 ***  METHODS TO READ IN THE ATTRIBUTES FOR DISPLAY  ***
	 *****************************************************/

	private void processAttributes(String firstCommand) throws ParseExceptions{
		int attributeCoordinate = findAttribute(firstCommand);
		//check if it's a 'select all' asterisk
		if(stringMatcher("*", firstCommand)){
			String nextCommand = getTokenSafe(DomainType.ATTRIBUTENAME);
			//check that it's an Asterisk followed immediately by a FROM
			if (stringMatcherTHROW("FROM", nextCommand, "SELECT *")) {
				requestAllColumns();
			}
		}
		//check if it's a normal attribute that's present in our table. If so, recursively check for more attributes.
		else if(attributeCoordinate >= 0){
			isItCommaSeparated(DomainType.ATTRIBUTENAME, "FROM");
			collectAttributes();
		}
		//throw error if it's neither
		else{
			throw new DoesNotExistAttribute(firstCommand, temporaryPathModel.getFilename());
		}
	}

	private void collectAttributes() throws ParseExceptions{
		String nextCommand = getTokenSafe(DomainType.ATTRIBUTENAME);
		int attributeCoordinate = findAttribute(nextCommand);
		//if it's a 'FROM', leave recursive loop
		if (stringMatcher("FROM", nextCommand)) {
			return;
		}
		else if (attributeCoordinate >= 0){
			requestedColumns.add(attributeCoordinate);
			if(isItCommaSeparated(DomainType.ATTRIBUTENAME, "FROM")) {
				collectAttributes();
			}
		}
		//if it's not a value or a 'FROM', throw an error
		else{
			throw new DoesNotExistAttribute(nextCommand, temporaryPathModel.getFilename());
		}
	}

	private void requestAllColumns(){
		//iterate through the columns of our table until we find a match
		for(int i=0; i<temporaryDataModel.getColumnNumber(); i++){
			requestedColumns.add(i);
		}
	}

	protected void executeCMD(ArrayList<RequestedRow> finalRows){
		System.out.println("COLUMN:" + requestedColumns + "WHERE ROW:" + finalRows);
	}

	public String query(DBServer server){
		return "Select";
	}
}
