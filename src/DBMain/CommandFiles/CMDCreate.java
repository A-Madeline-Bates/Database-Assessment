package DBMain.CommandFiles;
import DBMain.DBTokeniser.DBTokeniser;
import DBMain.ModelFiles.DBModelPath;
import DBMain.ParseExceptions.*;
import java.io.*;
import java.util.ArrayList;

public class CMDCreate extends FileControlClasses {
	final ArrayList<String> attributeNames = new ArrayList<>();

	public CMDCreate(DBTokeniser tokeniser, DBModelPath path) throws IOException, ParseExceptions {
		super(tokeniser, path);
	}

	public void transformModel() throws ParseExceptions, IOException {
		String firstInstruction = getTokenSafe(DomainType.UNKNOWN);
		if (stringMatcher("DATABASE", firstInstruction)) {
			processDatabase();
			setExitMessage();
		}
		else if (stringMatcher("TABLE", firstInstruction)) {
			processTable();
			setExitMessage();
		} else {
			throw new InvalidCommand(firstInstruction, "CREATE", "DATABASE", "TABLE");
		}
	}

	private void processDatabase() throws ParseExceptions{
		String secondInstruction = getTokenSafe(DomainType.DATABASENAME);
		if(isItAlphNumTHROW(secondInstruction, DomainType.DATABASENAME)) {
			if(isItLineEndTHROW()) {
				createDatabase(secondInstruction.toUpperCase());
			}
		}
	}

	private void createDatabase(String dbName) throws NotBuiltDB{
		String directoryPath = "databaseFiles" + File.separator + dbName;
		File newFolder = new File(directoryPath);
		//if database.exists, then call NotBuiltDB. If it doesn't, clear model and setDatabaseName
		if(newFolder.exists()){
			throw new NotBuiltDB(dbName);
		}
		//By clearing model, we are letting go of information about the file that was loaded
		//before this file was called. We want there to be no file loaded.
		clearFilePath();
		//This is copying information about what we've created into the model
		storagePath.setDatabaseName(dbName.toUpperCase());
	}

	private void processTable() throws ParseExceptions {
		String secondInstruction = getTokenSafe(DomainType.TABLENAME);
		if(areWeInDB()) {
			if (isItAlphNumTHROW(secondInstruction, DomainType.TABLENAME)) {
				splitByBrackets(secondInstruction);
			}
		}
	}

	private void splitByBrackets(String tableName) throws ParseExceptions{
		String thirdInstruction = getTokenSafe(DomainType.UNKNOWN);
		if(stringMatcher(";", thirdInstruction)) {
			//We are using a normal tokeniser.nextToken() here because we are expecting a NULL
			String extraInstruction = tokeniser.nextToken();
			if (isItNullEndTHROW(extraInstruction)) {
				//pass secondCommand on as it is our tableName
				createTable(tableName);
			}
		}
		else if(stringMatcher("(", thirdInstruction)){
			collectAttributes(tableName);
		}
		else{
			throw new InvalidCommand(thirdInstruction, "CREATE [table name]", "(", null);
		}
	}

	private void createTable(String tableName) throws ParseExceptions {
		String databaseName = storagePath.getDatabaseName();
		String filePath = "databaseFiles" + File.separator + databaseName + File.separator + tableName;
		File newFile = new File(filePath);
		//if file.exists, then call NotBuiltFile. If it doesn't, setDatabaseName then set information.
		//This is to prevent CMDCreate overwriting a file which might have information in.
		if(newFile.exists()){
			throw new NotBuiltFile(tableName);
		}
		//This is copying information about what we've created into the model. Information from
		//the storagePath and storageData model will be stored to file when DBStore is called.
		storagePath.setFilename(tableName);
		storageColumns.setColumnsFromSQL(attributeNames);
	}

	private void collectAttributes(String tableName) throws ParseExceptions{
		String nextInstruction = getTokenSafe(DomainType.ATTRIBUTENAME);
		//if it's a ')', leave recursive loop and create table
		if (stringMatcher(")", nextInstruction)) {
			if(isItLineEndTHROW()) {
				createTable(tableName);
			}
		}
		//if it fits the conditions for an attribute name, save it to our list and call collectAttributes again
		else if (isItAlphNumeric(nextInstruction)){
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

	protected boolean areWeInDB() throws WorkingOutsideDatabase {
		if (storagePath.getDatabaseName() != null) {
			return true;
		} else {
			throw new WorkingOutsideDatabase();
		}
	}
}
