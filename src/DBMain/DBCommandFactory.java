package DBMain;
import DBMain.CommandFiles.*;
import DBMain.DBTokeniser.DBTokeniser;
import DBMain.ModelFiles.DBModelPath;
import DBMain.ParseExceptions.*;
import java.io.IOException;

public class DBCommandFactory {
	//This will act as a factory for instances of CMDType
	public CMDType createCMD(DBTokeniser tokeniser, DBModelPath path) throws ParseExceptions, IOException {
		String nextToken = tokeniser.nextToken();
		if(nextToken == null){
			throw new CommandMissing(DomainType.FIRSTCOMMAND);
		}
		else{
			return commandSwitch(nextToken, tokeniser, path);
		}
	}

	private CMDType commandSwitch(String nextToken, DBTokeniser tokeniser, DBModelPath path) throws ParseExceptions, IOException{
		switch (nextToken.toUpperCase()) {
			case "USE":
				return new CMDUse(tokeniser, path);
			case "CREATE":
				return new CMDCreate(tokeniser, path);
			case "INSERT":
				return new CMDInsert(tokeniser, path);
			case "SELECT":
				return new CMDSelect(tokeniser, path);
			case "UPDATE":
				return new CMDUpdate(tokeniser, path);
			case "ALTER":
				return new CMDAlter(tokeniser, path);
			case "DELETE":
				return new CMDDelete(tokeniser, path);
			case "DROP":
				return new CMDDrop(tokeniser, path);
			case "JOIN":
				return new CMDJoin(tokeniser, path);
			default:
				throw new InvalidFirstCommand(nextToken);
		}
	}
}
