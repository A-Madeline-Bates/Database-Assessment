package DBMain.CommandFiles;
import DBMain.*;
import DBMain.ModelFiles.*;
import DBMain.ParseExceptions.*;
import java.util.Locale;

public class CMDUse extends CMDType {

	public void transformModel() throws ParseExceptions {
		String firstCommand = getTokenSafe(DomainType.DATABASENAME);
		if(doesDBExist(firstCommand)){
			if(isItLineEndTHROW()) {
				clearFilePath();
				storagePath.setDatabaseName(firstCommand.toUpperCase());
			}
		}
	}

	public String query(DBServer server){
		return "Use";
	}
}
