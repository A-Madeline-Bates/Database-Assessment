package DBMain.CommandFiles;
import DBMain.*;
import DBMain.ModelFiles.*;
import DBMain.ParseExceptions.ParseExceptions;

public class CMDSelect extends CMDType {

	public void transformModel() throws ParseExceptions {
		//<Select>         ::=  SELECT <WildAttribList> FROM <TableName> |
		//						SELECT <WildAttribList> FROM <TableName> WHERE <Condition>

	}

	public String query(DBServer server){
		return "Select";
	}
}
