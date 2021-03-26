package DBMain.ModelFiles;
import java.util.*;

public class DBModelData extends DBModel {
	//Should need a method to return column coordinate by name

	public void setColumnsFromFile(String command) {
		columnNames.addAll(Arrays.asList(command.split("\\t")));
		columnNames.removeAll(Arrays.asList("", null));
	}

	//This should only ever be called after clearing the model
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

	public void setRowsFromFile(String command) {
		//setDataArray() is called row by row, and we are exploiting
		//this to initialise each row as we go
		List<String> rowData = new ArrayList<>();
		tableData.add(rowData);
		tableData.get(row).addAll(Arrays.asList(command.split("\\t")));
		//there might be vulnerability here if there is an empty line included in the file?
		tableData.get(row).removeAll(Arrays.asList("", null));
		row++;
	}

	public void setRowsFromSQL(ArrayList<String> newValues) {
		List<String> rowData = new ArrayList<>();
		tableData.add(rowData);
		String idNo = getID();
		tableData.get(getRowNumber() - 1).add(idNo);
		tableData.get(getRowNumber() - 1).addAll(newValues);
	}

	private String getID(){
		String idNo = String.valueOf(1);
		if(getRowNumber() > 1) {
			//because we are deleting rows, we can't just set id to rowNo because there could be clashes- instead,
			// we're finding the id of the previous cell, adding one to it and using that as our new ID
			int prevRow = getRowNumber()-2;
			int calculateId = Integer.parseInt(getCell(prevRow, 0)) + 1;
			idNo = String.valueOf(calculateId);
		}
		return idNo;
	}

	public List<List<String>> getRowsData() {
		return tableData;
	}

	public List<String> getSingleRow(int i){
		return tableData.get(i);
	}

	public int getRowNumber() {
		return tableData.size();
	}

	public String getCell(int rowNo, int columnNo){
		return tableData.get(rowNo).get(columnNo);
	}

	public void setCell(int rowNo, int columnNo, String newValue){
		tableData.get(rowNo).set(columnNo, newValue);
	}

	public void addColumn(String colName){
		columnNames.add(colName);
		for(int i=0; i<getRowNumber(); i++){
			tableData.get(i).add("''");
		}
	}

	public void deleteColumn(int colNum){
		columnNames.remove(colNum);
		for(int i=0; i<getRowNumber(); i++){
			tableData.get(i).remove(colNum);
		}
	}

	public void deleteRow(int rowNum){
		tableData.remove(rowNum);
	}
}
