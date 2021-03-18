package DBMain.ParseExceptions;

public class NotBuiltFile extends ParseExceptions{
	public NotBuiltFile(String token) {
		this.token = token;
	}

	public String toString(){
		return "File could not be created. This is probably because the file " +
				"specified already exists. File name given was " + token + ".";
	}
}
