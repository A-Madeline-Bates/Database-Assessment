package DBMain.CommandFiles;
import DBMain.ModelFiles.DBModelPath;
import DBMain.ParseExceptions.NoDBFound;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public abstract class FileControlClasses extends InputTests {

	protected boolean doesDBExist(String dbName) throws NoDBFound {
		String location = "databaseFiles" + File.separator + dbName;
		Path path = Paths.get(location);
		if (Files.exists(path) && Files.isDirectory(path)) {
			return true;
		} else {
			throw new NoDBFound(dbName);
		}
	}

	protected void clearFilePath() {
		this.storagePath = new DBModelPath();
	}

}
