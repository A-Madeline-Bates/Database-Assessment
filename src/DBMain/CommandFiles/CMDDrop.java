package DBMain.CommandFiles;
import DBMain.*;
import DBMain.ParseExceptions.DomainType;
import DBMain.ParseExceptions.InvalidCommand;
import DBMain.ParseExceptions.ParseExceptions;

import java.io.File;

public class CMDDrop extends CMDType {

	public void transformModel() throws ParseExceptions {
		String firstInstruction = getTokenSafe(DomainType.UNKNOWN);
		if (stringMatcher("DATABASE", firstInstruction)) {
			processDB();
			setExitMessage();
		}
		else if (stringMatcher("TABLE", firstInstruction)) {
			processTable();
			setExitMessage();
		} else {
			throw new InvalidCommand(firstInstruction, "DROP", "DATABASE", "TABLE");
		}
	}

	private void processDB() throws ParseExceptions{
		String nextCommand = getTokenSafe(DomainType.DATABASENAME);
		if(doesDBExist(nextCommand)) {
			if (isItLineEndTHROW()) {
				//if database in use is the one we're deleting, remove it
				if(nextCommand.equals(storagePath.getDatabaseName())) {
					clearFilePath();
				}
				String dbLocation = "databaseFiles" + File.separator + nextCommand;
				File file = new File(dbLocation);
				deleteDB(file);
			}
		}
	}

	private void deleteDB(File file){
		File[] fileList = file.listFiles();
		if (fileList != null) {
			for (File temp : fileList) {
				deleteDB(temp);
			}
		}
		file.delete();
	}

	private void processTable() throws ParseExceptions {
		String nextCommand = getTokenSafe(DomainType.TABLENAME);
		if(doesTableExist(nextCommand)) {
			if (isItLineEndTHROW()) {
				//if filename in use is the one we're deleting, remove it
				if (nextCommand == storagePath.getFilename()) {
					storagePath.setFilename(null);
				}
				String currentDatabase = storagePath.getDatabaseName();
				String fileLocation = "databaseFiles" + File.separator + currentDatabase + File.separator + nextCommand;
				File file = new File(fileLocation);
				file.delete();
			}
		}
	}
}
