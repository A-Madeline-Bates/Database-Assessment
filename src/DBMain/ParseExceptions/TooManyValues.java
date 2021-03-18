package DBMain.ParseExceptions;

public class TooManyValues extends ParseExceptions{
	int valueNo;
	int columnNo;
	public TooManyValues(int valueNo, int columnNo) {
		this.valueNo = valueNo;
		this.columnNo = columnNo;
	}

	public String toString(){
		return "Attempt to input too many values. The number of values submitted at \n" +
				"one time can't be greater than the number of columns in a table.\n" +
				"Number of values input was " + valueNo + ".\n" +
				"Number of columns available is " + columnNo + ".";
	}
}
