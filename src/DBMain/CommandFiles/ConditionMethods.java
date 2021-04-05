package DBMain.CommandFiles;

import DBMain.DBTokeniser.DBTokeniser;
import DBMain.ModelFiles.DBModelPath;
import DBMain.ParseExceptions.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Stack;

public abstract class ConditionMethods extends AttributeSearch{
	protected ArrayList<RequestedCell> requestedRows = new ArrayList<>();
	final ArrayList<RequestedCell> finalRows = new ArrayList<>();
	final Stack<ArrayList<RequestedCell>> rowStack = new Stack<>();
	final Stack<String> operatorStack = new Stack<>();

	/******************************************************
	 ****************** CONDITION METHODS *****************
	 *****************************************************/

	protected void executeCondition(int attributeCoordinate) throws ParseExceptions {
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

	private OperatorType returnOpType(String operator) throws InvalidCommand{
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

	private void setNumRows(int attributeCoordinate, String opCommand, String valueCommand) throws OperatorDataMismatch{
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

	private void setStringRows(int attributeCoordinate, String valueCommand) throws OperatorDataMismatch{
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
