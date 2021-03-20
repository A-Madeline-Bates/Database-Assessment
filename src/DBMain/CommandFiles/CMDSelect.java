package DBMain.CommandFiles;
import DBMain.*;
import DBMain.ModelFiles.*;
import DBMain.ParseExceptions.*;

import java.util.ArrayList;

public class CMDSelect extends CMDType {
	private String tableName;
	private ArrayList<Integer> requestedColumns = new ArrayList<>();

	public void transformModel() throws ParseExceptions {
		//<Select>         ::=  SELECT <WildAttribList> FROM <TableName> |
		//						SELECT <WildAttribList> FROM <TableName> WHERE <Condition>
		String firstCommand = getNewTokenSafe(DomainType.ATTRIBUTENAME);
		if(canWeReadTable()){
			if(doesWildAttributeExist(firstCommand)){
				System.out.println("It exists!" + requestedColumns);
				//call getNewToken three times because we know we have the correct commands
				//then see if it's end or WHERE
			}
		}
	}

	private boolean canWeReadTable() throws ParseExceptions {
		//we need to know tablename before we can identify if our attribute is valid, so we start by 'peaking'
		//forwards at it.
		for(int i=1; ; i++) {
			//we search the command line for FROM- if we hit the end of the line without finding it,
			//peakTokenSafe will throw an error
			String peakOne = peakTokenSafe(i, DomainType.FROM);
			System.out.println(peakOne);
			if (isItFrom(peakOne)) {
				System.out.println("*" + peakOne);
				//if we find FROM, check if the next command is a valid table name. If it isn't,
				//doesTableExist will throw an error
				String peakTwo = peakTokenSafe(i+1, DomainType.TABLENAME);
				System.out.println(peakTwo);
				if (doesTableExist(peakTwo)) {
					tableName = peakTwo;
					return true;
				}
			}
		}
	}

	private boolean doesWildAttributeExist(String firstCommand) throws ParseExceptions{
		if(isItAsterisk(firstCommand)){
			String nextCommand = getNewTokenSafe(DomainType.ATTRIBUTENAME);
			//check that it's an Asterisk followed immediately by a FROM
			if (isItFrom(nextCommand)) {
				requestAllColumns();
			}
			return true;
		}
		else if(doesAttributeExist(firstCommand)){
			isItCommaSeparated(DomainType.ATTRIBUTENAME, "FROM");
			doesAttrExistRecursive();
			return true;
		}
		else{
			throw new DoesNotExistAttribute(firstCommand, tableName);
		}
	}

	private void doesAttrExistRecursive() throws ParseExceptions{
		String nextCommand = getNewTokenSafe(DomainType.ATTRIBUTENAME);
		//if it's a 'FROM', leave recursive loop and update table
		if (isItFrom(nextCommand)) {
			return;
		}
		//if it fits the conditions to be a attribute, call doesAttrExistRecursive again
		//doesAttrExistRecursive will automatically save the coordinate if it is valid
		else if (doesAttributeExist(nextCommand)){
			if(isItCommaSeparated(DomainType.ATTRIBUTENAME, "FROM")) {
				doesAttrExistRecursive();
			}
		}
		//if it's not a value or a 'FROM', throw an error
		else{
			throw new DoesNotExistAttribute(nextCommand, tableName);
		}
	}

	private boolean doesAttributeExist(String nextCommand){
		//create temporaryModel so that we can see if there are any attribute matches with our command
		DBModelData temporaryModel = new DBModelData();
		new DBLoad(temporaryModel, pathModel.getDatabaseName(), tableName);
		//iterate through the columns of our table until we find a match
		for(int i=0; i<temporaryModel.getColumnNumber(); i++){
			if(temporaryModel.getColumnData().get(i).equalsIgnoreCase(nextCommand)){
				//if we get an attribute match, save the column coordinate where the match occurred
				requestedColumns.add(i);
				return true;
			}
		}
		return false;
	}

	private void requestAllColumns(){
		//see if there is a more efficient way of doing this! This is also not DRY.

		//create temporaryModel so that we can see how many columns we have
		DBModelData temporaryModel = new DBModelData();
		new DBLoad(temporaryModel, pathModel.getDatabaseName(), tableName);
		//iterate through the columns of our table until we find a match
		for(int i=0; i<temporaryModel.getColumnNumber(); i++){
			requestedColumns.add(i);
		}
	}


	private boolean isItAsterisk(String nextCommand){
		if (nextCommand.equals("*")) {
			return true;
		}
		return false;
	}

	private boolean isItFrom(String nextCommand) throws ParseExceptions{
		if (nextCommand.equalsIgnoreCase("FROM")) {
			return true;
		}
		return false;
	}

	public String query(DBServer server){
		return "Select";
	}
}
