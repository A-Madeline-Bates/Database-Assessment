package DBMain.CommandFiles;
import DBMain.*;
import DBMain.ModelFiles.*;
import DBMain.ParseExceptions.*;

import java.util.ArrayList;

public class CMDSelect extends CMDType {
	private String tableName;
	private ArrayList<Integer> requestedColumns = new ArrayList<>();
	private ArrayList<RequestedRow> requestedRows = new ArrayList<>();
	private ArrayList<RequestedRow> finalRows = new ArrayList<>();
	DBModelData temporaryModel;

	public void transformModel() throws ParseExceptions {
		String firstCommand = getNewTokenSafe(DomainType.ATTRIBUTENAME);
		if(canWeReadTable()){
			loadTemporaryModel();
			if(doesWildAttributeExist(firstCommand)){
				System.out.println("It exists!" + requestedColumns);
				//calling getNewTokenSafe to move past the tablename, which we've already processed and so
				//don't need here
				getNewTokenSafe(DomainType.ATTRIBUTENAME);
				processEndOfString();
			}
		}
	}

	/******************************************************
	 *************  METHOD TO IDENTIFY FILE  **************
	 *****************************************************/

	private boolean canWeReadTable() throws ParseExceptions {
		//we need to know tablename before we can identify if our attribute is valid, so we start by 'peaking'
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
					tableName = peakTwo;
					return true;
				}
			}
		}
	}

	/******************************************************
	 ***************** LOAD TEMPORARY MODEL ****************
	 *****************************************************/

	private void loadTemporaryModel() {
		//create temporaryModel so that we can see if there are any attribute matches with our command
		this.temporaryModel = new DBModelData();
		new DBLoad(temporaryModel, pathModel.getDatabaseName(), tableName);
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
			throw new DoesNotExistAttribute(firstCommand, tableName);
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
			throw new DoesNotExistAttribute(nextCommand, tableName);
		}
	}

	private boolean doesAttributeExist(String nextCommand){
		//iterate through the columns of our table until we find a match
		for(int i=0; i<temporaryModel.getColumnNumber(); i++){
			if(temporaryModel.getColumnData().get(i).equalsIgnoreCase(nextCommand)){
				//if we get an attribute match, save the column coordinate where the match occurred
				requestedColumns.add(i);
				return true;
			}
		}
		return false;
	}

	private void requestAllColumns(){
		//iterate through the columns of our table until we find a match
		for(int i=0; i<temporaryModel.getColumnNumber(); i++){
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

	/******************************************************
	 ****** METHOD TO END STRING OR TRIGGER 'WHERE' ******
	 *****************************************************/

	private void processEndOfString() throws ParseExceptions{
		String nextCommand = getNewTokenSafe(DomainType.UNKNOWN);
		//if there is no WHERE condition, exit and prepare our print statement
		if(isThisSemicolon(nextCommand)) {
			//We are using a normal tokeniser.nextToken() here because we are expecting a NULL
			String extraInstruction = tokeniser.nextToken();
			if (isThisCommandEndTHROW(extraInstruction)) {
				//we want all rows, so set them all
				requestAllRows();
				createPrintStatement();
			}
		}
		//if WHERE is called, call our recursive where operation
		else if(isItWhere(nextCommand)){
			executeCondition();
			//the first set of rows we find with WHERE we can consider all relevant
			finalRows.addAll(requestedRows);
			recursiveWhereClause();
		}
		else{
			throw new InvalidCommand(nextCommand, "FROM [tablename]", "WHERE", ";");
		}
	}

	private boolean isItWhere(String nextCommand){
		if (nextCommand.equalsIgnoreCase("WHERE")) {
			return true;
		}
		return false;
	}

	private void requestAllRows(){
		for(int i=0; i<temporaryModel.getRowNumber(); i++){
			requestedRows.add(RequestedRow.TRUE);
		}
	}

	/******************************************************
	 ************* RECURSIVE 'WHERE' OPERATOR *************
	 *****************************************************/

	private void recursiveWhereClause() throws ParseExceptions{
		String nextCommand = getNewTokenSafe(DomainType.UNKNOWN);
		//if we find a semicolon, exit and prepare our print statement
		if(isThisSemicolon(nextCommand)) {
			//We are using a normal tokeniser.nextToken() here because we are expecting a NULL
			String extraInstruction = tokeniser.nextToken();
			if (isThisCommandEndTHROW(extraInstruction)) {
				createPrintStatement();
			}
//		} else if(nextCommand.equalsIgnoreCase("AND")){
//			//work out some AND logic
//			recursiveWhereClause();
//		} else if(nextCommand.equalsIgnoreCase("OR")){
//			//work out some OR logic
//			recursiveWhereClause();
		} else{
			throw new InvalidCommand(nextCommand, "[WHERE CLAUSE]", "AND/OR", ";");
		}
	}

	/******************************************************
	 ****************** CONDITION METHODS *****************
	 *****************************************************/

	private void executeCondition() throws ParseExceptions {
		String attributeCommand = getNewTokenSafe(DomainType.ATTRIBUTENAME);
		//find attribute we're processing- if it doesn't exist, an exception will be thrown
		int attributeCoordinate = findSingleAttributeTHROW(attributeCommand);
		String opCommand = getNewTokenSafe(DomainType.OPERATOR);
		//find the type of our operator- if it's not valid, an exception will be thrown
		OperatorType opType = returnOperatorType(opCommand);
		splitByOpType(opType, opCommand, attributeCoordinate);
	}

	private int findSingleAttributeTHROW(String nextCommand) throws ParseExceptions{
		//iterate through the columns of our table until we find a match
		for(int i=0; i<temporaryModel.getColumnNumber(); i++){
			if(temporaryModel.getColumnData().get(i).equalsIgnoreCase(nextCommand)){
				return i;
			}
		}
		throw new DoesNotExistAttribute(nextCommand, tableName);
	}

	/******************************************************
	 *********** SPLIT CONDITION BY OPERATOR TYPE **********
	 *****************************************************/

	private void splitByOpType(OperatorType opType, String opCommand, int attributeCoordinate) throws ParseExceptions{
		String valueCommand = getNewTokenSafe(DomainType.VALUE);
		//this operation checks whether a value is valid for the operator (throwing an error if not). It then
		//directs it to the appropriate method.
		switch(opType){
			case NUMERICAL:
				isItNumLiteralTHROW(valueCommand);
				searchAttributeNum(attributeCoordinate, opCommand, valueCommand);
				break;
			case STRING : //we are currently treating '==' and LIKE (i.e STRING and UNIVERSAL) as the same.
				isItStringLiteralTHROW(valueCommand);
				searchAttributeUniversal(attributeCoordinate, opCommand, valueCommand);
				break;
			default: //default means the opType is universal, so we only need to know if it's a valid value
				isItValidValue(valueCommand);
				searchAttributeUniversal(attributeCoordinate, opCommand, valueCommand);
		}
	}

	private OperatorType returnOperatorType(String operator) throws ParseExceptions{
		if(operator.equals("==")){
			return OperatorType.UNIVERSAL;
		} else if(operator.equals(">")){
			return OperatorType.NUMERICAL;
		} else if(operator.equals("<")){
			return OperatorType.NUMERICAL;
		} else if(operator.equals(">=")){
			return OperatorType.NUMERICAL;
		} else if(operator.equals("<=")){
			return OperatorType.NUMERICAL;
		} else if(operator.equals("!=")){
			return OperatorType.UNIVERSAL;
		} else if(operator.equalsIgnoreCase("LIKE")){
			return OperatorType.STRING;
		} else{
			throw new InvalidCommand(operator, "WHERE [attributename]", "[operator]", null);
		}
	}

	/******************************************************
	 ************* NUMERICAL CONDITION METHODS ************
	 *****************************************************/

	private void searchAttributeNum(int attributeCoordinate, String opCommand, String valueCommand){
		//create float version of our valueCommand (the number we are using to make our comparison)
		float comparisonValue = Float.parseFloat(valueCommand);
		for(int i=0; i<temporaryModel.getRowNumber(); i++){
			//create a cell for each row and set RequestedRow to false. If the cell does fit the criteria, it will be
			//set to true later in this loop.
			requestedRows.add(RequestedRow.FALSE);
			//if the operation was caught by NumberFormatException our cell value is not a valid number, and so we
			//want to jump to catch + don't want assignByOperator to consider it
			try{
				//consider every cell in our attribute's column
				float tableValue = Float.parseFloat(temporaryModel.getCell(i, attributeCoordinate));
				assignByOperator(i, opCommand, tableValue, comparisonValue);
			} catch(NumberFormatException n){}
		}
	}

	private void assignByOperator(int i, String opCommand, float tableValue, float comparisonValue) {
		switch(opCommand){
			case ">":
				if(tableValue > comparisonValue){
					requestedRows.set(i, RequestedRow.TRUE);
				} break;
			case "<":
				if(tableValue < comparisonValue){
					requestedRows.set(i, RequestedRow.TRUE);
				} break;
			case ">=":
				if(tableValue >= comparisonValue){
					requestedRows.set(i, RequestedRow.TRUE);
				} break;
			default: //opCommand doesn't equal any of the above, it has to equal "<="
				if(tableValue <= comparisonValue){
					requestedRows.set(i, RequestedRow.TRUE);
				}
		}
	}

	/******************************************************
	 ********* STRING/UNIVERSAL CONDITION METHODS *********
	 *****************************************************/

	private void searchAttributeUniversal(int attributeCoordinate, String opCommand, String valueCommand){
		for(int i=0; i<temporaryModel.getRowNumber(); i++){
			requestedRows.add(RequestedRow.FALSE);
			if(opCommand.equals("==") || opCommand.equalsIgnoreCase("LIKE")){
				if (temporaryModel.getCell(i, attributeCoordinate).equals(valueCommand)) {
					requestedRows.set(i, RequestedRow.TRUE);
				}
			}
			//if opCommand doesn't equal "==" it has to equal "!="
			else {
				if (!temporaryModel.getCell(i, attributeCoordinate).equals(valueCommand)) {
					requestedRows.set(i, RequestedRow.TRUE);
				}
			}
		}
	}

	private void createPrintStatement(){
		System.out.println("COLUMN:" + requestedColumns + "WHERE ROW:" + requestedRows);
	}

	public String query(DBServer server){
		return "Select";
	}
}
