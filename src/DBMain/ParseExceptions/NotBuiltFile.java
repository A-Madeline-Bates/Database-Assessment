package DBMain.ParseExceptions;

public class NotBuiltFile extends FileSystemError {
	public NotBuiltFile(String token) {
		this.token = token;
	}

	public String toString(){
		return "File could not be created. This is probably because the file \n" +
				"specified already exists. File name given was " + token + ".";
	}
}
