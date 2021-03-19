package DBMain.CommandFiles;
import DBMain.*;
import DBMain.ModelFiles.*;
import DBMain.ParseExceptions.DomainType;
import DBMain.ParseExceptions.InvalidCommand;
import DBMain.ParseExceptions.ParseExceptions;

public class CMDDrop extends CMDType {

	public void transformModel() throws ParseExceptions {
		String firstInstruction = getNewTokenSafe(DomainType.UNKNOWN);
		if (firstInstruction.equalsIgnoreCase("DATABASE")) {
			processDatabase();
		}
		else if (firstInstruction.equalsIgnoreCase("TABLE")) {
			processTable();
		} else {
			throw new InvalidCommand(firstInstruction, "CREATE", "DATABASE", "TABLE");
		}
	}

	private void processDatabase() throws ParseExceptions{
		String nextCommand = getNewTokenSafe(DomainType.DATABASENAME);
		if(isDBValid(nextCommand)) {
			if (isThisCommandLineEnd()) {
				clearModel();
				deleteDatabase();
			}
		}
	}

	private void deleteDatabase(){

	}

	public String query(DBServer server){
		return "Drop";
	}
}
