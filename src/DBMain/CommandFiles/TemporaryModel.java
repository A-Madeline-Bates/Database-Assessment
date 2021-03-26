package DBMain.CommandFiles;

import DBMain.DBLoad.DBLoad;
import DBMain.ModelFiles.DBModelData;
import DBMain.ModelFiles.DBModelPath;

import java.io.IOException;

public abstract class TemporaryModel extends StringMethods{
	//these are 'temporary' versions of the model that can be used to check data without running the risk of
	//storing the data you're working on (or wiping a file)
	final DBModelData temporaryDataModel = new DBModelData();
	final DBModelPath temporaryPathModel = new DBModelPath();

	/******************************************************
	 ***************** LOAD TEMPORARY MODEL ****************
	 *****************************************************/

	protected void setTemporaryModel(String fileName, DBModelPath tempPath, DBModelData tempData) throws IOException {
		tempPath.setFilename(fileName);
		tempPath.setDatabaseName(storagePath.getDatabaseName());
		new DBLoad(tempData, storagePath.getDatabaseName(), fileName);
	}
}
