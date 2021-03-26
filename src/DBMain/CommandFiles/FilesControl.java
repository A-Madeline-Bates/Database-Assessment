package DBMain.CommandFiles;

import DBMain.ModelFiles.DBModelPath;
import DBMain.ParseExceptions.DoesNotExistDB;
import DBMain.ParseExceptions.ParseExceptions;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public abstract class FilesControl extends StringMethods{

	protected boolean doesDBExist(String dbName) throws ParseExceptions {
		String location = "databaseFiles" + File.separator + dbName;
		Path path = Paths.get(location);
		if (Files.exists(path) && Files.isDirectory(path)) {
			return true;
		} else {
			throw new DoesNotExistDB(dbName);
		}
	}

	protected void clearFilePath() {
		this.storagePath = new DBModelPath();
	}

}
