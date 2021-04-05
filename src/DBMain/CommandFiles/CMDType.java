package DBMain.CommandFiles;
import DBMain.DBTokeniser.DBTokeniser;
import DBMain.ModelFiles.*;
import DBMain.ParseExceptions.*;
import java.io.IOException;

public abstract class CMDType {
	//these are the models that will get 'stored' by DBStore at the end of handleIncomingCommand
	final DBModelColumns storageColumns;
	protected DBModelRows storageRows;
	protected DBModelPath storagePath;
	protected String exitMessage;
	protected DBTokeniser tokeniser;

	public CMDType(DBTokeniser tokeniser, DBModelPath path) throws ParseExceptions, IOException{
		this.storageColumns = new DBModelColumns();
		this.tokeniser = tokeniser;
		this.storagePath = path;
		transformModel();
	}

	public DBModelRows getRowsData() {
		return storageRows;
	}

	/******************************************************
	 *************** MODEL ALTERING METHODS ***************
	 *****************************************************/

	public DBModelColumns getColumnsData() {
		return storageColumns;
	}

	public DBModelPath getStoragePath() {
		return storagePath;
	}

	public abstract void transformModel() throws ParseExceptions, IOException;

	protected void setExitMessage() {
		this.exitMessage = "";
	}

	public String getExitMessage() {
		return exitMessage;
	}
}