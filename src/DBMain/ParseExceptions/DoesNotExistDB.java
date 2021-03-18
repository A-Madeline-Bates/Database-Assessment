package DBMain.ParseExceptions;

public class DoesNotExistDB extends ParseExceptions{
	public DoesNotExistDB(String token) {
		this.token = token;
	}

	public String toString(){
		return "Invalid database. Database called does not exist. \nDatabase name given was " + token + ".";
	}
}
