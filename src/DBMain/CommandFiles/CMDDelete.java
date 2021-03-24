package DBMain.CommandFiles;
import DBMain.ParseExceptions.DomainType;
import DBMain.ParseExceptions.ParseExceptions;
import DBMain.ParseExceptions.RequestedCell;

import java.io.IOException;
import java.util.ArrayList;

public class CMDDelete extends CMDWhere {

	public void transformModel() throws ParseExceptions, IOException {
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

	protected void returnToCMD(ArrayList<RequestedCell> finalRows){
		System.out.println("COLUMN:" + "[all]" + "WHERE ROW:" + finalRows);
		setExitMessage();
	}
}
