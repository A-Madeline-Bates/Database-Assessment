package DBMain.CommandFiles;
import DBMain.*;
import DBMain.ModelFiles.*;
import DBMain.ParseExceptions.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.File;
import java.util.regex.Pattern;

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
			throw new DoesNotExistDB(dbName);
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

	protected boolean doesTableExist(String tableName) throws ParseExceptions {
		String currentDatabase = pathModel.getDatabaseName();
		String location = "databaseFiles" + File.separator + currentDatabase + File.separator + tableName;
		Path path = Paths.get(location);
		if(Files.exists(path) && Files.isRegularFile(path)){
			return true;
		}
		else {
			throw new DoesNotExistTable(tableName);
		}
	}

	/******************************************************
	 ******************* VALID VALUE TEST *****************
	 *****************************************************/


	protected boolean isItValidValue(String nextInstruction) throws ParseExceptions{
		if(isItStringLiteral(nextInstruction)){
			return true;
		} else if(isItBooleanLiteral(nextInstruction)){
			return true;
		} else if (isItFloatLiteral(nextInstruction)){
			return true;
		} else if (isItIntegerLiteral(nextInstruction)){
			return true;
		}
		else{
			throw new InvalidValue(nextInstruction);
		}
	}

	protected boolean isItStringLiteral(String nextInstruction){
		if(nextInstruction.startsWith("'") && nextInstruction.endsWith("'")){
			return true;
		}
		return false;
	}

	protected boolean isItBooleanLiteral(String nextInstruction){
		if(nextInstruction.equalsIgnoreCase("true") || nextInstruction.equalsIgnoreCase("false")) {
			return true;
		}
		return false;
	}

	protected boolean isItFloatLiteral(String nextInstruction){
		try{
			Float.parseFloat(nextInstruction);
		}
		catch(NumberFormatException n){
			return false;
		}
		return true;
	}

	protected boolean isItIntegerLiteral(String nextInstruction){
		try{
			Integer.parseInt(nextInstruction);
		}
		catch(NumberFormatException n){
			return false;
		}
		return true;
	}
}
