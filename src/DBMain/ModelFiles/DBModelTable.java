package DBMain.ModelFiles;

public class DBModelTable extends DBModelRows{

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
}
