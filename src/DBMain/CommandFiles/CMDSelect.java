package DBMain.CommandFiles;
import DBMain.*;
import DBMain.ModelFiles.*;
import DBMain.ParseExceptions.*;

import java.util.ArrayList;

public class CMDSelect extends CMDWhere {
	private ArrayList<Integer> requestedColumns = new ArrayList<>();

	public void transformModel() throws ParseExceptions {
		String firstCommand = getNewTokenSafe(DomainType.ATTRIBUTENAME);
		if(canWeReadTable()){
			if(doesWildAttributeExist(firstCommand)){
				//calling getNewTokenSafe to step past the table name, which we've already processed
				getNewTokenSafe(DomainType.TABLENAME);
				processWhere(this);
			}
		}
	}

	/******************************************************
	 *************  METHOD TO IDENTIFY FILE  **************
	 *****************************************************/

	private boolean canWeReadTable() throws ParseExceptions {
		//we need to know table name before we can identify if our attribute is valid, so we start by 'peaking'
		//forwards at it.
		for(int i=1; ; i++) {
			//we search the command line for FROM- if we hit the end of the line without finding it,
			//peakTokenSafe will throw an error
			String peakOne = peakTokenSafe(i, DomainType.FROM);
			if (isItFrom(peakOne)) {
				//if we find FROM, check if the next command is a valid table name. If it isn't,
				//doesTableExist will throw an error
				String peakTwo = peakTokenSafe(i+1, DomainType.TABLENAME);
				if (doesTableExist(peakTwo)) {
					//nothing will be stored at the end of this command's execution, so rather than using our storage
					// models, we've instantiating temporary models which we can use without running the risk of
					// creating messy data and it being stored. We will use these models for the rest of the operation.
					setTemporaryPath(peakTwo);
					setTemporaryData();
					return true;
				}
			}
		}
	}

	/******************************************************
	 ***  METHODS TO READ IN THE ATTRIBUTES FOR DISPLAY  ***
	 *****************************************************/

	private boolean doesWildAttributeExist(String firstCommand) throws ParseExceptions{
		//check if it's a 'select all' asterisk
		if(isItAsterisk(firstCommand)){
			String nextCommand = getNewTokenSafe(DomainType.ATTRIBUTENAME);
			//check that it's an Asterisk followed immediately by a FROM
			if (isItFromTHROW(nextCommand, "SELECT *")) {
				requestAllColumns();
			}
			return true;
		}
		//check if it's a normal attribute that's present in our table. If so, recursively check for more attributes.
		else if(doesAttributeExist(firstCommand)){
			isItCommaSeparated(DomainType.ATTRIBUTENAME, "FROM");
			doesAttrExistRecursive();
			return true;
		}
		//throw error if it's neither
		else{
			throw new DoesNotExistAttribute(firstCommand, temporaryPathModel.getFilename());
		}
	}

	private void doesAttrExistRecursive() throws ParseExceptions{
		String nextCommand = getNewTokenSafe(DomainType.ATTRIBUTENAME);
		//if it's a 'FROM', leave recursive loop
		if (isItFrom(nextCommand)) {
			return;
		}
		//if it fits the conditions to be a attribute, call doesAttrExistRecursive again
		//doesAttrExistRecursive will automatically save the coordinate if it is valid
		else if (doesAttributeExist(nextCommand)){
			if(isItCommaSeparated(DomainType.ATTRIBUTENAME, "FROM")) {
				doesAttrExistRecursive();
			}
		}
		//if it's not a value or a 'FROM', throw an error
		else{
			throw new DoesNotExistAttribute(nextCommand, temporaryPathModel.getFilename());
		}
	}

	private boolean doesAttributeExist(String nextCommand){
		//iterate through the columns of our table until we find a match
		for(int i=0; i<temporaryDataModel.getColumnNumber(); i++){
			if(temporaryDataModel.getColumnData().get(i).equalsIgnoreCase(nextCommand)){
				//if we get an attribute match, save the column coordinate where the match occurred
				requestedColumns.add(i);
				return true;
			}
		}
		return false;
	}

	private void requestAllColumns(){
		//iterate through the columns of our table until we find a match
		for(int i=0; i<temporaryDataModel.getColumnNumber(); i++){
			requestedColumns.add(i);
		}
	}

	private boolean isItAsterisk(String nextCommand){
		if (nextCommand.equals("*")) {
			return true;
		}
		return false;
	}

	private boolean isItFrom(String nextCommand){
		if (nextCommand.equalsIgnoreCase("FROM")) {
			return true;
		}
		return false;
	}

	private boolean isItFromTHROW(String nextCommand, String prevCommand) throws ParseExceptions{
		if (isItFrom(nextCommand)) {
			return true;
		}
		throw new InvalidCommand(nextCommand, prevCommand, "FROM", null);
	}

	protected void executeCMD(ArrayList<RequestedRow> finalRows){
		System.out.println("COLUMN:" + requestedColumns + "WHERE ROW:" + finalRows);
	}

	public String query(DBServer server){
		return "Select";
	}
}
