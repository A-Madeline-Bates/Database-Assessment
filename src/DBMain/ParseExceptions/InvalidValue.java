package DBMain.ParseExceptions;

public class InvalidValue extends ParseExceptions{
	public InvalidValue(String token){
		this.token = token;
	}

	public String toString(){
		return "Invalid value. Table values must be either '<StringLiteral>', <BooleanLiteral>,\n" +
				"<FloatLiteral> or <IntegerLiteral>. \nInvalid command was " + token + ".";
	}
}
