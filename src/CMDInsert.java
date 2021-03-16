import ParseExceptions.ParseExceptions;

public class CMDInsert extends CMDType {
	public void transformModel() throws ParseExceptions {}

	public String query(DBServer server){
		return "Insert";
	}
}
