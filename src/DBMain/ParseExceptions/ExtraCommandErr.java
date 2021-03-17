package DBMain.ParseExceptions;

public class ExtraCommandErr extends ParseExceptions{

	public ExtraCommandErr(String token){
		this.token = token;
	}

	public String toString(){
		return "Extra command. You added one more command than was expected. Extra command was " + token + ".";
	}
}
