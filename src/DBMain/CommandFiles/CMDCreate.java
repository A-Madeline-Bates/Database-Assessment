package DBMain.CommandFiles;
import DBMain.*;
import DBMain.ParseExceptions.*;
import java.io.*;
import java.util.ArrayList;

public class CMDCreate extends CMDType {
	protected ArrayList<String> attributeNames = new ArrayList<>();

	public String query(DBServer server){
		return "Create";
	}

	public void transformModel() throws ParseExceptions, IOException {
		String firstInstruction = tokeniser.nextToken();
		if(doesTokenExist(firstInstruction, DomainType.UNKNOWN)) {
			if (firstInstruction.toUpperCase().equals("DATABASE")) {
				processDatabase();
			}
			else if (firstInstruction.toUpperCase().equals("TABLE")) {
				processTable();
			} else {
				throw new InvalidCommand(firstInstruction, "CREATE", "DATABASE", "TABLE");
			}
		}
	}

	private void processDatabase() throws ParseExceptions{
		String secondInstruction = tokeniser.nextToken();
		if (areDBCommandsValid(secondInstruction)) {
			createDatabase(secondInstruction.toUpperCase());
		}
	}

	private boolean areDBCommandsValid(String secondInstruction) throws ParseExceptions{
		if(isNameValid(secondInstruction, DomainType.DATABASENAME)) {
			String extraCommand = tokeniser.nextToken();
			if(isThisCommandEndTHROW(extraCommand)){
				return true;
			}
		}
		return false;
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

	private void processTable() throws ParseExceptions, IOException {
		String secondInstruction = tokeniser.nextToken();
		if(areWeInADatabase()) {
			if (isNameValid(secondInstruction, DomainType.TABLENAME)) {
				String thirdInstruction = tokeniser.nextToken();
				if(isThisCommandEnd(thirdInstruction)){
					createTable(secondInstruction);
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

	private void createTable(String tableName) throws ParseExceptions, IOException {
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
		dataModel.setColumnDataByArrlist(attributeNames);
	}

	private void collectAttributes(String tableName) throws ParseExceptions, IOException{
		String nextInstruction = tokeniser.nextToken();
		if(doesTokenExist(nextInstruction, DomainType.ATTRIBUTENAME)) {
			//if it's a ')', leave recursive loop and create table
			if (nextInstruction.equals(")")) {
				createTable(tableName);
			}
			//if it fits the conditions for an attribute name, save it to our list and call collectAttributes again
			else if (isItAlphNumeric(nextInstruction, DomainType.ATTRIBUTENAME)){
				attributeNames.add(nextInstruction);
				collectAttributes(tableName);
			}
			//if it's not an attribute or a ')', throw an error
			else{
				throw new InvalidCommand(nextInstruction, "CREATE [table name] (",
						"[attributename])", null);
			}
		}
	}
}
