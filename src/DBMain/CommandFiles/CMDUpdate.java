package DBMain.CommandFiles;
import DBMain.DBLoad.DBLoad;
import DBMain.ParseExceptions.*;
import java.io.IOException;
import java.util.ArrayList;

public class CMDUpdate extends CMDWhere {
	final ArrayList<String> updatedColumns = new ArrayList<>();

	public void transformModel() throws ParseExceptions, IOException {
		String firstCommand = getTokenSafe(DomainType.TABLENAME);
		if (doesTableExist(firstCommand)) {
			setTemporaryModel(firstCommand, temporaryPathModel,temporaryDataModel);
			new DBLoad(storageData, temporaryPathModel.getDatabaseName(), firstCommand);
			String secondCommand = getTokenSafe(DomainType.SET);
			if (stringMatcherTHROW("SET", secondCommand, "UPDATE [table]")) {
				processNameVals();
				splitIfBrackets(this);
			}
		}
	}

	private void processNameVals() throws ParseExceptions {
		initColumnArray();
		String attributeCommand = getTokenSafe(DomainType.ATTRIBUTENAME);
		int attributeCoordinate = findAttributeTHROW(attributeCommand, temporaryPathModel, temporaryDataModel);
		setNameVal(attributeCoordinate);
		isItCommaSeparated(DomainType.ATTRIBUTENAME, "WHERE");
		collectNameVals();
	}

	private void collectNameVals() throws ParseExceptions{
		String nextCommand = getTokenSafe(DomainType.ATTRIBUTENAME);
		int attributeCoordinate = findAttribute(nextCommand, temporaryDataModel);
		//if it's a 'WHERE', leave recursive loop
		if (stringMatcher("WHERE", nextCommand)) {
			return;
		}
		//attribute coordinate will be set to negative if it doesn't exist
		else if(attributeCoordinate >= 0){
			setNameVal(attributeCoordinate);
			isItCommaSeparated(DomainType.ATTRIBUTENAME, "WHERE");
			collectNameVals();
		}
		else{
			throw new DoesNotExistAttribute(nextCommand, temporaryPathModel.getFilename(), null);
		}
	}

	private void setNameVal(int attributeCoordinate) throws ParseExceptions{
		String nextCommand = getTokenSafe(DomainType.OPERATOR);
		if(stringMatcherTHROW("=", nextCommand, "SET [attribute]")) {
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

	protected void returnToCMD(ArrayList<RequestedCell> finalRows) throws ParseExceptions{
		editTableValues(finalRows);
		//using storagePath.setFilename to indicate that we want to store changes
		storagePath.setFilename(temporaryPathModel.getFilename());
		setExitMessage();
	}

	private void editTableValues(ArrayList<RequestedCell> finalRows) throws ParseExceptions{
		for (int i = 0; i < storageData.getRowNumber(); i++) {
			if(finalRows.get(i).equals(RequestedCell.TRUE)) {
				for (int j = 0; j < storageData.getColumnNumber(); j++) {
					if(!updatedColumns.get(j).equals("n/a")) {
						protectIDCol(j);
						storageData.setCell(i, j, updatedColumns.get(j));
					}
				}
			}
		}
	}
}
