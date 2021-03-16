import ParseExceptions.ParseExceptions;

public class CMDDrop extends CMDType {
	public void transformModel() throws ParseExceptions {}
	public String query(DBServer server){
		return "Drop";
	}
}
