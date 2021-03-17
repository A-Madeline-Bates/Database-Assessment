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
	 ******************** DB NAME TEST ********************
	 *****************************************************/

	protected boolean isDBValid(String dbName) throws ParseExceptions{
		if (isNameValid(dbName, DomainType.DATABASENAME)){
			if(databaseExists(dbName)){
				return true;
			}
		}
		return false;
	}

	/******************************************************
	 ******************** STRING TESTS ********************
	 *****************************************************/

	protected boolean isNameValid(String dbName, DomainType domain) throws ParseExceptions{
		if (tokenExists(dbName, DomainType.DATABASENAME)){
			if (isAlphNumeric(dbName, DomainType.DATABASENAME)){
				return true;
			}
		}
		return false;
	}

	private boolean tokenExists(String testString, DomainType domain) throws ParseExceptions{
		if(testString != null){
			return true;
		}
		else{
			throw new TokenDoesNotExistErr(domain);
		}
	}

	private boolean isAlphNumeric (String testString, DomainType domain) throws ParseExceptions{
		if(testString.matches("[a-zA-Z0-9]+")){
			return true;
		}
		else{
			throw new AlphanumErr(testString, domain);
		}
	}

	/******************************************************
	 ******************** PATH TESTS ********************
	 *****************************************************/

	private boolean databaseExists(String dbName) throws ParseExceptions{
		String location = "databaseFiles" + File.separator + dbName;
		Path path = Paths.get(location);
		if(Files.exists(path) && Files.isDirectory(path)){
			return true;
		}
		else {
			throw new DBDoesNotExistErr(dbName);
		}
	}
}
