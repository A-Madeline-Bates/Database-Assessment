package DBMain.CommandFiles;
import DBMain.*;
import DBMain.ModelFiles.*;
import DBMain.ParseExceptions.*;

import java.util.ArrayList;

public class CMDUpdate extends CMDWhere {
	private ArrayList<String> updatedColumns = new ArrayList<>();

	public void transformModel() throws ParseExceptions {
		String firstCommand = getTokenSafe(DomainType.TABLENAME);
		if (doesTableExist(firstCommand)) {
			setTemporaryPath(firstCommand);
			setTemporaryData();
			String secondCommand = getTokenSafe(DomainType.SET);
			if (isItSet(secondCommand)) {
				processNameVals();
				splitIfBrackets(this);
			}
		}
	}

	private boolean isItSet(String nextCommand) throws ParseExceptions{
		if (nextCommand.equalsIgnoreCase("SET")) {
			return true;
		}
		throw new InvalidCommand(nextCommand, "UPDATE [tablename]", "SET", null);
	}

	private void processNameVals() throws ParseExceptions {
		initColumnArray();
		String attributeCommand = getTokenSafe(DomainType.ATTRIBUTENAME);
		int attributeCoordinate = findAttributeTHROW(attributeCommand);
		setNameVal(attributeCoordinate);
		isItCommaSeparated(DomainType.ATTRIBUTENAME, "WHERE");
		collectNameVals();
	}

	private void collectNameVals() throws ParseExceptions{
		String nextCommand = getTokenSafe(DomainType.ATTRIBUTENAME);
		int attributeCoordinate = findAttribute(nextCommand);
		//if it's a 'WHERE', leave recursive loop
		if (isItWhere(nextCommand)) {
			return;
		}
		//attribute coordinate will be set to negative if it doesn't exist
		else if(attributeCoordinate >= 0){
			setNameVal(attributeCoordinate);
			isItCommaSeparated(DomainType.ATTRIBUTENAME, "WHERE");
			collectNameVals();
		}
		else{
			throw new DoesNotExistAttribute(nextCommand, temporaryPathModel.getFilename());
		}
	}

	private void setNameVal(int attributeCoordinate) throws ParseExceptions{
		String nextCommand = getTokenSafe(DomainType.OPERATOR);
		if(isItEquals(nextCommand)) {
			nextCommand = getTokenSafe(DomainType.VALUE);
			if (isItValidValue(nextCommand)) {
				//updatedColumns mimics what we want our table to do- cells in the updatedColumns array correspond
				//to columns of our table
				updatedColumns.set(attributeCoordinate, nextCommand);
			}
		}
	}

	//creating an array to hold our name value pair data. We are initialising all cells to 'n/a' because that would
	//not be a valid value in this context, and therefore there is no chance of it clashing with a string entered by the
	//user.
	private void initColumnArray(){
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
