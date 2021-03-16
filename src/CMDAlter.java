import ParseExceptions.ParseExceptions;

public class CMDAlter extends CMDType {
	public void transformModel() throws ParseExceptions {}
	public String query(DBServer server){
		return "Alter";
	}
}
