package DBMain.CommandFiles;
import DBMain.*;
import DBMain.ModelFiles.*;
import DBMain.ParseExceptions.DomainType;
import DBMain.ParseExceptions.InvalidCommand;
import DBMain.ParseExceptions.ParseExceptions;

import java.util.ArrayList;

public class CMDInsert extends CMDType {
	private ArrayList<String> valueList = new ArrayList<>();

	public void transformModel() throws ParseExceptions {
		//<Insert> ::= INTO <TableName> VALUES (<ValueList>)
		String firstCommand = getNewTokenSafe(DomainType.INTO);
		if(firstCommand.equalsIgnoreCase("INTO")){
			String secondCommand = getNewTokenSafe(DomainType.TABLENAME);
			if(doesTableExist(secondCommand)) {
				String thirdCommand = getNewTokenSafe(DomainType.INTO);
				if (thirdCommand.equalsIgnoreCase("VALUES")) {
					String fourthCommand = getNewTokenSafe(DomainType.BRACKET);
					if(fourthCommand.equals("(")) {
						//pass secondCommand on as it is our tableName
						collectValues(secondCommand);
					}
				}
			}
		}
	}

	private void collectValues(String tableName) throws ParseExceptions{
		String nextCommand = getNewTokenSafe(DomainType.VALUE);
		//if it's a ')', leave recursive loop and update table
		if (nextCommand.equals(")")) {
			if(isThisCommandLineEnd()) {
				updateTable(tableName);
			}
		}
		//if it fits the conditions to be a value, save it to our list and call collectValues again
		else if (isItValidValue(nextCommand)){
			valueList.add(nextCommand);
			collectValues(tableName);
			//WHEN WE ARE UPDATING TABLE, CHECK THAT VALIDVALUE.SIZE<COLUMNVALUE.SIZE
		}
		//if it's not a value or a ')', throw an error
		else{
			throw new InvalidCommand(nextCommand, "INSERT INTO [TableName] VALUES (",
					"[value]", ");");
		}
	}

	private void updateTable(String tableName){

	}

	public String query(DBServer server){
		return "Insert";
	}

}
