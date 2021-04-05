package DBMain.ModelFiles;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DBModelRows extends DBModelColumns{

	public void setRowsFromFile(String command) {
		//setDataArray() is called row by row, and we are exploiting
		//this to initialise each row as we go
		List<String> rowData = new ArrayList<>();
		tableData.add(rowData);
		tableData.get(row).addAll(Arrays.asList(command.split("\\t")));
		tableData.get(row).removeAll(Arrays.asList("", null));
		row++;
	}

	public void setRowsFromSQL(ArrayList<String> newValues) {
		List<String> rowData = new ArrayList<>();
		tableData.add(rowData);
		String idNo = findIDNo();
		tableData.get(getRowNumber() - 1).add(idNo);
		tableData.get(getRowNumber() - 1).addAll(newValues);
	}

	private String findIDNo(){
		String idNo = String.valueOf(1);
		if(getRowNumber() > 1) {
			//because we are deleting rows, we can't just set id to rowNo because there could be clashes- instead,
			// we're finding the id of the previous cell, adding one to it and using that as our new ID
			int prevRow = getRowNumber()-2;
			int calculateId = Integer.parseInt(tableData.get(prevRow).get(0)) + 1;
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

	public void deleteRow(int rowNum){
		tableData.remove(rowNum);
	}
}
