package DBMain.ModelFiles;
import java.util.*;

public class DBModelData extends DBModel {
	//Should need a method to return column coordinate by name

	public void setColumnDataFromLoad(String command) {
		columnNames.addAll(Arrays.asList(command.split("\\t")));
		columnNames.removeAll(Arrays.asList("", null));
	}

	//This should only ever be called after clearing the model
	public void setColumnDataFromSQL(ArrayList<String> newColNames) {
		//adding an ID column
		columnNames.add("ID");
		columnNames.addAll(newColNames);
	}

	public ArrayList<String> getColumnData() {
		return columnNames;
	}

	//By finding the number of column headers, we can see how many columns we have
	public int getColumnNumber() {
		return columnNames.size();
	}

	public void setRowsDataFromLoad(String command) {
		//setDataArray() is called row by row, and we are exploiting
		//this to initialise each row as we go
		List<String> rowData = new ArrayList<>();
		tableData.add(rowData);
		tableData.get(row).addAll(Arrays.asList(command.split("\\t")));
		//there might be vulnerability here if there is an empty line included in the file?
		tableData.get(row).removeAll(Arrays.asList("", null));
		row++;
	}

	public void setRowsDataFromSQL(ArrayList<String> newValues) {
		List<String> rowData = new ArrayList<>();
		tableData.add(rowData);
		//setting an ID number for the row
		String idNo = String.valueOf(getRowNumber() - 1);
		tableData.get(getRowNumber() - 1).add(idNo);
		tableData.get(getRowNumber() - 1).addAll(newValues);
	}

	public List<List<String>> getRowsData() {
		return tableData;
	}

	public int getRowNumber() {
		return tableData.size();
	}

	public String getCell(int rowNo, int columnNo){
		return tableData.get(rowNo).get(columnNo);
	}
}
