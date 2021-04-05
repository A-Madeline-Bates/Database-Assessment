package DBMain.ParseExceptions;

import DBMain.DBEnums.OperatorType;

public class OperatorMismatch extends TableUseError {
	final OperatorType opType;
	final String value;

	public OperatorMismatch(String value, OperatorType opType) {
		this.value = value;
		this.opType = opType;
	}

	public String toString(){
		return "Trying to perform a " + opType + " operation on non-" + opType + " data. Invalid value was " + value +
				".";
	}
}
