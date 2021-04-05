package DBMain.CommandFiles;
import DBMain.DBLoad.DBLoad;
import DBMain.DBTokeniser.DBTokeniser;
import DBMain.ModelFiles.DBModelColumns;
import DBMain.ModelFiles.DBModelPath;
import DBMain.ModelFiles.DBModelRows;
import DBMain.ParseExceptions.ParseExceptions;

import java.io.IOException;

public abstract class TemporaryModel extends MainDataClasses{
	//these are 'temporary' versions of the model that can be used to check data without running the risk of
	//storing the data you're working on (or wiping a file)
	final DBModelColumns temporaryColumns = new DBModelColumns();
	final DBModelRows temporaryRows = new DBModelRows();
	final DBModelPath temporaryPathModel = new DBModelPath();

	public TemporaryModel(DBTokeniser tokeniser, DBModelPath path) throws IOException, ParseExceptions {
		super(tokeniser, path);
	}

	/******************************************************
	 ***************** LOAD TEMPORARY MODEL ****************
	 *****************************************************/

	protected void setTemporaryModel(String fileName, DBModelPath path, DBModelColumns cols, DBModelRows rows) throws IOException {
		path.setFilename(fileName);
		path.setDatabaseName(storagePath.getDatabaseName());
		new DBLoad(cols, rows, storagePath.getDatabaseName(), fileName);
	}
}
