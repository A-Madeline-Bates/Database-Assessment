import ParseExceptions.ParseExceptions;

public class CMDSelect extends CMDType {
	public void transformModel() throws ParseExceptions {}

	public String query(DBServer server){
		return "Select";
	}
}
