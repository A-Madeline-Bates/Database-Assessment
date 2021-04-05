package DBMain.ParseExceptions;

public class WrongNoValues extends TableUseError {
	final int valueNo;
	final int columnNo;

	public WrongNoValues(int valueNo, int columnNo) {
		this.valueNo = valueNo;
		this.columnNo = columnNo;
	}

	public String toString(){
		return "Attempt to input wrong number of many values. The number of values submitted at \n" +
				"one time must equal the number of columns in a table.\n" +
				"Number of values input was " + valueNo + ".\n" +
				"Number of columns available is " + columnNo + ".";
	}
}
