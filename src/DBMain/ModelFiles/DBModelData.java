package DBMain.ModelFiles;
import java.util.*;

public class DBModelData extends DBModel {
	//Should need a method to return column coordinate by name

	public void setColumnData(String command){
		columnNames.addAll(Arrays.asList(command.split("\\t")));
		columnNames.removeAll(Arrays.asList("", null));
	}

	public void setColumnDataByArrlist(ArrayList newColNames){
		//Make this safer!!!!
		columnNames.addAll(newColNames);
	}

	public ArrayList<String> getColumnData(){
		return columnNames;
	}

	//By finding the number of column headers, we can see how many columns we have
	public int getColumnNumber(){
		return columnNames.size();
	}

	public void setRowsData(String command){
		//setDataArray() is called row by row, and we are exploiting
		//this to initialise each row as we go
		List<String> rowData = new ArrayList<>();
		tableData.add(rowData);
		tableData.get(row).addAll(Arrays.asList(command.split("\\t")));
		//there might be vulnerability here if there is an empty line included in the file?
		tableData.get(row).removeAll(Arrays.asList("", null));
		row++;
	}
	public List<List<String>> getRowsData() {
		return tableData;
	}

	public int getRowNumber(){
		return row;
	}
}
