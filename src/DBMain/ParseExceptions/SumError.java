package DBMain.ParseExceptions;

public class SumError extends SyntaxError{

	public String toString(){
		return "Sum error in where condition. Check sum for mistakes.";
	}
}
