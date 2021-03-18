package DBMain.ParseExceptions;

public class DBDoesNotExist extends ParseExceptions{
	public DBDoesNotExist(String token) {
		this.token = token;
	}

	public String toString(){
		return "Invalid database. Database called does not exist. \nDatabase name given was " + token + ".";
	}
}
