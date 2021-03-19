package DBMain.CommandFiles;
import DBMain.*;
import DBMain.ModelFiles.*;
import DBMain.ParseExceptions.ParseExceptions;

public class CMDUpdate extends CMDType {
	public void transformModel() throws ParseExceptions {}

	public String query(DBServer server){
		return "Updater";
	}
}