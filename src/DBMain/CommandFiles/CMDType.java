package DBMain.CommandFiles;
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

	public CMDType(){
		this.storageData = new DBModelData();
	}

	/******************************************************
	 *************** MODEL ALTERING METHODS ***************
	 *****************************************************/

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
}