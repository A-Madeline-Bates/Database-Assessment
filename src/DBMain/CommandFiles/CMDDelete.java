package DBMain.CommandFiles;
import DBMain.*;
import DBMain.ModelFiles.*;
import DBMain.ParseExceptions.DomainType;
import DBMain.ParseExceptions.InvalidCommand;
import DBMain.ParseExceptions.ParseExceptions;
import DBMain.ParseExceptions.RequestedRow;

import java.util.ArrayList;

public class CMDDelete extends CMDWhere {

	public void transformModel() throws ParseExceptions {
		String firstCommand = getTokenSafe(DomainType.FROM);
		if(isItFromTHROW(firstCommand, "DELETE")){
			String secondCommand = getTokenSafe(DomainType.TABLENAME);
			if (doesTableExist(secondCommand)) {
				setTemporaryPath(secondCommand);
				setTemporaryData();
				String thirdCommand = getTokenSafe(DomainType.WHERE);
				if(isItWhereThrow(thirdCommand, "DELETE FROM [table]")) {
					splitIfBrackets(this);
					//processForcedWhere(this);
				}
			}
		}
	}

	protected void executeCMD(ArrayList<RequestedRow> finalRows){
		System.out.println("COLUMN:" + "[all]" + "WHERE ROW:" + finalRows);
	}

	public String query(DBServer server){
		return "Delete";
	}
}
