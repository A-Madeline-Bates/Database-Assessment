import java.util.ArrayList;
import java.util.Arrays;

public class DBModelColumns extends DBModel implements DBModelInterface {
	//Should need a method to return column coordinate by name

	public ArrayList<String> getData(){
		return columnNames;
	}

	public void setData(String command){
		columnNames.addAll(Arrays.asList(command.split("\\t")));
	}

	//By finding the number of column headers, we can see how many columns we have
	public int getColumnNumber(){
		return columnNames.size();
	}

//	public void setAllColNames(String command) {
//		columnNames.addAll(Arrays.asList(command.split("\\t")));
//	}
//	public ArrayList<String> getAllColNames() {
//		return columnNames;
//	}
}
