package DBMain.CommandFiles;

import DBMain.DBLoad;
import DBMain.ModelFiles.DBModelData;
import DBMain.ParseExceptions.*;
import java.util.*;

public abstract class CMDWhere extends CMDType {
	private ArrayList<RequestedRow> requestedRows = new ArrayList<>();
	private ArrayList<RequestedRow> finalRows = new ArrayList<>();
	private Stack<ArrayList<RequestedRow>> rowStack = new Stack<>();
	private Stack<String> operatorStack = new Stack<>();

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
			//we use stacks a lot in these operations. If they are ever used while empty, throw EmptyStackException
			try {
				recursiveWhere(currentCommand);
			} catch(EmptyStackException e){
				System.out.println("stack is empty");
				throw new SumError();
			}
		} //attribute coordinate will be set to negative if attribute doesn't exist
		else if(attributeCoordinate >= 0){
			//call getNewTokenSafe so that executeCondition is looking at the correct token
			getNewTokenSafe(DomainType.UNKNOWN);
			executeCondition(attributeCoordinate);
			//the first set of rows we find with WHERE we can consider all relevant
			finalRows.addAll(requestedRows);
			//because there are no brackets we can expect the where clause to end here.
			isThisCommandLineEnd();
			currentCommand.executeCMD(finalRows);
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
	 ************* RECURSIVE 'WHERE' OPERATOR *************
	 *****************************************************/

	private void recursiveWhere(CMDWhere currentCommand) throws ParseExceptions, EmptyStackException{
		String nextCommand = getNewTokenSafe(DomainType.UNKNOWN);
		if(nextCommand.equals("(")){
			openBracketOp(currentCommand);
		}
		else if(nextCommand.equals(")")){
			while ((!operatorStack.isEmpty()) && (!operatorStack.peek().equals("("))) {
				String andOrOp = operatorStack.pop();
				performStackOperation(andOrOp);
			} //pop the stack again to remove the open bracket
			if(operatorStack.peek().equals("(")) {
				operatorStack.pop();
			}
			recursiveWhere(currentCommand);
		}
		else if(nextCommand.equalsIgnoreCase("AND") || nextCommand.equalsIgnoreCase("OR")) {
			operatorStack.push(nextCommand);
			recursiveWhere(currentCommand);
		} //if command equals ";" we are done- we now need to clear the stack and execute the rest of the operations
		else if(nextCommand.equals(";")){
			String extraInstruction = tokeniser.nextToken();
			if (isThisCommandEndTHROW(extraInstruction)) {
				clearUpStack(currentCommand);
			}
		}
		else{
			throw new InvalidCommand(nextCommand, "WHERE CLAUSE", "AND, OR", ";");
		}
	}

	private void openBracketOp(CMDWhere currentCommand) throws ParseExceptions, EmptyStackException{
		String nextCommand = peakTokenSafe(1, DomainType.UNKNOWN);
		int attributeCoordinate = doesAttributeExist(nextCommand);
		//we don't know yet whether this is an open bracket or part of a condition yet, so we can only use
		//this information to conclude that the last "(" WAS an open bracket
		if(nextCommand.equals("(")) {
			operatorStack.push(nextCommand);
			recursiveWhere(currentCommand);
		}
		//we have found a condition and NOT an open bracket. If there is only a single bracket it doesn't 'count'-
		// i.e it is considered part of the condition in this operation
		else if(attributeCoordinate>=0){
			//call getNewTokenSafe() to step past attribute name
			getNewTokenSafe(DomainType.ATTRIBUTENAME);
			//clear requested rows
			requestedRows = new ArrayList<RequestedRow>();
			executeCondition(attributeCoordinate);
			nextCommand = getNewTokenSafe(DomainType.UNKNOWN);
			if(nextCommand.equals(")")){
				//make sure this is pushing the data and not a pointer to the data :/
				rowStack.push(requestedRows);
				recursiveWhere(currentCommand);
			} else{
				throw new InvalidCommand(nextCommand, "[condition]", ")", null);
			}
		}
		else{
			throw new InvalidCommand(nextCommand, "WHERE (", "(", "[condition]");
		}
	}

	private void performStackOperation(String andOrOp) throws EmptyStackException{
		ArrayList<RequestedRow> rowsOne = rowStack.pop();
		ArrayList<RequestedRow> rowsTwo = rowStack.pop();
		if(andOrOp.equalsIgnoreCase("OR")){
			ArrayList<RequestedRow> rowsResult = computeOR(rowsOne, rowsTwo);
			rowStack.push(rowsResult);
		} //if andOrOp isn't "OR" it has to be "AND"
		else{
			ArrayList<RequestedRow> rowsResult = computeAND(rowsOne, rowsTwo);
			rowStack.push(rowsResult);
		}
	}

	private void clearUpStack(CMDWhere currentCommand) throws ParseExceptions, EmptyStackException{
		while(!operatorStack.isEmpty()) {
			if (operatorStack.peek().equals("(")) {
				System.out.println("extra (");
				throw new SumError();
			}
			String andOrOp = operatorStack.pop();
			performStackOperation(andOrOp);
		}
		//pop what should hopefully be our final result from the stack
		finalRows.addAll(rowStack.pop());
		if(!rowStack.isEmpty()){
			System.out.println("stack isn't empty");
			throw new SumError();
		}
		currentCommand.executeCMD(finalRows);
	}

	protected ArrayList<RequestedRow> computeAND(ArrayList<RequestedRow> rowsOne, ArrayList<RequestedRow> rowsTwo){
		for(int i=0; i<temporaryDataModel.getRowNumber(); i++) {
			if((rowsOne.get(i) == RequestedRow.TRUE) && (rowsTwo.get(i) == RequestedRow.TRUE)){
				rowsOne.set(i, RequestedRow.TRUE);
			}
			else{
				rowsOne.set(i, RequestedRow.FALSE);
			}
		}
		return rowsOne;
	}

	protected ArrayList<RequestedRow> computeOR(ArrayList<RequestedRow> rowsOne, ArrayList<RequestedRow> rowsTwo){
		for(int i=0; i<temporaryDataModel.getRowNumber(); i++) {
			if((rowsOne.get(i) == RequestedRow.TRUE) || (rowsTwo.get(i) == RequestedRow.TRUE)){
				rowsOne.set(i, RequestedRow.TRUE);
			}
		}
		return rowsOne;
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
