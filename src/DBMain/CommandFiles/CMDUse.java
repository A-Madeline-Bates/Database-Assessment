package DBMain.CommandFiles;
import DBMain.DBTokeniser.DBTokeniser;
import DBMain.ModelFiles.DBModelPath;
import DBMain.ParseExceptions.*;

import java.io.IOException;

public class CMDUse extends FilesControl {

	public CMDUse(DBTokeniser tokeniser, DBModelPath path) throws IOException, ParseExceptions {
		buildCommand(tokeniser, path);
	}

//	public CMDUse(DBTokeniser tokeniser, DBModelPath path) throws ParseExceptions, IOException {
//		this.tokeniser = tokeniser;
//		this.storagePath = path;
//		transformModel();
//	}

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
