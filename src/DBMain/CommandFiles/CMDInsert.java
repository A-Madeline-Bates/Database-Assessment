package DBMain.CommandFiles;
import DBMain.*;
import DBMain.ModelFiles.*;
import DBMain.ParseExceptions.*;

import java.util.ArrayList;

public class CMDInsert extends CMDType {
	private ArrayList<String> valueList = new ArrayList<>();

	public void transformModel() throws ParseExceptions {
		String firstCommand = getNewTokenSafe(DomainType.INTO);
		if(areWeInADatabase()) {
			if (firstCommand.equalsIgnoreCase("INTO")) {
				String secondCommand = getNewTokenSafe(DomainType.TABLENAME);
				if (doesTableExist(secondCommand)) {
					String thirdCommand = getNewTokenSafe(DomainType.VALUES);
					if (thirdCommand.equalsIgnoreCase("VALUES")) {
						String fourthCommand = getNewTokenSafe(DomainType.BRACKET);
						if (fourthCommand.equals("(")) {
							//pass secondCommand on as it is our tableName
							collectValues(secondCommand);
						} else {
							throw new InvalidCommand(fourthCommand, "VALUES", "(", null);
						}
					} else {
						throw new InvalidCommand(thirdCommand, "INSERT INTO [tablename]", "VALUES", null);
					}
				}
			} else {
				throw new InvalidCommand(firstCommand, "INSERT", "INTO", null);
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
		}
		//if it's not a value or a ')', throw an error
		else{
			throw new InvalidCommand(nextCommand, "INSERT INTO [TableName] VALUES (",
					"[value]", ");");
		}
	}

	private void updateTable(String tableName) throws ParseExceptions{
		//create an temporary instance of DBModel so that we can find out if the user is trying to
		//load too many values into our table before they do it.
		DBModelData temporaryModel = new DBModelData();
		new DBLoad(temporaryModel, pathModel.getDatabaseName(), tableName);
		if(isValueListSmallEnough(temporaryModel)){
			//saving database name before clearing model
			String databaseName = pathModel.getDatabaseName();
			//we need to clear model before loading to it in order to prevent messy data
			clearModel();
			new DBLoad(dataModel, databaseName, tableName);
			pathModel.setDatabaseName(databaseName);
			pathModel.setFilename(tableName);
			dataModel.setNewRow(valueList);
		}
	}

	private boolean isValueListSmallEnough(DBModelData temporaryModel) throws ParseExceptions{
		if(valueList.size()<=temporaryModel.getColumnNumber()){
			return true;
		}
		else {
			throw new TooManyValues(valueList.size(), dataModel.getColumnNumber());
		}
	}

	public String query(DBServer server){
		return "Insert";
	}
}
