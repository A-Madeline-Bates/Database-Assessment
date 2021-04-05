package DBMain.CommandFiles;

import DBMain.DBEnums.DomainType;
import DBMain.ParseExceptions.*;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public abstract class InputTests extends CMDType {

	/******************************************************
	 ******************** TOKEN METHODS *******************
	 *****************************************************/

	protected String getTokenSafe(DomainType domain) throws CommandMissing {
		String nextToken = tokeniser.nextToken();
		if (nextToken != null) {
			return nextToken;
		} else {
			throw new CommandMissing(domain);
		}
	}

	protected String peakTokenSafe(int lookForward, DomainType domain) throws CommandMissing {
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

	protected boolean isItAlphNumTHROW(String testString, DomainType domain) throws AlphanumFormatProblem {
		if (isItAlphNumeric(testString)) {
			return true;
		}
		throw new AlphanumFormatProblem(testString, domain);
	}

	protected boolean isItLineEndTHROW() throws CommandMissing, MissingSemiColon, ExtraCommandGiven {
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

	protected boolean isItNullEndTHROW(String extraCommand) throws ExtraCommandGiven {
		if (isItNullEnd(extraCommand)) {
			return true;
		}
		throw new ExtraCommandGiven(extraCommand);
	}

	protected boolean isItSemicolonTHROW(String nextCommand) throws MissingSemiColon {
		if (stringMatcher(";", nextCommand)) {
			return true;
		}
		throw new MissingSemiColon(nextCommand);
	}

	protected boolean isItCommaSeparated(DomainType domain, String exitToken) throws CommandMissing, NotCommaSeparated {
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

	protected boolean stringMatcherTHROW(String commandNeeded, String nextCommand, String prevCommand) throws InvalidCommand {
		if (stringMatcher(commandNeeded, nextCommand)) {
			return true;
		}
		throw new InvalidCommand(nextCommand, prevCommand, commandNeeded, null);
	}

	/******************************************************
	 ******************** PATH TESTS ********************
	 *****************************************************/

	protected boolean doesTableExist(String tableName) throws NoTableFound {
		String currentDatabase = storagePath.getDatabaseName();
		String location = "databaseFiles" + File.separator + currentDatabase + File.separator + tableName;
		Path path = Paths.get(location);
		if (Files.exists(path) && Files.isRegularFile(path)) {
			return true;
		} else {
			throw new NoTableFound(tableName);
		}
	}

	/******************************************************
	 *************** PREVENT EDITING ID COL **************
	 *****************************************************/

	protected void protectIDCol(int colNum) throws EditingID{
		if(colNum == 0){
			throw new EditingID();
		}
	}
}
