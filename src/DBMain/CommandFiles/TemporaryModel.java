package DBMain.CommandFiles;

import DBMain.DBLoad.DBLoad;
import DBMain.ModelFiles.DBModelTable;
import DBMain.ModelFiles.DBModelPath;

import java.io.IOException;

public abstract class TemporaryModel extends StringMethods{
	//these are 'temporary' versions of the model that can be used to check data without running the risk of
	//storing the data you're working on (or wiping a file)
	final DBModelTable temporaryDataModel = new DBModelTable();
	final DBModelPath temporaryPathModel = new DBModelPath();

	/******************************************************
	 ***************** LOAD TEMPORARY MODEL ****************
	 *****************************************************/

	protected void setTemporaryModel(String fileName, DBModelPath tempPath, DBModelTable tempData) throws IOException {
		tempPath.setFilename(fileName);
		tempPath.setDatabaseName(storagePath.getDatabaseName());
		new DBLoad(tempData, storagePath.getDatabaseName(), fileName);
	}
}
