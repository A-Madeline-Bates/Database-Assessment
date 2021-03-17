package DBMain.ParseExceptions;

public class OutsideDatabaseErr extends ParseExceptions{

	public String toString(){
		return "Trying to perform a table operation without a database specified. Please use USE or CREATE " +
				"commands to specify a database.";
	}
}
