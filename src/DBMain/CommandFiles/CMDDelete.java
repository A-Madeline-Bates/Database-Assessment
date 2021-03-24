package DBMain.CommandFiles;
import DBMain.ParseExceptions.DomainType;
import DBMain.ParseExceptions.ParseExceptions;
import DBMain.ParseExceptions.RequestedCell;

import java.util.ArrayList;

public class CMDDelete extends CMDWhere {

	public void transformModel() throws ParseExceptions {
		String firstCommand = getTokenSafe(DomainType.FROM);
		if(stringMatcherTHROW("FROM", firstCommand, "DELETE")){
			String secondCommand = getTokenSafe(DomainType.TABLENAME);
			if (doesTableExist(secondCommand)) {
				setTemporaryPath(secondCommand);
				setTemporaryData();
				String thirdCommand = getTokenSafe(DomainType.WHERE);
				if(stringMatcherTHROW("WHERE", thirdCommand, "DELETE FROM [table]")) {
					splitIfBrackets(this);
				}
			}
		}
	}

	protected void executeCMD(ArrayList<RequestedCell> finalRows){
		System.out.println("COLUMN:" + "[all]" + "WHERE ROW:" + finalRows);
		setExitMessage();
	}
}
