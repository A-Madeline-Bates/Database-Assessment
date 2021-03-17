package DBMain.CommandFiles;
import DBMain.*;
import DBMain.ModelFiles.*;
import DBMain.ParseExceptions.*;
import java.io.*;

public class CMDCreate extends CMDType {
	public String query(DBServer server){
		return "Create";
	}

	public void transformModel() throws ParseExceptions {
		String firstInstruction = tokeniser.nextToken();
		if(doesTokenExist(firstInstruction, DomainType.UNKNOWN)) {
			if (firstInstruction.toUpperCase().equals("DATABASE")) {
				processDatabase();
			}
			else if (firstInstruction.toUpperCase().equals("TABLE")) {
				processTable();
			} else {
				throw new InvalidTokenError(firstInstruction, "CREATE", "DATABASE", "TABLE");
			}
		}
	}

	private void processDatabase() throws ParseExceptions{
		String secondInstruction = tokeniser.nextToken();
		if (areDBCommandsValid(secondInstruction)) {
			createDatabase(secondInstruction.toUpperCase());
			//let go of the file that was loaded before this file was called. There is currently no file loaded.
			clearModel();
		}
	}

	private boolean areDBCommandsValid(String secondInstruction) throws ParseExceptions{
		if(isNameValid(secondInstruction, DomainType.DATABASENAME)) {
			String extraCommand = tokeniser.nextToken();
			if(isItFinalCommand(extraCommand)){
				return true;
			}
		}
		return false;
	}

	private void createDatabase(String dbName) throws ParseExceptions{
		String directoryPath = "databaseFiles" + File.separator + dbName;
		File newFolder = new File(directoryPath);
		if(!newFolder.mkdirs()){
			throw new DBNotBuiltErr(dbName);
		}
	}

	private void processTable() throws ParseExceptions{
		String secondInstruction = tokeniser.nextToken();

//			firstInstruction = tokeniser.nextToken();
//			//this is split into more stages to accommodate recursively searching for attributes
//			if(isNameValid(firstInstruction.toUpperCase(), DomainType.TABLENAME)){
//				//code here to check attribute list stuff
//				//and then createTable()
//			}
	}
}
