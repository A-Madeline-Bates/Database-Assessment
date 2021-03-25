package DBMain.CommandFiles;
import DBMain.*;
import DBMain.ModelFiles.*;
import DBMain.ParseExceptions.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.File;

public abstract class CMDType {
	//these are the models that will get 'stored' by DBStore at the end of handleIncomingCommand
	protected DBModelData storageData;
	protected DBModelPath storagePath;
	//these are 'temporary' versions of the model that can be used to check data without running the risk of
	//storing the data you're working on (or wiping a file)
	protected DBModelData temporaryDataModel = new DBModelData();
	protected DBModelPath temporaryPathModel = new DBModelPath();
	String exitMessage;

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
		this.storageData = new DBModelData();
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
//
//	protected void setTemporaryData(DBModelPath data) throws IOException {
//		new DBLoad(data, storagePath.getDatabaseName(), data.getFilename());
//	}

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

	protected String getTokenSafe(DomainType domain) throws ParseExceptions {
		String nextToken = tokeniser.nextToken();
		if (nextToken != null) {
			return nextToken;
		} else {
			throw new CommandMissing(domain);
		}
	}

	protected String peakTokenSafe(int lookForward, DomainType domain) throws ParseExceptions {
		String tokenPreview = tokeniser.peakToken(lookForward);
		if (tokenPreview != null) {
			return tokenPreview;
		} else {
			throw new CommandMissing(domain);
		}
	}

	/******************************************************
	 ******************** STRING TESTS ********************
	 *****************************************************/

	protected boolean isItAlphNumeric(String testString) {
		if (testString.matches("[a-zA-Z0-9]+")) {
			return true;
		} else {
			return false;
		}
	}

	protected boolean isItAlphNumTHROW(String testString, DomainType domain) throws ParseExceptions {
		if (isItAlphNumeric(testString)) {
			return true;
		}
		throw new AlphanumFormatProblem(testString, domain);
	}

	protected boolean isItLineEndTHROW() throws ParseExceptions {
		String finalCommand = getTokenSafe(DomainType.SEMICOLON);
		if (isItSemicolonTHROW(finalCommand)) {
			String extraCommand = tokeniser.nextToken();
			if (isItNullEndTHROW(extraCommand)) {
				return true;
			}
		}
		return false;
	}

	protected boolean isItNullEnd(String extraCommand) {
		if (extraCommand == null) {
			return true;
		}
		return false;
	}

	protected boolean isItNullEndTHROW(String extraCommand) throws ParseExceptions {
		if (isItNullEnd(extraCommand)) {
			return true;
		}
		throw new ExtraCommandGiven(extraCommand);
	}

	protected boolean isItSemicolonTHROW(String nextCommand) throws ParseExceptions {
		if (stringMatcher(";", nextCommand)) {
			return true;
		}
		throw new MissingSemiColon(nextCommand);
	}

	protected boolean isItCommaSeparated(DomainType domain, String exitToken) throws ParseExceptions {
		String nextCommand = peakTokenSafe(1, DomainType.COMMA);
		//is the next instruction a comma
		if (stringMatcher(",", nextCommand)) {
			//call nextToken so that our array position steps forward by one
			tokeniser.nextToken();
			return true;
		}
		//if next instruction is the exit token a comma is not necessary
		else if (nextCommand.equalsIgnoreCase(exitToken)) {
			return true;
		} else {
			throw new NotCommaSeparated(domain);
		}
	}

	protected boolean stringMatcher(String commandNeeded, String nextCommand) {
		if (nextCommand.equalsIgnoreCase(commandNeeded)) {
			return true;
		}
		return false;
	}

	protected boolean stringMatcherTHROW(String commandNeeded, String nextCommand, String prevCommand) throws ParseExceptions {
		if (stringMatcher(commandNeeded, nextCommand)) {
			return true;
		}
		throw new InvalidCommand(nextCommand, prevCommand, commandNeeded, null);
	}

	/******************************************************
	 ******************** PATH TESTS ********************
	 *****************************************************/

	protected boolean doesDBExist(String dbName) throws ParseExceptions {
		String location = "databaseFiles" + File.separator + dbName;
		Path path = Paths.get(location);
		if (Files.exists(path) && Files.isDirectory(path)) {
			return true;
		} else {
			throw new DoesNotExistDB(dbName);
		}
	}

	protected boolean doesTableExist(String tableName) throws ParseExceptions {
		String currentDatabase = storagePath.getDatabaseName();
		String location = "databaseFiles" + File.separator + currentDatabase + File.separator + tableName;
		Path path = Paths.get(location);
		if (Files.exists(path) && Files.isRegularFile(path)) {
			return true;
		} else {
			throw new DoesNotExistTable(tableName);
		}
	}

	/******************************************************
	 ******************* VALID VALUE TEST *****************
	 *****************************************************/


	protected boolean isItValidValue(String nextInstruction) throws ParseExceptions {
		if (isItString(nextInstruction)
				|| isItBoolean(nextInstruction)
				|| isItFloat(nextInstruction)
				|| isItInteger(nextInstruction)) {
			return true;
		} else {
			throw new InvalidValue(nextInstruction);
		}
	}

	protected boolean isItString(String nextInstruction) {
		if (nextInstruction.startsWith("'") && nextInstruction.endsWith("'")) {
			return true;
		}
		return false;
	}

	protected boolean isItStringTHROW(String nextInstruction) throws ParseExceptions {
		if (isItString(nextInstruction)) {
			return true;
		}
		throw new InvalidValueType(nextInstruction, OperatorType.STRING);
	}

	protected boolean isItBoolean(String nextInstruction) {
		if (nextInstruction.equalsIgnoreCase("true") || nextInstruction.equalsIgnoreCase("false")) {
			return true;
		}
		return false;
	}

	protected boolean isItFloat(String nextInstruction) {
		try {
			Float.parseFloat(nextInstruction);
		} catch (NumberFormatException n) {
			return false;
		}
		return true;
	}

	protected boolean isItInteger(String nextInstruction) {
		try {
			Integer.parseInt(nextInstruction);
		} catch (NumberFormatException n) {
			return false;
		}
		return true;
	}

	protected boolean isItNumTHROW(String nextInstruction) throws ParseExceptions {
		if (isItInteger(nextInstruction) || isItFloat(nextInstruction)) {
			return true;
		}
		throw new InvalidValueType(nextInstruction, OperatorType.NUMERICAL);
	}

	/******************************************************
	 ****************** ATTRIBUTE METHODS ****************
	 *****************************************************/

	protected int findAttribute(String nextCommand, DBModelData data) {
		//iterate through the columns of our table until we find a match
		for (int i = 0; i < data.getColumnNumber(); i++) {
			if (data.getColumnData().get(i).equalsIgnoreCase(nextCommand)) {
				return i;
			}
		}
		return -1;
	}

	protected int findAttributeTHROW(String nextCommand, DBModelPath path, DBModelData data) throws ParseExceptions {
		int attributeCoordinate = findAttribute(nextCommand, data);
		if (attributeCoordinate >= 0) {
			return attributeCoordinate;
		} else {
			throw new DoesNotExistAttribute(nextCommand, path.getFilename(), null);
		}
	}


	/******************************************************
 	*************** PREVENT EDITING ID COL **************
 	*****************************************************/

	protected void protectIDCol(int colNum) throws ParseExceptions{
		if(colNum == 0){
			throw new EditingID();
		}
	}
}