package DBMain.ParseExceptions;

public class NotBuiltDB extends ParseExceptions{
	public NotBuiltDB(String token) {
		this.token = token;
	}

	public String toString(){
		return "Database could not be created. This is probably because the database \n" +
				"specified already exists. Database name given was " + token + ".";
	}
}
