package DBMain.CommandFiles;

import DBMain.ParseExceptions.InvalidValue;
import DBMain.ParseExceptions.InvalidValueType;
import DBMain.ParseExceptions.OperatorType;
import DBMain.ParseExceptions.ParseExceptions;

public abstract class CMDValidValue extends CMDStringMethods{

	/******************************************************
	 ******************* VALID VALUE TEST *****************
	 *****************************************************/

	protected boolean isItValidValue(String nextInstruction) throws ParseExceptions {
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

	protected void isItStringTHROW(String nextInstruction) throws ParseExceptions {
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

	protected void isItNumTHROW(String nextInstruction) throws ParseExceptions {
		if ((!isItInteger(nextInstruction)) || (!isItFloat(nextInstruction))) {
			throw new InvalidValueType(nextInstruction, OperatorType.NUMERICAL);
		}
	}
}
