import ParseExceptions.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.File;

abstract class CMDType {
	protected DBModelData dataModel;
	protected DBModelPath pathModel;
	protected DBTokeniser tokeniser;

	//abstract class which defines all CMD classes
	abstract String query(DBServer server);

	protected void setModel(DBModelData dataModel, DBModelPath pathModel){
		this.dataModel = dataModel;
		this.pathModel = pathModel;
	}

	protected void clearModel(){
		this.dataModel = new DBModelData();
		this.pathModel = new DBModelPath();
	}

	abstract void transformModel() throws ParseExceptions;

	protected void setInstructionSet(DBTokeniser tokeniser){
		this.tokeniser = tokeniser;
	}

	protected boolean isDBNameValid(String dbName) throws ParseExceptions{
		if (tokenExists(dbName, DomainType.DATABASENAME)){
			if (isAlphNumeric(dbName, DomainType.DATABASENAME)){
				if(databaseExists(dbName)){
					return true;
				}
			}
		}
		return false;
	}

	private boolean tokenExists(String testString, DomainType domain) throws ParseExceptions{
		if(testString != null){
			return true;
		}
		else{
			throw new TokenDoesNotExistErr(domain);
		}
	}

	private boolean isAlphNumeric (String testString, DomainType domain) throws ParseExceptions{
		if(testString.matches("[a-zA-Z0-9]+")){
			return true;
		}
		else{
			throw new AlphanumErr(testString, domain);
		}
	}

	private boolean databaseExists(String dbName) throws ParseExceptions{
		String location = "databaseFiles" + File.separator + dbName;
		Path path = Paths.get(location);
		if(Files.exists(path) && Files.isDirectory(path)){
			return true;
		}
		else {
			throw new DBDoesNotExistErr(dbName);
		}
	}
}
