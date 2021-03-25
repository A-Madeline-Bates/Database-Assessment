package DBMain.CommandFiles;
import DBMain.ParseExceptions.*;

import java.io.IOException;

public class CMDUse extends CMDType {

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
