package DBMain.ParseExceptions;

public class DBDoesNotExistErr extends ParseExceptions{
	DomainType domain;

	public DBDoesNotExistErr(String token) {
		this.token = token;
	}

	public String toString(){
		return "Invalid database. Database called does not exist. Database name given was " + token + ".";
	}
}
