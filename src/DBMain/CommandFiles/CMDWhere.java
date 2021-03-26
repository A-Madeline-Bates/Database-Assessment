package DBMain.CommandFiles;
import DBMain.ParseExceptions.*;
import java.util.*;

public abstract class CMDWhere extends CMDAttributeSearch {
	private ArrayList<RequestedCell> requestedRows = new ArrayList<>();
	final ArrayList<RequestedCell> finalRows = new ArrayList<>();
	final Stack<ArrayList<RequestedCell>> rowStack = new Stack<>();
	final Stack<String> operatorStack = new Stack<>();

	protected abstract void returnToCMD(ArrayList<RequestedCell> finalRows) throws ParseExceptions;

	/******************************************************
	 ****** METHOD TO END STRING OR TRIGGER 'WHERE' ******
	 *****************************************************/

	protected void processWhere(CMDWhere currentCommand) throws ParseExceptions {
		String nextCommand = getTokenSafe(DomainType.UNKNOWN);
		//if there is no WHERE condition, exit and prepare our print statement
		if(stringMatcher(";", nextCommand)) {
			//We are using a normal tokeniser.nextToken() here because we are expecting a NULL
			String extraInstruction = tokeniser.nextToken();
			if (isItNullEndTHROW(extraInstruction)) {
				//we want all rows, so set them all
				requestAllRows();
				currentCommand.returnToCMD(finalRows);
			}
		}
		//if WHERE is called, call our recursive where operation
		else if(stringMatcher("WHERE", nextCommand)){
			splitIfBrackets(currentCommand);
		}
		else{
			throw new InvalidCommand(nextCommand, "FROM [tablename]", "WHERE", ";");
		}
	}

	private void requestAllRows(){
		for(int i=0; i<temporaryDataModel.getRowNumber(); i++){
			finalRows.add(RequestedCell.TRUE);
		}
	}

	/************************************************************************
	 ** END AFTER ONE CONDITION OR CALL RECURSIVE METHOD IF BRACKETS FOUND **
	 ***********************************************************************/

	//if there are not brackets on our first 'where' condition we know there should only be one condition used and we
	//do not need to process the where clause recursively
	protected void splitIfBrackets(CMDWhere currentCommand) throws ParseExceptions{
		String nextCommand = peakTokenSafe(1, DomainType.UNKNOWN);
		int attributeCoordinate = findAttribute(nextCommand, temporaryDataModel);
		//if it's a '(', that indicates we'll be doing a recursive where operation
		if (stringMatcher("(", nextCommand)) {
			callComplexWhere(currentCommand);
		} //attribute coordinate will be set to negative if attribute doesn't exist
		else if(attributeCoordinate >= 0){
			//call getNewTokenSafe so that executeCondition is looking at the correct token
			getTokenSafe(DomainType.ATTRIBUTENAME);
			executeCondition(attributeCoordinate);
			//the first set of rows we find with WHERE we can consider all relevant
			finalRows.addAll(requestedRows);
			//because there are no brackets we can expect the where clause to end here.
			isItLineEndTHROW();
			currentCommand.returnToCMD(finalRows);
		}
		else{
			throw new InvalidCommand(nextCommand, "WHERE", "[attribute name]", "(");
		}
	}

	private void callComplexWhere(CMDWhere currentCommand) throws ParseExceptions{
		//we use stacks a lot in these operations. If they are ever used while empty, throw EmptyStackException
		try {
			recursiveWhere(currentCommand);
		} catch(EmptyStackException e){
			throw new SumError();
		}
	}

	/******************************************************
	 ************* RECURSIVE 'WHERE' OPERATOR *************
	 *****************************************************/

	private void recursiveWhere(CMDWhere currentCommand) throws ParseExceptions, EmptyStackException{
		String nextCommand = getTokenSafe(DomainType.UNKNOWN);
		if(stringMatcher("(", nextCommand)){
			openBracketOp(currentCommand);
		}
		else if(stringMatcher(")", nextCommand)){
			closeBracketOp(currentCommand);
		}
		else if(stringMatcher("AND", nextCommand) || stringMatcher("OR", nextCommand)) {
			andOrOp(currentCommand, nextCommand);
		}
		//if command equals ";" we are done- we now need to clear the stack and execute the rest of the operations
		else if(stringMatcher(";", nextCommand)){
			semicolonOp(currentCommand);
		}
		else{
			throw new InvalidCommand(nextCommand, "WHERE CLAUSE", "AND, OR", ";");
		}
	}

	private void openBracketOp(CMDWhere currentCommand) throws ParseExceptions, EmptyStackException{
		String nextCommand = peakTokenSafe(1, DomainType.UNKNOWN);
		int attributeCoordinate = findAttribute(nextCommand, temporaryDataModel);
		//we don't know yet whether this is an open bracket or part of a condition yet, so we can only use
		//this information to conclude that the last "(" WAS an open bracket
		if(stringMatcher("(", nextCommand)) {
			operatorStack.push("(");
			recursiveWhere(currentCommand);
		}
		//we have found a condition and NOT an open bracket. If there is only a single bracket it doesn't 'count'-
		// i.e it is considered part of the condition in this operation
		else if(attributeCoordinate>=0){
			conditionOp(currentCommand, attributeCoordinate);
		}
		else{
			throw new InvalidCommand(nextCommand, "WHERE (", "(", "[condition]");
		}
	}

	private void conditionOp(CMDWhere currentCommand, int attributeCoordinate) throws ParseExceptions, EmptyStackException{
		//call getNewTokenSafe() to step past attribute name
		getTokenSafe(DomainType.ATTRIBUTENAME);
		//clear requested rows
		requestedRows = new ArrayList<RequestedCell>();
		executeCondition(attributeCoordinate);
		String nextCommand = getTokenSafe(DomainType.UNKNOWN);
		if(nextCommand.equals(")")){
			//make sure this is pushing the data and not a pointer to the data :/
			rowStack.push(requestedRows);
			recursiveWhere(currentCommand);
		} else{
			throw new InvalidCommand(nextCommand, "[condition]", ")", null);
		}
	}

	private void closeBracketOp(CMDWhere currentCommand) throws ParseExceptions, EmptyStackException {
		while ((!operatorStack.isEmpty()) && (!operatorStack.peek().equals("("))) {
			String andOrOp = operatorStack.pop();
			performStackOp(andOrOp);
		} //pop the stack again to remove the open bracket
		if(operatorStack.peek().equals("(")) {
			operatorStack.pop();
		}
		recursiveWhere(currentCommand);
	}

	private void semicolonOp(CMDWhere currentCommand) throws ParseExceptions, EmptyStackException{
		String extraInstruction = tokeniser.nextToken();
		if (isItNullEndTHROW(extraInstruction)) {
			clearUpStack(currentCommand);
		}
	}

	private void andOrOp(CMDWhere currentCommand, String nextCommand) throws ParseExceptions, EmptyStackException{
		operatorStack.push(nextCommand);
		recursiveWhere(currentCommand);
	}

	private void performStackOp(String andOrOp) throws EmptyStackException{
		ArrayList<RequestedCell> rowsOne = rowStack.pop();
		ArrayList<RequestedCell> rowsTwo = rowStack.pop();
		if(stringMatcher("OR", andOrOp)){
			ArrayList<RequestedCell> rowsResult = computeOR(rowsOne, rowsTwo);
			rowStack.push(rowsResult);
		} //if andOrOp isn't "OR" it has to be "AND"
		else{
			ArrayList<RequestedCell> rowsResult = computeAND(rowsOne, rowsTwo);
			rowStack.push(rowsResult);
		}
	}

	private void clearUpStack(CMDWhere currentCommand) throws ParseExceptions, EmptyStackException{
		while(!operatorStack.isEmpty()) {
			if (operatorStack.peek().equals("(")) {
				throw new SumError();
			}
			String andOrOp = operatorStack.pop();
			performStackOp(andOrOp);
		}
		//pop what should hopefully be our final result from the stack
		finalRows.addAll(rowStack.pop());
		if(!rowStack.isEmpty()){
			throw new SumError();
		}
		currentCommand.returnToCMD(finalRows);
	}

	private ArrayList<RequestedCell> computeAND(ArrayList<RequestedCell> rowsOne, ArrayList<RequestedCell> rowsTwo){
		for(int i=0; i<temporaryDataModel.getRowNumber(); i++) {
			if((rowsOne.get(i) == RequestedCell.TRUE) && (rowsTwo.get(i) == RequestedCell.TRUE)){
				rowsOne.set(i, RequestedCell.TRUE);
			}
			else{
				rowsOne.set(i, RequestedCell.FALSE);
			}
		}
		return rowsOne;
	}

	private ArrayList<RequestedCell> computeOR(ArrayList<RequestedCell> rowsOne, ArrayList<RequestedCell> rowsTwo){
		for(int i=0; i<temporaryDataModel.getRowNumber(); i++) {
			if((rowsOne.get(i) == RequestedCell.TRUE) || (rowsTwo.get(i) == RequestedCell.TRUE)){
				rowsOne.set(i, RequestedCell.TRUE);
			}
		}
		return rowsOne;
	}

	/******************************************************
	 ****************** CONDITION METHODS *****************
	 *****************************************************/

	private void executeCondition(int attributeCoordinate) throws ParseExceptions {
		String opCommand = getTokenSafe(DomainType.OPERATOR);
		//find the type of our operator- if it's not valid, an exception will be thrown
		OperatorType opType = returnOpType(opCommand);
		splitByOpType(opType, opCommand, attributeCoordinate);
	}

	/******************************************************
	 *********** SPLIT CONDITION BY OPERATOR TYPE **********
	 *****************************************************/

	private void splitByOpType(OperatorType opType, String opCommand, int attributeCoordinate) throws ParseExceptions{
		String valueCommand = getTokenSafe(DomainType.VALUE);
		//this operation checks whether a value is valid for the operator (throwing an error if not). It then
		//directs it to the appropriate method.
		switch(opType){
			case NUMERICAL:
				isItNumTHROW(valueCommand);
				setNumRows(attributeCoordinate, opCommand, valueCommand);
				break;
			case STRING :
				isItStringTHROW(valueCommand);
				setStringRows(attributeCoordinate, valueCommand);
				break;
			default: //default means the opType is universal, so we only need to know if it's a valid value
				isItValidValue(valueCommand);
				setUniveralRows(attributeCoordinate, opCommand, valueCommand);
		}
	}

	private OperatorType returnOpType(String operator) throws ParseExceptions{
		if(isOpTypeNum(operator)){
			return OperatorType.NUMERICAL;
		} else if(isOpTypeUniversal(operator)){
			return OperatorType.UNIVERSAL;
		} else if(isOpTypeString(operator)){
			return OperatorType.STRING;
		} else{
			throw new InvalidCommand(operator, "WHERE [attributename]", "[operator]", null);
		}
	}

	private boolean isOpTypeNum(String operator){
		if(stringMatcher(">", operator) ||
				stringMatcher("<", operator) ||
				stringMatcher(">=", operator) ||
				stringMatcher("<=", operator)){
			return true;
		}
		return false;
	}

	private boolean isOpTypeUniversal(String operator){
		if(stringMatcher("==", operator)){
			return true;
		} else if(stringMatcher("!=", operator)) {
			return true;
		}
		return false;
	}

	private boolean isOpTypeString(String operator){
		if(stringMatcher("LIKE", operator)){
			return true;
		}
		return false;
	}

	/******************************************************
	 ************* NUMERICAL CONDITION METHODS ************
	 *****************************************************/

	private void setNumRows(int attributeCoordinate, String opCommand, String valueCommand) throws ParseExceptions{
		//create float version of our valueCommand (the number we are using to make our comparison)
		float comparisonValue = Float.parseFloat(valueCommand);
		for(int i=0; i<temporaryDataModel.getRowNumber(); i++){
			//create a cell for each row and set RequestedCell to false. If the cell does fit the criteria, it will be
			//set to true later in this loop.
			requestedRows.add(RequestedCell.FALSE);
			//if the operation was caught by NumberFormatException our cell value is not a valid number, and so we
			//want to jump to catch + don't want assignByOperator to consider it
			try{
				//consider every cell in our attribute's column
				float tableValue = Float.parseFloat(temporaryDataModel.getCell(i, attributeCoordinate));
				assignByOperator(i, opCommand, tableValue, comparisonValue);
			} catch(NumberFormatException n){
				throw new OperatorDataMismatch(temporaryDataModel.getCell(i, attributeCoordinate), OperatorType.NUMERICAL);
			}
		}
	}

	private void assignByOperator(int i, String opCommand, float tableValue, float comparisonValue) {
		switch(opCommand){
			case ">":
				setLessThan(i, tableValue, comparisonValue);
				break;
			case "<":
				setMoreThan(i, tableValue, comparisonValue);
				break;
			case ">=":
				setLessThanEqual(i, tableValue, comparisonValue);
				break;
			default: //opCommand doesn't equal any of the above, it has to equal "<="
				setMoreThanEqual(i, tableValue, comparisonValue);
		}
	}

	private void setLessThan(int i, float tableValue, float comparisonValue){
		if(tableValue > comparisonValue){
			requestedRows.set(i, RequestedCell.TRUE);
		}
	}

	private void setMoreThan(int i, float tableValue, float comparisonValue){
		if(tableValue < comparisonValue){
			requestedRows.set(i, RequestedCell.TRUE);
		}
	}

	private void setLessThanEqual(int i, float tableValue, float comparisonValue){
		if(tableValue >= comparisonValue){
			requestedRows.set(i, RequestedCell.TRUE);
		}
	}

	private void setMoreThanEqual(int i, float tableValue, float comparisonValue){
		if(tableValue <= comparisonValue){
			requestedRows.set(i, RequestedCell.TRUE);
		}
	}

	/******************************************************
	 ********* STRING/UNIVERSAL CONDITION METHODS *********
	 *****************************************************/

	private void setUniveralRows(int attributeCoordinate, String opCommand, String valueCommand){
		for(int i=0; i<temporaryDataModel.getRowNumber(); i++){
			requestedRows.add(RequestedCell.FALSE);
			if(stringMatcher("==", opCommand)){
				if (temporaryDataModel.getCell(i, attributeCoordinate).equals(valueCommand)) {
					requestedRows.set(i, RequestedCell.TRUE);
				}
			}
			//if opCommand doesn't equal "==" it has to equal "!="
			else {
				if (!temporaryDataModel.getCell(i, attributeCoordinate).equals(valueCommand)) {
					requestedRows.set(i, RequestedCell.TRUE);
				}
			}
		}
	}

	private void setStringRows(int attributeCoordinate, String valueCommand) throws ParseExceptions{
		for(int i=0; i<temporaryDataModel.getRowNumber(); i++){
			//if we're not looking at a string, throw an exception
			String currentValue = temporaryDataModel.getCell(i, attributeCoordinate);
			if(!isItString(currentValue)){
				throw new OperatorDataMismatch(currentValue, OperatorType.STRING);
			}
			requestedRows.add(RequestedCell.FALSE);
			if (isItSimilar(currentValue, valueCommand)) {
				requestedRows.set(i, RequestedCell.TRUE);
			}
		}
	}

	private boolean isItSimilar(String currentValue, String comparisonValue){
		//trim the quote marks from the strings
		currentValue = currentValue.substring(1, currentValue.length() - 1);
		comparisonValue = comparisonValue.substring(1, comparisonValue.length() - 1);		//we will judge two strings to be similar if they share 60% of the same characters or if the comparison value
		//is contained within our current value
		if(currentValue.toLowerCase().contains(comparisonValue.toLowerCase()) ||
				(isCountEnough(currentValue, comparisonValue))){
			return true;
		}
		else {
			return false;
		}
	}

	private boolean isCountEnough(String currentValue, String comparisonValue){
		int matchThreshold = (int) (currentValue.length() * 0.6);
		int matches = countSameChars(currentValue, comparisonValue);
		if(matches >= matchThreshold){
			return true;
		}
		return false;
	}

	private int countSameChars(String currentValue, String comparisonValue){
		int matches = 0;
		for(int i=0; i<currentValue.length(); i++){
			for(int j=0; j<comparisonValue.length(); j++){
				if(String.valueOf(currentValue.charAt(i)).equals(String.valueOf(comparisonValue.charAt(j)))){
					matches++;
					break;
				}
			}
		}
		return matches;
	}
}
