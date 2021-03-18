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

	public abstract void transformModel() throws ParseExceptions ;

	/******************************************************
	 ****************** SET TOKENISER ********************
	 *****************************************************/

	public void setInstructionSet(DBTokeniser tokeniser){
		this.tokeniser = tokeniser;
	}

	protected String getNewTokenSafe(DomainType domain) throws ParseExceptions{
		String nextToken = tokeniser.nextToken();
		if(nextToken != null){
			return nextToken;
		}
		else{
			throw new CommandMissing(domain);
		}
	}

	/******************************************************
	 *************** DIRECTORY SEARCH TEST ****************
	 *****************************************************/

	protected boolean isDBValid(String dbName) throws ParseExceptions{
		if (isNameAlphNumTHROW(dbName, DomainType.DATABASENAME)){
			if(doesDatabaseExist(dbName)){
				return true;
			}
		}
		return false;
	}

	/******************************************************
	 ******************** STRING TESTS ********************
	 *****************************************************/

//	protected boolean doesTokenExist(String testString){
//		if(testString != null){
//			return true;
//		}
//		else{
//			return false;
//		}
//	}
//
//	protected boolean doesTokenExistTHROW(String testString, DomainType domain) throws ParseExceptions{
//		if(doesTokenExist(testString)){
//			return true;
//		}
//		throw new CommandMissing(domain);
//	}

	protected boolean isNameAlphNumeric (String testString){
		if(testString.matches("[a-zA-Z0-9]+")){
			return true;
		}
		else{
			return false;
		}
	}

	protected boolean isNameAlphNumTHROW (String testString, DomainType domain) throws ParseExceptions{
		if(isNameAlphNumeric(testString)){
			return true;
		}
		throw new AlphanumFormatProblem(testString, domain);
	}


	protected boolean isThisCommandLineEnd() throws ParseExceptions{
		String finalCommand = getNewTokenSafe(DomainType.SEMICOLON);
		if(isThisSemicolonTHROW(finalCommand)) {
			String extraCommand = tokeniser.nextToken();
			if (isThisCommandEndTHROW(extraCommand)) {
				return true;
			}
		}
		return false;
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
		throw new ExtraCommandGiven(extraCommand);
	}

	protected boolean isThisSemicolon(String nextCommand){
		if (nextCommand.equals(";")) {
			return true;
		}
		return false;
	}

	protected boolean isThisSemicolonTHROW(String nextCommand) throws ParseExceptions{
		if(isThisSemicolon(nextCommand)){
			return true;
		}
		throw new MissingSemiColon(nextCommand);
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
			throw new DBDoesNotExist(dbName);
		}
	}

	protected boolean areWeInADatabase() throws ParseExceptions {
		if(pathModel.getDatabaseName() != null){
			return true;
		}
		else{
			throw new WorkingOutsideDatabase();
		}
	}
}
