package DBMain.ParseExceptions;

public class DoesNotExistTable extends ParseExceptions{
	public DoesNotExistTable(String token) {
		this.token = token;
	}

	public String toString(){
		return "Table not found. This may be because the table does not exist,\n" +
		"or because we are in the wrong database to access it. Table name given was " + token + ".";
	}
}
