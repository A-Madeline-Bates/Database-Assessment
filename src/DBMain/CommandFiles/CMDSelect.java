package DBMain.CommandFiles;
import DBMain.DBEnums.DomainType;
import DBMain.DBEnums.RequestedCell;
import DBMain.DBTokeniser.DBTokeniser;
import DBMain.ModelFiles.DBModelPath;
import DBMain.ParseExceptions.*;
import java.io.IOException;
import java.util.ArrayList;

public class CMDSelect extends ProcessWhere {
	final ArrayList<RequestedCell> requestedColumns = new ArrayList<>();

	public CMDSelect(DBTokeniser tokeniser, DBModelPath path) throws IOException, ParseExceptions {
		buildCommand(tokeniser, path);
	}

	public void transformModel() throws ParseExceptions, IOException {
		String firstCommand = getTokenSafe(DomainType.ATTRIBUTENAME);
		findTable();
		initRequestedCols();
		processAttributes(firstCommand);
		//calling getNewTokenSafe to step past the table name, which we've already processed
		getTokenSafe(DomainType.TABLENAME);
		processWhere(this);
	}

	private void initRequestedCols(){
		for(int i=0; i<temporaryDataModel.getColumnNumber(); i++){
			requestedColumns.add(RequestedCell.FALSE);
		}
	}

	/******************************************************
	 *************  METHOD TO IDENTIFY FILE  **************
	 *****************************************************/

	private void findTable() throws BNFError, IOException {
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
					setTemporaryModel(peakTwo, temporaryPathModel, temporaryDataModel);
					return;
				}
			}
		}
	}

	/******************************************************
	 ***  METHODS TO READ IN THE ATTRIBUTES FOR DISPLAY  ***
	 *****************************************************/

	private void processAttributes(String firstCommand) throws BNFError{
		int attributeCoordinate = findAttribute(firstCommand, temporaryDataModel);
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
			requestedColumns.set(attributeCoordinate, RequestedCell.TRUE);
			isItCommaSeparated(DomainType.ATTRIBUTENAME, "FROM");
			collectAttributes();
		}
		//throw error if it's neither
		else{
			throw new NoAttributeFound(firstCommand, temporaryPathModel.getFilename(), null);
		}
	}

	private void collectAttributes() throws BNFError{
		String nextCommand = getTokenSafe(DomainType.ATTRIBUTENAME);
		int attributeCoordinate = findAttribute(nextCommand, temporaryDataModel);
		//if it's a 'FROM', leave recursive loop
		if (stringMatcher("FROM", nextCommand)) {
			return;
		}
		else if (attributeCoordinate >= 0){
			requestedColumns.set(attributeCoordinate, RequestedCell.TRUE);
			if(isItCommaSeparated(DomainType.ATTRIBUTENAME, "FROM")) {
				collectAttributes();
			}
		}
		//if it's not a value or a 'FROM', throw an error
		else{
			throw new NoAttributeFound(nextCommand, temporaryPathModel.getFilename(), null);
		}
	}

	private void requestAllColumns(){
		//iterate through the columns of our table until we find a match
		for(int i=0; i<temporaryDataModel.getColumnNumber(); i++){
			requestedColumns.set(i, RequestedCell.TRUE);
		}
	}

	protected void returnToCMD(ArrayList<RequestedCell> finalRows){
		setExitMessage(finalRows);
	}

	//this is overriding the blank exitMessage method
	public void setExitMessage(ArrayList<RequestedCell> finalRows){
		setColumnsMessage();
		exitMessage = exitMessage + "\n";
		setRowsMessage(finalRows);
	}

	private void setColumnsMessage(){
		for (int k = 0; k < temporaryDataModel.getColumnNumber(); k++) {
			if (requestedColumns.get(k).equals(RequestedCell.TRUE)) {
				if(exitMessage == null){
					exitMessage = temporaryDataModel.getColumnAttribute(k) + "\t\t";
				} else {
					exitMessage = exitMessage + temporaryDataModel.getColumnAttribute(k) + "\t\t";
				}
			}
		}
	}

	private void setRowsMessage(ArrayList<RequestedCell> finalRows){
		for (int i = 0; i < temporaryDataModel.getRowNumber(); i++) {
			if(finalRows.get(i).equals(RequestedCell.TRUE)) {
				for (int j = 0; j < temporaryDataModel.getColumnNumber(); j++) {
					if(requestedColumns.get(j).equals(RequestedCell.TRUE)) {
						exitMessage = exitMessage + temporaryDataModel.getCell(i, j) + "\t\t";
					}
				}
				//Write a new line for every new row
				exitMessage = exitMessage + "\n";
			}
		}
	}
}
