package DBMain.ParseExceptions;

public class EditingID extends ParseExceptions{

	public String toString(){
		return "Trying to edit ID column. Editing or deleting the table's ID column is not allowed.";
	}
}
