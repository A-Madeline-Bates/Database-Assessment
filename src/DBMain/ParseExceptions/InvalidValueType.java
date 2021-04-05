package DBMain.ParseExceptions;

import DBMain.DBEnums.OperatorType;

public class InvalidValueType extends SyntaxError{
	final OperatorType opType;

	public InvalidValueType(String token, OperatorType opType){
		this.token = token;
		this.opType = opType;
	}

	public String toString(){
		return "Invalid operator type. The operator you used in your WHERE cause only works with values\n" +
				"of type " + opType + ". Invalid value was " + token + ".";
	}
}
