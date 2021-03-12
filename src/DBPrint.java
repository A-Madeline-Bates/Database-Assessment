import java.util.ArrayList;

public class DBPrint {
	DBModel model;
	//this currently returns an arraylist and not a string. It might be best to refactor
	//this code as interface which can deal with some of the select commands as well?

	public DBPrint(DBModel model) {
		this.model = model;
	}

	public ArrayList<String> stringifyTable() {
		ArrayList<String> PrintString = new ArrayList<String>();
		stringifyColumnNames(PrintString);
		stringifyDataArray(PrintString);
		return PrintString;
	}

	private void stringifyColumnNames(ArrayList<String> PrintString){
		for (int k = 0; k < model.getColumnNumber(); k++) {
			PrintString.add(model.getAllColNames().get(k) + "\t\t\t");
		}
	}

	private void stringifyDataArray(ArrayList<String> PrintString){
		for (int i = 0; i < (model.getRowNumber() + 1); i++) {
			for (int j = 0; j < model.getColumnNumber(); j++) {
				PrintString.add(model.getDataArray().get(i).get(j) + "\t\t\t");
			}
			//Write a new line for every new row
			PrintString.add("\n");
		}
	}
}
