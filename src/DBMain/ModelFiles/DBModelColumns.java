package DBMain.ModelFiles;

import java.util.ArrayList;
import java.util.Arrays;

public class DBModelColumns extends DBModel{

	public void setColumnsFromFile(String command) {
		columnNames.addAll(Arrays.asList(command.split("\\t")));
		columnNames.removeAll(Arrays.asList("", null));
	}

	public void setColumnsFromSQL(ArrayList<String> newColNames) {
		//adding an ID column
		columnNames.add("ID");
		columnNames.addAll(newColNames);
	}

	public void setColumnsSQLNoID(ArrayList<String> newColNames) {
		columnNames.addAll(newColNames);
	}

	public ArrayList<String> getColumnData() {
		return columnNames;
	}

	//By finding the number of column headers, we can see how many columns we have
	public int getColumnNumber() {
		return columnNames.size();
	}

	public String getColumnAttribute(int i) {
		return columnNames.get(i);
	}
}
