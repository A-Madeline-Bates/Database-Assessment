package DBMain.CommandFiles;
import DBMain.DBTokeniser.DBTokeniser;
import DBMain.ModelFiles.*;
import DBMain.ParseExceptions.*;
import java.io.IOException;

public abstract class CMDType {
	//these are the models that will get 'stored' by DBStore at the end of handleIncomingCommand
	final DBModelTable storageData;
	protected DBModelPath storagePath;
	protected String exitMessage;
	protected DBTokeniser tokeniser;

	public CMDType(){
		this.storageData = new DBModelTable();
	}

	protected void buildCommand(DBTokeniser tokeniser, DBModelPath path) throws ParseExceptions, IOException{
		this.tokeniser = tokeniser;
		this.storagePath = path;
		transformModel();
	}

	/******************************************************
	 *************** MODEL ALTERING METHODS ***************
	 *****************************************************/

	public DBModelTable getStorageData() {
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