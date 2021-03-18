package DBMain.CommandFiles;
import DBMain.*;
import DBMain.ModelFiles.*;
import DBMain.ParseExceptions.ParseExceptions;

public class CMDInsert extends CMDType {
	public void transformModel() throws ParseExceptions {}

	//insert whatever is given into a new row

	public String query(DBServer server){
		return "Insert";
	}

}
