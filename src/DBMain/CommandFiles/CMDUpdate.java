package DBMain.CommandFiles;
import DBMain.*;
import DBMain.ModelFiles.*;
import DBMain.ParseExceptions.*;

import java.util.ArrayList;

public class CMDUpdate extends CMDWhere {
	private ArrayList<String> updatedColumns = new ArrayList<>();

	public void transformModel() throws ParseExceptions {
		String firstCommand = getNewTokenSafe(DomainType.TABLENAME);
		if (doesTableExist(firstCommand)) {
			setTemporaryPath(firstCommand);
			setTemporaryData();
			String secondCommand = getNewTokenSafe(DomainType.SET);
			if (isItSet(secondCommand)) {
				collectNameValList();
				processForcedWhere(this);
			}
		}
	}

	private boolean isItSet(String nextCommand) throws ParseExceptions{
		if (nextCommand.equalsIgnoreCase("SET")) {
			return true;
		}
		throw new InvalidCommand(nextCommand, "UPDATE [tablename]", "SET", null);
	}

	private void collectNameValList() throws ParseExceptions {
		initialiseColsArray();
		String attributeCommand = getNewTokenSafe(DomainType.ATTRIBUTENAME);
		int attributeCoordinate = doesAttributeExist(attributeCommand);
		//this is a bit of a clunky solution- may need fixing
		if(attributeCoordinate >= 0) {
			processNameValPair(attributeCoordinate);
			isItCommaSeparated(DomainType.ATTRIBUTENAME, "WHERE");
			nameValsRecursive();
		}
	}

	private void nameValsRecursive() throws ParseExceptions{
		String nextCommand = getNewTokenSafe(DomainType.ATTRIBUTENAME);
		int attributeCoordinate = doesAttributeExist(nextCommand);
		//if it's a 'WHERE', leave recursive loop
		if (isItWhere(nextCommand)) {
			return;
		}
		//attribute coordinate will be set to negative if it doesn't exist
		else if(attributeCoordinate >= 0){
			processNameValPair(attributeCoordinate);
			isItCommaSeparated(DomainType.ATTRIBUTENAME, "WHERE");
			nameValsRecursive();
		}
		else{
			throw new DoesNotExistAttribute(nextCommand, temporaryPathModel.getFilename());
		}
	}

	private void processNameValPair(int attributeCoordinate) throws ParseExceptions{
		String nextCommand = getNewTokenSafe(DomainType.OPERATOR);
		if(isItEquals(nextCommand)) {
			nextCommand = getNewTokenSafe(DomainType.VALUE);
			if (isItValidValue(nextCommand)) {
				//updatedColumns mimics what we want our table to do- cells in the updatedColumns array correspond
				//to columns of our table
				updatedColumns.set(attributeCoordinate, nextCommand);
			}
		}
	}

	private int doesAttributeExist(String nextCommand){
		//iterate through the columns of our table until we find a match
		for(int i=0; i<temporaryDataModel.getColumnNumber(); i++){
			if(temporaryDataModel.getColumnData().get(i).equalsIgnoreCase(nextCommand)){
				return i;
			}
		}
		return -1;
	}

	//creating an array to hold our name value pair data. We are initialising all cells to 'n/a' because that would
	//not be a valid value in this context, and therefore there is no chance of it clashing with a string entered by the
	//user.
	private void initialiseColsArray(){
		for(int i=0; i<temporaryDataModel.getColumnNumber(); i++){
			updatedColumns.add("n/a");
		}
	}

	private boolean isItEquals(String nextCommand) throws ParseExceptions{
		if (nextCommand.equals("=")) {
			return true;
		}
		throw new InvalidCommand(nextCommand, "SET [attribute]", "=", null);
	}

	protected void executeCMD(ArrayList<RequestedRow> finalRows){
		System.out.println("COLUMN:" + updatedColumns + "WHERE ROW:" + finalRows);
	}

	public String query(DBServer server){
		return "Updater";
	}
}
