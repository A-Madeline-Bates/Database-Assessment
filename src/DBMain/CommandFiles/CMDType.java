package DBMain.CommandFiles;
import DBMain.*;
import DBMain.ModelFiles.*;
import DBMain.ParseExceptions.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.File;

public abstract class CMDType {
	protected DBModelData dataModel;
	protected DBModelPath pathModel;
	protected DBTokeniser tokeniser;

	//SPLIT THIS INTO DIFFERENT CHILD CLASSES WHEN WE KNOW WHAT COMMANDS WILL NEED WHAT THINGS

	public abstract String query(DBServer server);

	/******************************************************
	 *************** MODEL ALTERING METHODS ***************
	 *****************************************************/

	public void setModel(DBModelData dataModel, DBModelPath pathModel){
		this.dataModel = dataModel;
		this.pathModel = pathModel;
	}

	public DBModelData getModelData(){
		return dataModel;
	}

	public DBModelPath getModelPath(){
		return pathModel;
	}

	protected void clearModel(){
		this.dataModel = new DBModelData();
		this.pathModel = new DBModelPath();
	}

	public abstract void transformModel() throws ParseExceptions;

	/******************************************************
	 ****************** SET TOKENISER ********************
	 *****************************************************/

	public void setInstructionSet(DBTokeniser tokeniser){
		this.tokeniser = tokeniser;
	}

	/******************************************************
	 *************** DIRECTORY SEARCH TEST ****************
	 *****************************************************/

	protected boolean isDBValid(String dbName) throws ParseExceptions{
		if (isNameValid(dbName, DomainType.DATABASENAME)){
			if(doesDatabaseExist(dbName)){
				return true;
			}
		}
		return false;
	}

	/******************************************************
	 ******************** STRING TESTS ********************
	 *****************************************************/

	protected boolean isNameValid(String dbName, DomainType domain) throws ParseExceptions{
		if (doesTokenExist(dbName, domain)){
			if (isItAlphNumeric(dbName, domain)){
				return true;
			}
		}
		return false;
	}

	protected boolean doesTokenExist(String testString, DomainType domain) throws ParseExceptions{
		if(testString != null){
			return true;
		}
		else{
			throw new CommandMissingErr(domain);
		}
	}

	protected boolean isItAlphNumeric (String testString, DomainType domain) throws ParseExceptions{
		if(testString.matches("[a-zA-Z0-9]+")){
			return true;
		}
		else{
			throw new AlphanumErr(testString, domain);
		}
	}

	protected boolean isThisCommandEnd(String extraCommand){
		if(extraCommand == null){
			return true;
		}
		return false;
	}

	protected boolean isThisCommandEndTHROW(String extraCommand) throws ParseExceptions{
		if(isThisCommandEnd(extraCommand)){
			return true;
		}
		throw new ExtraCommandErr(extraCommand);
	}

	/******************************************************
	 ******************** PATH TESTS ********************
	 *****************************************************/

	private boolean doesDatabaseExist(String dbName) throws ParseExceptions{
		String location = "databaseFiles" + File.separator + dbName;
		Path path = Paths.get(location);
		if(Files.exists(path) && Files.isDirectory(path)){
			return true;
		}
		else {
			throw new DBDoesNotExistErr(dbName);
		}
	}

	protected boolean areWeInADatabase() throws ParseExceptions {
		if(pathModel.getDatabaseName() != null){
			return true;
		}
		else{
			throw new OutsideDatabaseErr();
		}
	}
}
