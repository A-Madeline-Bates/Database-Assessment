package DBMain.ModelFiles;

public class DBModelCells extends DBModelRows{

	public String getCell(int rowNo, int columnNo){
		return tableData.get(rowNo).get(columnNo);
	}

	public void setCell(int rowNo, int columnNo, String newValue){
		tableData.get(rowNo).set(columnNo, newValue);
	}
}
