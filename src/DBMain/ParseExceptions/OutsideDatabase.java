package DBMain.ParseExceptions;

public class OutsideDatabase extends FileSystemError {

	public String toString(){
		return "Trying to perform a table operation without a database specified. \nPlease use USE or CREATE " +
				"commands to specify a database.";
	}
}
