package DBMain.CommandFiles;
import DBMain.*;
import DBMain.ModelFiles.*;
import DBMain.ParseExceptions.*;
import java.util.Locale;

public class CMDUse extends CMDType {

	public void transformModel() throws ParseExceptions {
		String firstCommand = getNewTokenSafe(DomainType.DATABASENAME);
		if(isDBValid(firstCommand)){
			if(isThisCommandLineEnd()) {
				clearFilePath();
				storagePath.setDatabaseName(firstCommand.toUpperCase());
			}
		}
	}

	public String query(DBServer server){
		return "Use";
	}
}
