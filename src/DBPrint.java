import java.io.BufferedWriter;
import java.io.IOException;

public class DBPrint {
	DBModel model;
	//This class is going to be deleted or refactored

	public DBPrint(DBModel model) {
		this.model = model;
		printColumnNames();
		printDataArray();
	}

	private void printColumnNames(){

		for (int k = 0; k < model.getColumnNumber(); k++) {
			System.out.println(model.getColumnNames().get(k) + "\t\t\t");
		}
	}

	private void printDataArray(){
		for (int i = 0; i < (model.getRowNumber() + 1); i++) {
			for (int j = 0; j < model.getColumnNumber(); j++) {
				System.out.print(model.getDataArray().get(i).get(j) + "\t\t\t");
			}
			//Write a new line for every new row
			System.out.print("\n");
		}
	}
}
