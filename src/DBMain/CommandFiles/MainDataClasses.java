package DBMain.CommandFiles;
import DBMain.ParseExceptions.InvalidValue;
import DBMain.ParseExceptions.InvalidValueType;
import DBMain.DBEnums.OperatorType;

public abstract class MainDataClasses extends TemporaryModel {
	//all classes which need access to the rows data are extended from this class

	/******************************************************
	 ******************* VALID VALUE TEST *****************
	 *****************************************************/

	protected boolean isItValidValue(String nextInstruction) throws InvalidValue {
		if (isItString(nextInstruction)
				|| isItBoolean(nextInstruction)
				|| isItFloat(nextInstruction)
				|| isItInteger(nextInstruction)) {
			return true;
		} else {
			throw new InvalidValue(nextInstruction);
		}
	}

	protected boolean isItString(String nextInstruction) {
		if (nextInstruction.startsWith("'") && nextInstruction.endsWith("'")) {
			return true;
		}
		return false;
	}

	protected void isItStringTHROW(String nextInstruction) throws InvalidValueType {
		if (!isItString(nextInstruction)) {
			throw new InvalidValueType(nextInstruction, OperatorType.STRING);
		}
	}

	protected boolean isItBoolean(String nextInstruction) {
		if (nextInstruction.equalsIgnoreCase("true") || nextInstruction.equalsIgnoreCase("false")) {
			return true;
		}
		return false;
	}

	protected boolean isItFloat(String nextInstruction) {
		try {
			Float.parseFloat(nextInstruction);
		} catch (NumberFormatException n) {
			return false;
		}
		return true;
	}

	protected boolean isItInteger(String nextInstruction) {
		try {
			Integer.parseInt(nextInstruction);
		} catch (NumberFormatException n) {
			return false;
		}
		return true;
	}

	protected void isItNumTHROW(String nextInstruction) throws InvalidValueType {
		if ((!isItInteger(nextInstruction)) || (!isItFloat(nextInstruction))) {
			throw new InvalidValueType(nextInstruction, OperatorType.NUMERICAL);
		}
	}
}
