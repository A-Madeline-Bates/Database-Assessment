package DBMain.ParseExceptions;

public class ExtraCommandGiven extends SyntaxError{

	public ExtraCommandGiven(String token){
		this.token = token;
	}

	public String toString(){
		return "Extra command. You added one more command than was expected. \nExtra command was " + token + ".";
	}
}
