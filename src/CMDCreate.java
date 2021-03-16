import ParseExceptions.ParseExceptions;

public class CMDCreate extends CMDType {
	public void transformModel() throws ParseExceptions {}
	public String query(DBServer server){
		return "Create";
	}
}
