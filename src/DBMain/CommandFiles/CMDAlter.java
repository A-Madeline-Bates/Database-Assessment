package DBMain.CommandFiles;
import DBMain.DBLoad.DBLoad;
import DBMain.DBTokeniser.DBTokeniser;
import DBMain.ModelFiles.DBModelPath;
import DBMain.ParseExceptions.DomainType;
import DBMain.ParseExceptions.InvalidCommand;
import DBMain.ParseExceptions.ParseExceptions;
import java.io.IOException;

public class CMDAlter extends AttributeSearch {

	public CMDAlter(DBTokeniser tokeniser, DBModelPath path) throws IOException, ParseExceptions {
		super(tokeniser, path);
	}

	public void transformModel() throws ParseExceptions, IOException{
		String firstCommand = getTokenSafe(DomainType.TABLE);
		if (stringMatcherTHROW("TABLE", firstCommand, "ALTER")) {
			String secondCommand = getTokenSafe(DomainType.TABLENAME);
			if (doesTableExist(secondCommand)) {
				//split based on whether we're doing an 'add' or 'drop' operation
				new DBLoad(storageColumns, storageRows, storagePath.getDatabaseName(), secondCommand);
				storagePath.setFilename(secondCommand);
				addDropSplit();
				setExitMessage();
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
			int attributeCoordinate = findAttributeTHROW(attributeCommand, storagePath, storageColumns);
			protectIDCol(attributeCoordinate);
			if(isItLineEndTHROW()) {
				dropColumn(attributeCoordinate);
			}
		} else{
			throw new InvalidCommand(nextCommand, "ALTER TABLE [tablename]", "ADD", "DROP");
		}
	}

	private void addColumn(String attributeName){
		storageRows.addColumn(attributeName);
	}

	private void dropColumn(int attributeCoordinate){
		storageRows.deleteColumn(attributeCoordinate);
	}
}
