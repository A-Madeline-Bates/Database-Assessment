package DBMain.CommandFiles;
import DBMain.*;
import DBMain.ModelFiles.*;
import DBMain.ParseExceptions.*;

public class CMDSelect extends CMDType {
	private String tableName;

	public void transformModel() throws ParseExceptions {
		//<Select>         ::=  SELECT <WildAttribList> FROM <TableName> |
		//						SELECT <WildAttribList> FROM <TableName> WHERE <Condition>
		String firstCommand = getNewTokenSafe(DomainType.ATTRIBUTENAME);
		if(canWeReadTable()){
			if(doesWildAttributeExist(firstCommand)){
				System.out.println("It exists!");
				//call getNewToken three times because we know we have the correct commands
				//then see if it's end or WHERE
			}
		}
	}

	private boolean canWeReadTable() throws ParseExceptions {
		//we need to know tablename before we can identify if our attribute is valid, so we start be 'peaking'
		//forwards at it.
		String peakOne = peakTokenSafe(1, DomainType.FROM);
		if(isItFROM(peakOne)){
			String peakTwo = peakTokenSafe(2, DomainType.FROM);
			if(doesTableExist(peakTwo)){
				tableName = peakTwo;
				return true;
			}
		}
		return false;
	}

	private boolean doesWildAttributeExist(String firstCommand) throws ParseExceptions{
		if(isItAsterisk(firstCommand)){
			return true;
		}
		else if(doesAttributeExist(firstCommand)){
			return true;
		}
		else{
			throw new DoesNotExistAttribute(firstCommand, tableName);
		}
	}

	private boolean doesAttributeExist(String firstCommand){
		//create temporaryModel so that we can see if there are any attribute matches with our command
		DBModelData temporaryModel = new DBModelData();
		new DBLoad(temporaryModel, pathModel.getDatabaseName(), tableName);
		//iterate through the columns of our table until we find a match
		for(int i=0; i<temporaryModel.getColumnNumber(); i++){
			if(temporaryModel.getColumnData().get(i).equalsIgnoreCase(firstCommand)){
				return true;
			}
		}
		return false;
	}

	private boolean isItAsterisk(String nextCommand){
		if (nextCommand.equals("*")) {
			return true;
		}
		return false;
	}

	private boolean isItFROM(String nextCommand) throws ParseExceptions{
		if (nextCommand.equalsIgnoreCase("FROM")) {
			return true;
		}
		throw new InvalidCommand(nextCommand, "SELECT [attribute]", "FROM", null);
	}

	public String query(DBServer server){
		return "Select";
	}
}
