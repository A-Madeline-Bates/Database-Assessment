import ParseExceptions.ParseExceptions;

public class CMDDelete extends CMDType {
	public void transformModel() throws ParseExceptions {}
	public String query(DBServer server){
		return "Delete";
	}
}
