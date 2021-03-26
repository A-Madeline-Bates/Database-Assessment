package DBMain.CommandFiles;
import DBMain.DBLoad.DBLoad;
import DBMain.DBTokeniser.DBTokeniser;
import DBMain.ModelFiles.*;
import DBMain.ParseExceptions.*;
import java.io.IOException;

public abstract class CMDType {
	//these are the models that will get 'stored' by DBStore at the end of handleIncomingCommand
	protected DBModelData storageData;
	protected DBModelPath storagePath;
	//these are 'temporary' versions of the model that can be used to check data without running the risk of
	//storing the data you're working on (or wiping a file)
	final DBModelData temporaryDataModel = new DBModelData();
	final DBModelPath temporaryPathModel = new DBModelPath();
	protected String exitMessage;

	protected DBTokeniser tokeniser;

	/******************************************************
	 *************** MODEL ALTERING METHODS ***************
	 *****************************************************/

	//most data is cleared because it minimises the possibility of mistakes and overwrites
	public void setModel(DBModelPath storagePath) {
		this.storageData = new DBModelData();
		this.storagePath = storagePath;
		storagePath.setFilename(null);
	}

	public DBModelData getStorageData() {
		return storageData;
	}

	public DBModelPath getStoragePath() {
		return storagePath;
	}

	protected void clearFilePath() {
		this.storagePath = new DBModelPath();
	}

	public abstract void transformModel() throws ParseExceptions, IOException;

	protected void setExitMessage() {
		this.exitMessage = "";
	}

	public String getExitMessage() {
		return exitMessage;
	}

	/******************************************************
	 ***************** LOAD TEMPORARY MODEL ****************
	 *****************************************************/

	protected void setTemporaryModel(String fileName, DBModelPath tempPath, DBModelData tempData) throws IOException {
		tempPath.setFilename(fileName);
		tempPath.setDatabaseName(storagePath.getDatabaseName());
		new DBLoad(tempData, storagePath.getDatabaseName(), fileName);
	}

	/******************************************************
	 ****************** SET TOKENISER ********************
	 *****************************************************/

	public void setTokeniser(DBTokeniser tokeniser) {
		this.tokeniser = tokeniser;
	}

}