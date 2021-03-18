package DBMain.ParseExceptions;

public class MissingSemiColon extends ParseExceptions{
	public MissingSemiColon(String token) {
		this.token = token;
	}

	public String toString(){
		return "Incorrect end to command line. All command lines must \nend with a semicolon " +
				"in order to be valid. \nCommand given was " + token + ".";
	}
}
