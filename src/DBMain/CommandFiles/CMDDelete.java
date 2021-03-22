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
		String firstCommand = getNewTokenSafe(DomainType.FROM);
		if(isItFromTHROW(firstCommand, "DELETE")){
			String secondCommand = getNewTokenSafe(DomainType.TABLENAME);
			if (doesTableExist(secondCommand)) {
				setTemporaryPath(secondCommand);
				setTemporaryData();
				String thirdCommand = getNewTokenSafe(DomainType.WHERE);
				if(isItWhereThrow(thirdCommand, "DELETE FROM [table]")) {
					splitIfBrackets(this);
					//processForcedWhere(this);
				}
			}
		}
	}

	//FROM appears in another class. Maybe merge?
	private boolean isItFrom(String nextCommand){
		if (nextCommand.equalsIgnoreCase("FROM")) {
			return true;
		}
		return false;
	}

	private boolean isItFromTHROW(String nextCommand, String prevCommand) throws ParseExceptions{
		if (isItFrom(nextCommand)) {
			return true;
		}
		throw new InvalidCommand(nextCommand, prevCommand, "FROM", null);
	}

	protected void executeCMD(ArrayList<RequestedRow> finalRows){
		System.out.println("COLUMN:" + "[all]" + "WHERE ROW:" + finalRows);
	}

	public String query(DBServer server){
		return "Delete";
	}
}
