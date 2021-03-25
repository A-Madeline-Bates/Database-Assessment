package DBMain.ParseExceptions;

public class OperatorDataMismatch extends ParseExceptions{
	OperatorType opType;
	String value;
	public OperatorDataMismatch(String value, OperatorType opType) {
		this.value = value;
		this.opType = opType;
	}

	public String toString(){
		return "Trying to perform a " + opType + " operation on non-" + opType + " data. Invalid value was " + value +
				".";
	}
}