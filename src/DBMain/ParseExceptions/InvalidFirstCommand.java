package DBMain.ParseExceptions;

public class InvalidFirstCommand extends ParseExceptions{

	public InvalidFirstCommand(String token){
		this.token = token;
	}

	public String toString(){
		return "Invalid first command. Valid first commands are USE, CREATE, INSERT, SELECT\n" +
				"UPDATE, ALTER, DELETE, DROP, JOIN. Command used was " + token + ".";
	}
}
