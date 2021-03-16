import ParseExceptions.*;

public class DBParser {

	//This will act as a factory for instances of CMDType
	public CMDType parse(DBTokeniser tokeniser) {
		try {
			return chooseCMD(tokeniser);
		}
		catch(ParseExceptions exception){
			System.out.println("Command exception: " + exception);
		}
		return new CMDCreate();
	}

	private CMDType chooseCMD(DBTokeniser tokeniser) throws ParseExceptions{
		String nextToken = tokeniser.nextToken();
		switch (nextToken.toUpperCase()) {
			case "USE":
				return new CMDUse();
			case "CREATE":
				return new CMDCreate();
			case "INSERT":
				return new CMDInsert();
			case "SELECT":
				return new CMDSelect();
			case "UPDATE":
				return new CMDUpdate();
			case "ALTER":
				return new CMDAlter();
			case "DELETE":
				return new CMDDelete();
			case "DROP":
				return new CMDDrop();
			case "JOIN":
				return new CMDJoin();
			default:
				throw new InvalidFirstTokenError(nextToken);
		}
	}
}
