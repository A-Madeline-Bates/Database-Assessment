import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DBModelRows extends DBModel implements DBModelInterface {
	//Row number is determined by the number of times setDataArray() has been called

	public List<List<String>> getData(){
		return tableData;
	}

	public void setData(String command){
		//setDataArray() is called row by row, and we are exploiting
		//this to initialise each row as we go
		List<String> rowData = new ArrayList<>();
		tableData.add(rowData);
		tableData.get(row).addAll(Arrays.asList(command.split("\\t")));
		row++;
	}

	public int getRowNumber(){
		return row;
	}

//	public void setDataArray(String command){
//		//setDataArray() is called row by row, and we are exploiting
//		//this to initialise each row as we go
//		List<String> rowData = new ArrayList<>();
//		tableData.add(rowData);
//		tableData.get(row).addAll(Arrays.asList(command.split("\\t")));
//		row++;
//	}
//	public List<List<String>> getDataArray() {
//		return tableData;
//	}
}
