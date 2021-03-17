package DBMain.CommandFiles;
import DBMain.*;
import DBMain.ModelFiles.*;
import DBMain.ParseExceptions.*;

public class CMDCreate extends CMDType {
	public String query(DBServer server){
		return "Create";
	}

	public void transformModel() throws ParseExceptions {
		String nextToken = tokeniser.nextToken();
		if(nextToken.toUpperCase() == "DATABASE"){
			//nextToken is updated to contain the word following DATABASE
			nextToken = tokeniser.nextToken();
			createDatabase(nextToken.toUpperCase(), DomainType.DATABASENAME);
		}
		else if(nextToken.toUpperCase() == "TABLE"){
			//nextToken is updated to contain the word following TABLE
			nextToken = tokeniser.nextToken();
			//this is split into more stages to accomodate recursively searching for attributes
			if(isNameValid(nextToken.toUpperCase(), DomainType.TABLENAME)){
				//code here to check attribute list stuff
				//and then createTable()
			}
		}
		else{
			//If token was null it would call this err and display null, which isn't quite right
			throw new InvalidTokenError(nextToken, "CREATE", "DATABASE", "TABLE");
		}
	}

	private void createDatabase(String dbName, DomainType domain) throws ParseExceptions{
		if(isNameValid(dbName, domain)) {
			//if(isFinalSymbol{
				//insert code to create new directory in our folder.
			//}
		}
	}
}
