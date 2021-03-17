package DBMain.CommandFiles;
import DBMain.*;
import DBMain.ModelFiles.*;
import DBMain.ParseExceptions.*;

public class CMDUse extends CMDType {

	public void transformModel() throws ParseExceptions {
		String dbName = tokeniser.nextToken();
		if(isDBValid(dbName)){
			clearModel();
			pathModel.setDatabaseName(dbName);
		}
	}

	public String query(DBServer server){
		return "Use";
	}
}
