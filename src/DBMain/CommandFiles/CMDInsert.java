package DBMain.CommandFiles;
import DBMain.*;
import DBMain.ModelFiles.*;
import DBMain.ParseExceptions.*;

import java.util.ArrayList;

public class CMDInsert extends CMDType {
	private ArrayList<String> valueList = new ArrayList<>();

	//divide word identifiers in 'isIt...' methods
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
			if(isItCommaSeparated(DomainType.VALUE, ")")) {
				collectValues(tableName);
			}
		}
		//if it's not a value or a ')', throw an error
		else{
			throw new InvalidCommand(nextCommand, "INSERT INTO [TableName] VALUES (",
					"[value]", ");");
		}
	}

	private void updateTable(String tableName) throws ParseExceptions{

		//load data into a temporary instance of DBModel so that we can find out if the user is trying to
		//load too many values into our table before they do it.
		String databaseName = storagePath.getDatabaseName();
		new DBLoad(temporaryDataModel, databaseName, tableName);
		//columnsAvailable is total column number - 1 to accommodate for the ID column
		if(isListSizeCorrect(temporaryDataModel.getColumnNumber() - 1)){
			//we can now load the data into our 'storage' model- i.e the one that will be pushed to file
			new DBLoad(storageData, databaseName, tableName);
			storagePath.setFilename(tableName);
			storageData.setRowsDataFromSQL(valueList);
		}
	}

	private boolean isListSizeCorrect(int columnsAvailable) throws ParseExceptions{
		if(valueList.size()==columnsAvailable){
			return true;
		}
		else {
			throw new WrongNoValues(valueList.size(), columnsAvailable);
		}
	}

	public String query(DBServer server){
		return "Insert";
	}
}
