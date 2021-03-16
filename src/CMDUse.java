import ParseExceptions.*;

public class CMDUse extends CMDType {

	public CMDUse(){
	}

	public void transformModel() throws ParseExceptions {
		String dbName = tokeniser.nextToken();
		clearModel();
		if(isDBNameValid(dbName)){
			pathModel.setDatabaseName(dbName);
		}
	}

	public String query(DBServer server){
		return "Use";
	}
}
