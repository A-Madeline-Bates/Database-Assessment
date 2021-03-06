package DBMain.CommandFiles;
import DBMain.DBEnums.DomainType;
import DBMain.DBLoad.DBLoad;
import DBMain.DBTokeniser.DBTokeniser;
import DBMain.ModelFiles.DBModelPath;
import DBMain.ParseExceptions.*;

import java.io.IOException;
import java.util.ArrayList;

public class CMDInsert extends MainDataClasses {
	final ArrayList<String> valueList = new ArrayList<>();

	public CMDInsert(DBTokeniser tokeniser, DBModelPath path) throws IOException, ParseExceptions {
		buildCommand(tokeniser, path);
	}

	public void transformModel() throws ParseExceptions, IOException {
		String firstCommand = getTokenSafe(DomainType.INTO);
		if (stringMatcherTHROW("INTO", firstCommand, "INSERT")) {
			String secondCommand = getTokenSafe(DomainType.TABLENAME);
			if (doesTableExist(secondCommand)) {
				String thirdCommand = getTokenSafe(DomainType.VALUES);
				if (stringMatcherTHROW("VALUES", thirdCommand, "INSERT INTO [table]")) {
					String fourthCommand = getTokenSafe(DomainType.BRACKET);
					if (stringMatcherTHROW("(", fourthCommand, "INSERT INTO [table] VALUES")) {
						//pass secondCommand on as it is our tableName
						collectValues(secondCommand);
						setExitMessage();
					}
				}
			}
		}
	}

	private void collectValues(String tableName) throws BNFError, IOException{
		String nextCommand = getTokenSafe(DomainType.VALUE);
		//if it's a ')', leave recursive loop and update table
		if (stringMatcher(")", nextCommand)) {
			if(isItLineEndTHROW()) {
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

	private void updateTable(String tableName) throws WrongNoValues, IOException{
		//load data into a temporary instance of DBModel so that we can find out if the user is trying to
		//load too many values into our table before they do it.
		String databaseName = storagePath.getDatabaseName();
		new DBLoad(temporaryDataModel, databaseName, tableName);
		//columnsAvailable is total column number - 1 to accommodate for the ID column
		if(isSizeCorrect(temporaryDataModel.getColumnNumber() - 1)){
			//we can now load the data into our 'storage' model- i.e the one that will be pushed to file
			new DBLoad(storageData, databaseName, tableName);
			storagePath.setFilename(tableName);
			storageData.setRowsFromSQL(valueList);
		}
	}

	private boolean isSizeCorrect(int columnsAvailable) throws WrongNoValues{
		if(valueList.size()==columnsAvailable){
			return true;
		}
		else {
			throw new WrongNoValues(valueList.size(), columnsAvailable);
		}
	}
}
