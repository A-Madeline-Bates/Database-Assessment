package DBMain.ParseExceptions;

public class NoDBFound extends TableUseError {
	public NoDBFound(String token) {
		this.token = token;
	}

	public String toString(){
		return "Invalid database. Database called does not exist. \nDatabase name given was " + token + ".";
	}
}
