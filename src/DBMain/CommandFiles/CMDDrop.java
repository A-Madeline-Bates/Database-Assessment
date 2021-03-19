package DBMain.CommandFiles;
import DBMain.*;
import DBMain.ModelFiles.*;
import DBMain.ParseExceptions.DomainType;
import DBMain.ParseExceptions.InvalidCommand;
import DBMain.ParseExceptions.ParseExceptions;

import java.io.File;

public class CMDDrop extends CMDType {

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
		String nextCommand = getNewTokenSafe(DomainType.DATABASENAME);
		if(isDBValid(nextCommand)) {
			if (isThisCommandLineEnd()) {
				//IT MAY BE A BETTER DECISION NOT TO CLEAR MODEL- DECIDE
				if(nextCommand == pathModel.getDatabaseName()) {
					clearModel();
				}
				String dbLocation = "databaseFiles" + File.separator + nextCommand;
				File file = new File(dbLocation);
				deleteDatabase(file);
			}
		}
	}

	private void deleteDatabase(File file){
		File[] fileList = file.listFiles();
		if (fileList != null) {
			for (File temp : fileList) {
				deleteDatabase(temp);
			}
		}
		file.delete();
	}

	private void processTable() throws ParseExceptions {
		String nextCommand = getNewTokenSafe(DomainType.DATABASENAME);
		if(doesTableExist(nextCommand)) {
			if (isThisCommandLineEnd()) {
				if (nextCommand == pathModel.getFilename()) {
					clearModel();
				}
				String currentDatabase = pathModel.getDatabaseName();
				String fileLocation = "databaseFiles" + File.separator + currentDatabase + File.separator + nextCommand;
				File file = new File(fileLocation);
				file.delete();
			}
		}
	}

		public String query(DBServer server){
		return "Drop";
	}
}
