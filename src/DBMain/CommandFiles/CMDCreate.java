package DBMain.CommandFiles;
import DBMain.*;
import DBMain.ParseExceptions.*;
import java.io.*;
import java.util.ArrayList;

public class CMDCreate extends CMDType {
	private ArrayList<String> attributeNames = new ArrayList<>();

	public String query(DBServer server){
		return "Create";
	}

	public void transformModel() throws ParseExceptions {
		String firstInstruction = getNewTokenSafe(DomainType.UNKNOWN);
		if (firstInstruction.equalsIgnoreCase("DATABASE")) {
			processDatabase();
		}
		else if (firstInstruction.equalsIgnoreCase("TABLE")) {
			processTable();
		} else {
			throw new InvalidCommand(firstInstruction, "CREATE", "DATABASE", "TABLE");
		}
	}

	private void processDatabase() throws ParseExceptions{
		String secondInstruction = getNewTokenSafe(DomainType.DATABASENAME);
		if(isNameAlphNumTHROW(secondInstruction, DomainType.DATABASENAME)) {
			if(isThisCommandLineEnd()) {
				createDatabase(secondInstruction.toUpperCase());
			}
		}
	}

	private void createDatabase(String dbName) throws ParseExceptions{
		String directoryPath = "databaseFiles" + File.separator + dbName;
		File newFolder = new File(directoryPath);
		//if database.exists, then call NotBuiltDB. If it doesn't, clear model and setDatabaseName
		if(newFolder.exists()){
			throw new NotBuiltDB(dbName);
		}
		//By clearing model, we are letting go of information about the file that was loaded
		//before this file was called. We want there to be no file loaded.
		clearModel();
		//This is copying information about what we've created into the model
		pathModel.setDatabaseName(dbName.toUpperCase());
	}

	private void processTable() throws ParseExceptions {
		String secondInstruction = getNewTokenSafe(DomainType.TABLENAME);
		if(areWeInADatabase()) {
			if (isNameAlphNumTHROW(secondInstruction, DomainType.TABLENAME)) {
				String thirdInstruction = getNewTokenSafe(DomainType.UNKNOWN);
				if(isThisSemicolon(thirdInstruction)) {
					//We are using a normal tokeniser.nextToken() here because we are expecting a NULL
					String extraInstruction = tokeniser.nextToken();
					if (isThisCommandEndTHROW(extraInstruction)) {
						//pass secondCommand on as it is our tableName
						createTable(secondInstruction);
					}
				}
				else if(thirdInstruction.equals("(")){
					collectAttributes(secondInstruction);
				}
				else{
					throw new InvalidCommand(thirdInstruction, "CREATE [table name]", "(", null);
				}
			}
		}
	}

	private void createTable(String tableName) throws ParseExceptions {
		//Save our database name before clearing the model so it doesn't get lost
		String databaseName = pathModel.getDatabaseName();
		String filePath = "databaseFiles" + File.separator + databaseName + File.separator + tableName;
		File newFile = new File(filePath);
		//if file.exists, then call NotBuiltFile. If it doesn't, setDatabaseName then set information.
		//This is to prevent CMDCreate overwriting a file which might have information in.
		if(newFile.exists()){
			throw new NotBuiltFile(tableName);
		}
		//By clearing model, we are letting go of information about the file that was loaded
		//before this file was called.
		clearModel();
		//This is copying information about what we've created into the model. New file information from
		//the model will be properly stored to file when DBStore is called.
		pathModel.setDatabaseName(databaseName);
		pathModel.setFilename(tableName);
		dataModel.setColumnDataFromSQL(attributeNames);
	}

	private void collectAttributes(String tableName) throws ParseExceptions{
		String nextInstruction = getNewTokenSafe(DomainType.ATTRIBUTENAME);
		//if it's a ')', leave recursive loop and create table
		if (nextInstruction.equals(")")) {
			if(isThisCommandLineEnd()) {
				createTable(tableName);
			}
		}
		//if it fits the conditions for an attribute name, save it to our list and call collectAttributes again
		else if (isNameAlphNumeric(nextInstruction)){
			attributeNames.add(nextInstruction);
			if(isItCommaSeparated(DomainType.ATTRIBUTENAME, ")")) {
				collectAttributes(tableName);
			}
		}
		//if it's not an attribute or a ')', throw an error
		else{
			throw new InvalidCommand(nextInstruction, "CREATE [table name] (",
					"[attributename])", ");");
		}
	}
}
