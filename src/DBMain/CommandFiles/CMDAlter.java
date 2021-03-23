package DBMain.CommandFiles;
import DBMain.*;
import DBMain.ModelFiles.*;
import DBMain.ParseExceptions.DomainType;
import DBMain.ParseExceptions.InvalidCommand;
import DBMain.ParseExceptions.ParseExceptions;

public class CMDAlter extends CMDType {

	public void transformModel() throws ParseExceptions {
		String firstCommand = getTokenSafe(DomainType.TABLE);
		if (stringMatcherTHROW("TABLE", firstCommand, "ALTER")) {
			String secondCommand = getTokenSafe(DomainType.TABLENAME);
			if (doesTableExist(secondCommand)) {
				//split based on whether we're doing an 'add' or 'drop' operation
				addDropSplit();
			}
		}
	}

	private void addDropSplit() throws ParseExceptions{
		String nextCommand = getTokenSafe(DomainType.UNKNOWN);
		if(stringMatcher("ADD", nextCommand)){
			//finding if the attribute name would be valid
			String newAttributeName = getTokenSafe(DomainType.ATTRIBUTENAME);
			isItAlphNumTHROW(newAttributeName, DomainType.ATTRIBUTENAME);
			if(isItLineEndTHROW()){
				addColumn(newAttributeName);
			}
		} else if(stringMatcher("DROP", nextCommand)){
			//searching for preexisting attributes
			String attributeCommand = getTokenSafe(DomainType.ATTRIBUTENAME);
			int attributeCoordinate = findAttributeTHROW(attributeCommand);
			if(isItLineEndTHROW()) {
				dropColumn(attributeCoordinate);
			}
		} else{
			throw new InvalidCommand(nextCommand, "ALTER TABLE [tablename]", "ADD", "DROP");
		}
	}

	//check this works!
	private void addColumn(String attributeName){
		storageData.addColumn(attributeName);
	}

	//check this works!
	private void dropColumn(int attributeCoordinate){
		storageData.deleteColumn(attributeCoordinate);
	}

	public String query(DBServer server){
		return "Alter";
	}
}
