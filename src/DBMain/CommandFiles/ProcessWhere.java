package DBMain.CommandFiles;
import DBMain.ParseExceptions.*;
import java.util.*;

public abstract class ProcessWhere extends ConditionMethods {

	protected abstract void returnToCMD(ArrayList<RequestedCell> finalRows) throws ParseExceptions;

	/******************************************************
	 ****** METHOD TO END STRING OR TRIGGER 'WHERE' ******
	 *****************************************************/

	protected void processWhere(ProcessWhere currentCommand) throws ParseExceptions {
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
	protected void splitIfBrackets(ProcessWhere currentCommand) throws ParseExceptions{
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

	private void callComplexWhere(ProcessWhere currentCommand) throws ParseExceptions{
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

	private void recursiveWhere(ProcessWhere currentCommand) throws ParseExceptions, EmptyStackException{
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

	private void openBracketOp(ProcessWhere currentCommand) throws ParseExceptions, EmptyStackException{
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

	private void conditionOp(ProcessWhere currentCommand, int attributeCoordinate) throws ParseExceptions, EmptyStackException{
		//call getNewTokenSafe() to step past attribute name
		getTokenSafe(DomainType.ATTRIBUTENAME);
		//clear requested rows
		requestedRows = new ArrayList<RequestedCell>();
		executeCondition(attributeCoordinate);
		String nextCommand = getTokenSafe(DomainType.UNKNOWN);
		if(nextCommand.equals(")")){
			rowStack.push(requestedRows);
			recursiveWhere(currentCommand);
		} else{
			throw new InvalidCommand(nextCommand, "[condition]", ")", null);
		}
	}

	private void closeBracketOp(ProcessWhere currentCommand) throws ParseExceptions, EmptyStackException {
		while ((!operatorStack.isEmpty()) && (!operatorStack.peek().equals("("))) {
			String andOrOp = operatorStack.pop();
			performStackOp(andOrOp);
		} //pop the stack again to remove the open bracket
		if(operatorStack.peek().equals("(")) {
			operatorStack.pop();
		}
		recursiveWhere(currentCommand);
	}

	private void semicolonOp(ProcessWhere currentCommand) throws ParseExceptions, EmptyStackException{
		String extraInstruction = tokeniser.nextToken();
		if (isItNullEndTHROW(extraInstruction)) {
			clearUpStack(currentCommand);
		}
	}

	private void andOrOp(ProcessWhere currentCommand, String nextCommand) throws ParseExceptions, EmptyStackException{
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

	private void clearUpStack(ProcessWhere currentCommand) throws ParseExceptions, EmptyStackException{
		while(!operatorStack.isEmpty()) {
			if (operatorStack.peek().equals("(")) {
				throw new SumError();
			}
			String andOrOp = operatorStack.pop();
			performStackOp(andOrOp);
		}
		//pop what should be our final result from the stack
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
}
