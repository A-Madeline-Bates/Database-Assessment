package DBMain.CommandFiles;
import DBMain.*;
import DBMain.ModelFiles.*;
import DBMain.ParseExceptions.*;

import java.util.ArrayList;

public class CMDSelect extends CMDType {
	private String tableName;
	private ArrayList<Integer> requestedColumns = new ArrayList<>();
	private ArrayList<Integer> requestedRows = new ArrayList<>();

	public void transformModel() throws ParseExceptions {
		//<Select>         ::=  SELECT <WildAttribList> FROM <TableName> |
		//						SELECT <WildAttribList> FROM <TableName> WHERE <Condition>
		String firstCommand = getNewTokenSafe(DomainType.ATTRIBUTENAME);
		if(canWeReadTable()){
			if(doesWildAttributeExist(firstCommand)){
				System.out.println("It exists!" + requestedColumns);
				//calling getNewTokenSafe to move past the tablename, which we've already processed and so
				//don't need here
				getNewTokenSafe(DomainType.ATTRIBUTENAME);
				processEndOfString();
			}
		}
	}

	private boolean canWeReadTable() throws ParseExceptions {
		//we need to know tablename before we can identify if our attribute is valid, so we start by 'peaking'
		//forwards at it.
		for(int i=1; ; i++) {
			//we search the command line for FROM- if we hit the end of the line without finding it,
			//peakTokenSafe will throw an error
			String peakOne = peakTokenSafe(i, DomainType.FROM);
			System.out.println(peakOne);
			if (isItFrom(peakOne)) {
				System.out.println("*" + peakOne);
				//if we find FROM, check if the next command is a valid table name. If it isn't,
				//doesTableExist will throw an error
				String peakTwo = peakTokenSafe(i+1, DomainType.TABLENAME);
				System.out.println(peakTwo);
				if (doesTableExist(peakTwo)) {
					tableName = peakTwo;
					return true;
				}
			}
		}
	}

	private boolean doesWildAttributeExist(String firstCommand) throws ParseExceptions{
		if(isItAsterisk(firstCommand)){
			String nextCommand = getNewTokenSafe(DomainType.ATTRIBUTENAME);
			//check that it's an Asterisk followed immediately by a FROM
			if (isItFromTHROW(nextCommand, "SELECT *")) {
				requestAllColumns();
			}
			return true;
		}
		else if(doesAttributeExist(firstCommand)){
			isItCommaSeparated(DomainType.ATTRIBUTENAME, "FROM");
			doesAttrExistRecursive();
			return true;
		}
		else{
			throw new DoesNotExistAttribute(firstCommand, tableName);
		}
	}

	private void doesAttrExistRecursive() throws ParseExceptions{
		String nextCommand = getNewTokenSafe(DomainType.ATTRIBUTENAME);
		//if it's a 'FROM', leave recursive loop and update table
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
		//create temporaryModel so that we can see if there are any attribute matches with our command
		DBModelData temporaryModel = new DBModelData();
		new DBLoad(temporaryModel, pathModel.getDatabaseName(), tableName);
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

	//see if there is a more efficient way of doing this! This is also not DRY.
	private void requestAllColumns(){
		//create temporaryModel so that we can see how many columns we have
		DBModelData temporaryModel = new DBModelData();
		new DBLoad(temporaryModel, pathModel.getDatabaseName(), tableName);
		//iterate through the columns of our table until we find a match
		for(int i=0; i<temporaryModel.getColumnNumber(); i++){
			requestedColumns.add(i);
		}
	}

	//MERGE THESE METHODS
	private void requestAllRows(){
		//create temporaryModel so that we can see how many columns we have
		DBModelData temporaryModel = new DBModelData();
		new DBLoad(temporaryModel, pathModel.getDatabaseName(), tableName);
		//iterate through the columns of our table until we find a match
		for(int i=0; i<temporaryModel.getRowNumber(); i++){
			requestedRows.add(i);
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

	private boolean isItWhere(String nextCommand){
		if (nextCommand.equalsIgnoreCase("WHERE")) {
			return true;
		}
		return false;
	}

	private void processEndOfString() throws ParseExceptions{
		String nextCommand = getNewTokenSafe(DomainType.UNKNOWN);
		if(isThisSemicolon(nextCommand)) {
			//We are using a normal tokeniser.nextToken() here because we are expecting a NULL
			String extraInstruction = tokeniser.nextToken();
			if (isThisCommandEndTHROW(extraInstruction)) {
				//we want all rows, so set them all
				requestAllRows();
				//if there is no WHERE condition, we can end and create our print statement
				createPrintStatement();
			}
		}
		else if(isItWhere(nextCommand)){
			executeCondition();
			recursiveWhereClause();
		}
		else{
			throw new InvalidCommand(nextCommand, "FROM [tablename]", "WHERE", ";");
		}
	}

	private void recursiveWhereClause() throws ParseExceptions{
		String nextCommand = getNewTokenSafe(DomainType.UNKNOWN);
		if(isThisSemicolon(nextCommand)) {
			//We are using a normal tokeniser.nextToken() here because we are expecting a NULL
			String extraInstruction = tokeniser.nextToken();
			if (isThisCommandEndTHROW(extraInstruction)) {
				createPrintStatement();
			}
		} else if(nextCommand.equalsIgnoreCase("AND")){
			//work out some AND logic
			recursiveWhereClause();
		} else if(nextCommand.equalsIgnoreCase("OR")){
			//work out some OR logic
			recursiveWhereClause();
		} else{
			throw new InvalidCommand(nextCommand, "[WHERE CLAUSE]", "AND/OR", ";");
		}
	}

	private void executeCondition() throws ParseExceptions{
		String nextCommand = getNewTokenSafe(DomainType.ATTRIBUTENAME);
		//find attribute we're processing- if it doesn't exist, an exception will be thrown
		int attributeCoordinate = findAttributeTHROW(nextCommand);
		String opCommand = getNewTokenSafe(DomainType.OPERATOR);
		//find opType- if it's not valid, an exception will be thrown
		OperatorType opType = returnOperatorType(opCommand);
		String valueCommand = getNewTokenSafe(DomainType.VALUE);
		//if our command doesn't match our operator, there is no point trying to search
		if(doesValueMatchOperator(opType, nextCommand)){
			if(opType.equals(OperatorType.NUMERICAL)) {
				searchAttributeNum(attributeCoordinate, opCommand, valueCommand);
			}
			//we are currently treating '==' and LIKE (i.e STRING and UNIVERSAL) as the same,
			// so we're sending them to the same place
			else{
				searchAttributeString(attributeCoordinate, opCommand, valueCommand);
			}
		}
	}

	//WE HAVE TO MERGE THIS WITH doesAttributeExist!!
	private int findAttributeTHROW(String nextCommand) throws ParseExceptions{
		//create temporaryModel so that we can see if there are any attribute matches with our command
		DBModelData temporaryModel = new DBModelData();
		new DBLoad(temporaryModel, pathModel.getDatabaseName(), tableName);
		//iterate through the columns of our table until we find a match
		for(int i=0; i<temporaryModel.getColumnNumber(); i++){
			if(temporaryModel.getColumnData().get(i).equalsIgnoreCase(nextCommand)){
				return i;
			}
		}
		throw new DoesNotExistAttribute(nextCommand, tableName);
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

	private boolean doesValueMatchOperator(OperatorType opType, String nextCommand) throws ParseExceptions{
		switch(opType){
			case NUMERICAL:
				return isItNumLiteralTHROW(nextCommand);
			case STRING :
				return isItStringLiteralTHROW(nextCommand);
			//default means the opType is universal, so we only need to know if it's a valid value
			default: return isItValidValue(nextCommand);
		}
	}

	//definitely create a temp model which is global to the class so we don't have to keep doing this
	private void searchAttributeNum(int attributeCoordinate, String opCommand, String valueCommand){
		//create temporaryModel so that we can search it
		DBModelData temporaryModel = new DBModelData();
		new DBLoad(temporaryModel, pathModel.getDatabaseName(), tableName);
		float comparisonValue = Float.parseFloat(valueCommand);
		for(int i=0; i<dataModel.getRowNumber(); i++){
			//if the operation was caught by NumberFormatException our cell value is not a valid number, so we want it
			//to pass by assignByOperator
			try{
				float tableValue = Float.parseFloat(dataModel.getCell(i, attributeCoordinate));
				assignByOperator(i, opCommand, tableValue, comparisonValue);
			} catch(NumberFormatException n){}
		}
	}

	private void assignByOperator(int i, String opCommand, float tableValue, float comparisonValue) {
		if (opCommand.equals(">")) {
			if(tableValue > comparisonValue){
				requestedRows.add(i);
			}
		} else if (opCommand.equals("<")) {
			if(tableValue < comparisonValue){
				requestedRows.add(i);
			}
		} else if (opCommand.equals(">=")) {
			if(tableValue >= comparisonValue){
				requestedRows.add(i);
			}
		} //opCommand doesn't equal any of the above, it has to equal "<="
		else {
			if(tableValue <= comparisonValue){
				requestedRows.add(i);
			}
		}
	}

	//this is currently dealing with both == and LIKE, which isn't correct
	//if there's a match with one of the cells in our array in the relevant column, then save that row coordinate
	private void searchAttributeString(int attributeCoordinate, String opCommand, String valueCommand){
		//create temporaryModel so that we can search it
		DBModelData temporaryModel = new DBModelData();
		new DBLoad(temporaryModel, pathModel.getDatabaseName(), tableName);
		for(int i=0; i<dataModel.getRowNumber(); i++){
			if(opCommand.equals("==") || opCommand.equalsIgnoreCase("LIKE")){
				if (dataModel.getCell(i, attributeCoordinate).equals(valueCommand)) {
					requestedRows.add(i);
				}
			}
			//if opCommand doesn't equal "==" it has to equal "!="
			else {
				if (!dataModel.getCell(i, attributeCoordinate).equals(valueCommand)) {
					requestedRows.add(i);
				}
			}
		}
	}

	public String query(DBServer server){
		return "Select";
	}
}
