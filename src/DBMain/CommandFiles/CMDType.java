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
	//these are the models that will get 'stored' by DBStore at the end of handleIncomingCommand
	protected DBModelData storageData;
	protected DBModelPath storagePath;
	//these are 'temporary' versions of the model that can be used to check data without running the risk of
	//storing the data you're working on (or wiping a file)
	protected DBModelData temporaryDataModel = new DBModelData();
	protected DBModelPath temporaryPathModel = new DBModelPath();

	protected DBTokeniser tokeniser;

	public abstract String query(DBServer server);

	/******************************************************
	 *************** MODEL ALTERING METHODS ***************
	 *****************************************************/

	//most data is cleared because it minimises the possibility of mistakes and overwrites
	public void setModel(DBModelPath storagePath){
		this.storageData = new DBModelData();
		this.storagePath = storagePath;
		storagePath.setFilename(null);
	}

	public DBModelData getDataForStorage(){
		return storageData;
	}

	public DBModelPath getPathForStorage(){
		return storagePath;
	}

	protected void clearFilePath() {
		this.storageData = new DBModelData();
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

	protected String peakTokenSafe(int lookForward, DomainType domain) throws ParseExceptions{
		String tokenPreview = tokeniser.peakToken(lookForward);
		if(tokenPreview != null){
			return tokenPreview;
		}
		else{
			throw new CommandMissing(domain);
		}
	}

	/******************************************************
	 *************** DIRECTORY SEARCH TEST ****************
	 *****************************************************/

	protected boolean isDBValid(String dbName) throws ParseExceptions{
		//shouldn't actually need to test alphanum if database exists?
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

	protected boolean isItCommaSeparated(DomainType domain, String exitToken) throws ParseExceptions{
		String nextCommand = peakTokenSafe(1, DomainType.COMMA);
		//is the next instruction a comma
		if(isItComma(nextCommand)){
			//call nextToken so that our array position steps forward by one
			tokeniser.nextToken();
			return true;
		}
		//if next instruction is the exit token a comma is not necessary
		else if(nextCommand.equalsIgnoreCase(exitToken)){
			return true;
		}
		else{
			throw new NotCommaSeparated(domain);
		}
	}

	protected boolean isItComma(String nextCommand){
		if (nextCommand.equals(",")) {
			return true;
		}
		return false;
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
		if(storagePath.getDatabaseName() != null){
			return true;
		}
		else{
			throw new WorkingOutsideDatabase();
		}
	}

	protected boolean doesTableExist(String tableName) throws ParseExceptions {
		String currentDatabase = storagePath.getDatabaseName();
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

	protected boolean isItStringLiteralTHROW(String nextInstruction) throws ParseExceptions{
		if(isItStringLiteral(nextInstruction)){
			return true;
		}
		throw new InvalidValueType(nextInstruction, OperatorType.STRING);
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

	protected boolean isItNumLiteralTHROW(String nextInstruction) throws ParseExceptions{
		if(isItIntegerLiteral(nextInstruction) || isItFloatLiteral(nextInstruction)){
			return true;
		}
		throw new InvalidValueType(nextInstruction, OperatorType.NUMERICAL);
	}
}
