package DBMain.CommandFiles;

import DBMain.DBLoad;
import DBMain.ModelFiles.DBModelData;
import DBMain.ParseExceptions.*;
import java.util.Stack;
import java.util.ArrayList;
import java.util.List;

public abstract class CMDWhere extends CMDType {
	private ArrayList<RequestedRow> requestedRows = new ArrayList<>();
	private ArrayList<RequestedRow> finalRows = new ArrayList<>();
	private List<List<RequestedRow>> rowOutput = new ArrayList<List<RequestedRow>>();
	Stack<String> conditionsStack = new Stack<>();

	protected abstract void executeCMD(ArrayList<RequestedRow> finalRows);


	/******************************************************
	 ***************** LOAD TEMPORARY MODEL ****************
	 *****************************************************/

	protected void setTemporaryData() {
		new DBLoad(temporaryDataModel, storagePath.getDatabaseName(), temporaryPathModel.getFilename());
	}

	protected void setTemporaryPath(String fileName) {
		temporaryPathModel.setFilename(fileName);
		temporaryPathModel.setDatabaseName(storagePath.getDatabaseName());
	}

	/******************************************************
	 ****** METHOD TO END STRING OR TRIGGER 'WHERE' ******
	 *****************************************************/

	protected void processWhere(CMDWhere currentCommand) throws ParseExceptions {
		String nextCommand = getNewTokenSafe(DomainType.UNKNOWN);
		//if there is no WHERE condition, exit and prepare our print statement
		if(isThisSemicolon(nextCommand)) {
			//We are using a normal tokeniser.nextToken() here because we are expecting a NULL
			String extraInstruction = tokeniser.nextToken();
			if (isThisCommandEndTHROW(extraInstruction)) {
				//we want all rows, so set them all
				requestAllRows();
				currentCommand.executeCMD(finalRows);
			}
		}
		//if WHERE is called, call our recursive where operation
		else if(isItWhere(nextCommand)){
			splitIfBrackets(currentCommand);
		}
		else{
			throw new InvalidCommand(nextCommand, "FROM [tablename]", "WHERE", ";");
		}
	}

	protected boolean isItWhere(String nextCommand){
		if (nextCommand.equalsIgnoreCase("WHERE")) {
			return true;
		}
		return false;
	}

	protected boolean isItWhereThrow(String nextCommand, String prevCommand) throws ParseExceptions{
		if (isItWhere(nextCommand)) {
			return true;
		}
		throw new InvalidCommand(nextCommand, prevCommand, "WHERE", null) ;
	}

	protected void requestAllRows(){
		for(int i=0; i<temporaryDataModel.getRowNumber(); i++){
			finalRows.add(RequestedRow.TRUE);
		}
	}

	/************************************************************************
	 ** END AFTER ONE CONDITION OR CALL RECURSIVE METHOD IF BRACKETS FOUND **
	 ***********************************************************************/

	//if there are not brackets on our first 'where' condition we know there should only be one condition used and we
	//do not need to process the where clause recursively
	protected void splitIfBrackets(CMDWhere currentCommand) throws ParseExceptions{
		String nextCommand = peakTokenSafe(1, DomainType.UNKNOWN);
		int attributeCoordinate = doesAttributeExist(nextCommand);
		//if it's a '(', that indicates we'll be doing a recursive where operation
		if (nextCommand.equals("(")) {
			prepareRecursiveWhere();
		} //attribute coordinate will be set to negative if attribute doesn't exist
		else if(attributeCoordinate >= 0){
			//call getNewTokenSafe so that executeCondition is looking at the correct token
			getNewTokenSafe(DomainType.UNKNOWN);
			executeCondition(attributeCoordinate);
			//the first set of rows we find with WHERE we can consider all relevant
			finalRows.addAll(requestedRows);
			currentCommand.executeCMD(finalRows);
			//because there are no brackets we can expect the where clause to end here.
			isThisCommandLineEnd();
		}
		else{
			throw new InvalidCommand(nextCommand, "WHERE", "[attribute name]", "(");
		}
	}

	protected int doesAttributeExist(String nextCommand){
		//iterate through the columns of our table until we find a match
		for(int i=0; i<temporaryDataModel.getColumnNumber(); i++){
			if(temporaryDataModel.getColumnData().get(i).equalsIgnoreCase(nextCommand)){
				return i;
			}
		}
		return -1;
	}

	/******************************************************
	 ***************** REORDER BY BRACKETS ****************
	 *****************************************************/

	private void prepareRecursiveWhere() throws ParseExceptions{
		String nextCommand = getNewTokenSafe(DomainType.UNKNOWN);
		//maybe call static int noOfConditions, and before exiting is if noOfConditions==0, throw error.
		if(nextCommand.equals("(")){
			nextCommand = peakTokenSafe(1, DomainType.UNKNOWN);
			int attributeCoordinate = doesAttributeExist(nextCommand);
			//we don't know yet whether this is an open bracket or part of a condition yet, so we can only use
			//this information to conclude that the last "(" WAS an open bracket
			if(nextCommand.equals("(")) {
				conditionsStack.push(nextCommand);
			}
			//we have found a condition and NOT an open bracket.
			//if there is only a single bracket it doesn't 'count'- i.e it is considered part of the condition
			//in this operation
			if(attributeCoordinate>=0){
				executeCondition(attributeCoordinate);
				nextCommand = getNewTokenSafe(DomainType.UNKNOWN);
				if(nextCommand.equals(")")){
					//add the result of our condition to our output.
					List<RequestedRow> rowData = new ArrayList<>();
					rowOutput.add(rowData);
					rowOutput.get(rowOutput.size() - 1).addAll(requestedRows);
				}
			}
			else{
				//THROW - these are the only things that should follow a bracket
			}
		}
		else if(nextCommand.equals(")")){
			while (!conditionsStack.isEmpty() && conditionsStack.peek() != "(") {
				//this where we do operations on our rowOutput!!
				//stack.pop() x2 for each operator and use the operations to alter condition result
			}
			//pop the stack again to remove the open bracket
			conditionsStack.pop();
		}
		else if(nextCommand.equalsIgnoreCase("AND") || nextCommand.equalsIgnoreCase("OR")){
			conditionsStack.push(nextCommand);
		}
	}

	/******************************************************
	 ************* RECURSIVE 'WHERE' OPERATOR *************
	 *****************************************************/

	protected void recursiveWhereClause(CMDWhere currentCommand) throws ParseExceptions{
		String nextCommand = getNewTokenSafe(DomainType.UNKNOWN);
		//if we find a semicolon, exit and prepare our print statement
		if(isThisSemicolon(nextCommand)) {
			//We are using a normal tokeniser.nextToken() here because we are expecting a NULL
			String extraInstruction = tokeniser.nextToken();
			if (isThisCommandEndTHROW(extraInstruction)) {
				currentCommand.executeCMD(finalRows);
			}
		} else if(nextCommand.equalsIgnoreCase("AND")){
			//clear requestedRows
			requestedRows = new ArrayList<RequestedRow>();
			nextCommand = getNewTokenSafe(DomainType.ATTRIBUTENAME);
			int attributeCoordinate = findSingleAttributeTHROW(nextCommand);
			executeCondition(attributeCoordinate);
			computeAND();
			recursiveWhereClause(currentCommand);
		} else if(nextCommand.equalsIgnoreCase("OR")){
			//clear requestedRows
			requestedRows = new ArrayList<RequestedRow>();
			nextCommand = getNewTokenSafe(DomainType.ATTRIBUTENAME);
			int attributeCoordinate = findSingleAttributeTHROW(nextCommand);
			executeCondition(attributeCoordinate);
			computeOR();
			recursiveWhereClause(currentCommand);
		} else{
			throw new InvalidCommand(nextCommand, "[WHERE CLAUSE]", "AND, OR", ";");
		}
	}

	//computeAND decides which cells from our most recent requestedRows result should make it into our finalRows
	protected void computeAND(){
		for(int i=0; i<temporaryDataModel.getRowNumber(); i++) {
			if((requestedRows.get(i) == RequestedRow.TRUE) && (finalRows.get(i) == RequestedRow.TRUE)){
				finalRows.set(i, RequestedRow.TRUE);
			}
			else{
				finalRows.set(i, RequestedRow.FALSE);
			}
		}
	}

	//computeOR decides which cells from our most recent requestedRows result should make it into our finalRows
	protected void computeOR(){
		for(int i=0; i<temporaryDataModel.getRowNumber(); i++) {
			if((requestedRows.get(i) == RequestedRow.TRUE) || (finalRows.get(i) == RequestedRow.TRUE)){
				finalRows.set(i, RequestedRow.TRUE);
			}
		}
	}

	/******************************************************
	 ****************** CONDITION METHODS *****************
	 *****************************************************/

	protected void executeCondition(int attributeCoordinate) throws ParseExceptions {
		String opCommand = getNewTokenSafe(DomainType.OPERATOR);
		//find the type of our operator- if it's not valid, an exception will be thrown
		OperatorType opType = returnOperatorType(opCommand);
		splitByOpType(opType, opCommand, attributeCoordinate);
	}

	protected int findSingleAttributeTHROW(String nextCommand) throws ParseExceptions{
		//iterate through the columns of our table until we find a match
		for(int i=0; i<temporaryDataModel.getColumnNumber(); i++){
			if(temporaryDataModel.getColumnData().get(i).equalsIgnoreCase(nextCommand)){
				return i;
			}
		}
		throw new DoesNotExistAttribute(nextCommand, temporaryPathModel.getFilename());
	}

	/******************************************************
	 *********** SPLIT CONDITION BY OPERATOR TYPE **********
	 *****************************************************/

	protected void splitByOpType(OperatorType opType, String opCommand, int attributeCoordinate) throws ParseExceptions{
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

	protected OperatorType returnOperatorType(String operator) throws ParseExceptions{
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

	protected void searchAttributeNum(int attributeCoordinate, String opCommand, String valueCommand){
		//create float version of our valueCommand (the number we are using to make our comparison)
		float comparisonValue = Float.parseFloat(valueCommand);
		for(int i=0; i<temporaryDataModel.getRowNumber(); i++){
			//create a cell for each row and set RequestedRow to false. If the cell does fit the criteria, it will be
			//set to true later in this loop.
			requestedRows.add(RequestedRow.FALSE);
			//if the operation was caught by NumberFormatException our cell value is not a valid number, and so we
			//want to jump to catch + don't want assignByOperator to consider it
			try{
				//consider every cell in our attribute's column
				float tableValue = Float.parseFloat(temporaryDataModel.getCell(i, attributeCoordinate));
				assignByOperator(i, opCommand, tableValue, comparisonValue);
			} catch(NumberFormatException n){}
		}
	}

	protected void assignByOperator(int i, String opCommand, float tableValue, float comparisonValue) {
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

	protected void searchAttributeUniversal(int attributeCoordinate, String opCommand, String valueCommand){
		for(int i=0; i<temporaryDataModel.getRowNumber(); i++){
			requestedRows.add(RequestedRow.FALSE);
			if(opCommand.equals("==") || opCommand.equalsIgnoreCase("LIKE")){
				if (temporaryDataModel.getCell(i, attributeCoordinate).equals(valueCommand)) {
					requestedRows.set(i, RequestedRow.TRUE);
				}
			}
			//if opCommand doesn't equal "==" it has to equal "!="
			else {
				if (!temporaryDataModel.getCell(i, attributeCoordinate).equals(valueCommand)) {
					requestedRows.set(i, RequestedRow.TRUE);
				}
			}
		}
	}


}
