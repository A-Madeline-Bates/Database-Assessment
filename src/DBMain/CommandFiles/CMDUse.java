package DBMain.CommandFiles;
import DBMain.DBTokeniser.DBTokeniser;
import DBMain.ModelFiles.DBModelPath;
import DBMain.ParseExceptions.*;

import java.io.IOException;

public class CMDUse extends FileControlClasses {

	public CMDUse(DBTokeniser tokeniser, DBModelPath path) throws IOException, ParseExceptions {
		buildCommand(tokeniser, path);
	}

	public void transformModel() throws ParseExceptions, IOException {
		String firstCommand = getTokenSafe(DomainType.DATABASENAME);
		if(doesDBExist(firstCommand)){
			if(isItLineEndTHROW()) {
				clearFilePath();
				storagePath.setDatabaseName(firstCommand.toUpperCase());
				setExitMessage();
			}
		}
	}
}
