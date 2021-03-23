package DBMain.CommandFiles;
import DBMain.*;
import DBMain.ModelFiles.*;
import DBMain.ParseExceptions.DomainType;
import DBMain.ParseExceptions.InvalidCommand;
import DBMain.ParseExceptions.ParseExceptions;

public class CMDAlter extends CMDType {

	public void transformModel() throws ParseExceptions {
		String firstCommand = getTokenSafe(DomainType.TABLE);
		if (firstCommand.equalsIgnoreCase("TABLE")) {
			String secondCommand = getTokenSafe(DomainType.TABLENAME);
			if (doesTableExist(secondCommand)) {
				//split based on whether we're doing an 'add' or 'drop' operation
				addDropSplit();
			}
		} else {
			throw new InvalidCommand(firstCommand, "ALTER", "TABLE", null);
		}
	}

	private void addDropSplit() throws ParseExceptions{
		String nextCommand = getTokenSafe(DomainType.UNKNOWN);
		if(nextCommand.equalsIgnoreCase("ADD")){
			//finding if the attribute name would be valid
			String newAttributeName = getTokenSafe(DomainType.ATTRIBUTENAME);
			isItAlphNumTHROW(newAttributeName, DomainType.ATTRIBUTENAME);
			if(isItLineEndTHROW()){
				addColumn(newAttributeName);
			}
		} else if(nextCommand.equalsIgnoreCase("DROP")){
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
