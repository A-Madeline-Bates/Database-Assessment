package DBMain.CommandFiles;
import DBMain.*;
import DBMain.ModelFiles.*;
import DBMain.ParseExceptions.*;
import java.util.Locale;

public class CMDUse extends CMDType {

	public void transformModel() throws ParseExceptions {
		String dbName = tokeniser.nextToken();
		if(isDBValid(dbName)){
			String extraCommand = tokeniser.nextToken();
			if(isThisCommandEndTHROW(extraCommand)) {
				clearModel();
				pathModel.setDatabaseName(dbName.toUpperCase());
			}
		}
	}

	public String query(DBServer server){
		return "Use";
	}
}
