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

	public abstract void transformModel() throws ParseExceptions, IOException;

	protected void setExitMessage() {
		this.exitMessage = "";
	}

	public String getExitMessage() {
		return exitMessage;
	}

	/******************************************************
	 ****************** SET TOKENISER ********************
	 *****************************************************/

	public void setTokeniser(DBTokeniser tokeniser) {
		this.tokeniser = tokeniser;
	}

}