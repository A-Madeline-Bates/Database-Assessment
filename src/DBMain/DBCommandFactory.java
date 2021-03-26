package DBMain;
import DBMain.CommandFiles.*;
import DBMain.DBTokeniser.DBTokeniser;
import DBMain.ParseExceptions.*;

public class DBCommandFactory {
	//This will act as a factory for instances of CMDType

	public CMDType createCMD(DBTokeniser tokeniser) throws ParseExceptions{
		String nextToken = tokeniser.nextToken();
		if(nextToken == null){
			throw new CommandMissing(DomainType.FIRSTCOMMAND);
		}
		else{
			return commandSwitch(nextToken);
		}
	}

	private CMDType commandSwitch(String nextToken) throws ParseExceptions{
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
				throw new InvalidFirstCommand(nextToken);
		}
	}
}
