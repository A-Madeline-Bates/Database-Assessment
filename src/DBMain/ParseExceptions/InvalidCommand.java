package DBMain.ParseExceptions;

public class InvalidCommand extends SyntaxError{
	final String previousCommand;
	final String optionOne;
	final String optionTwo;

	public InvalidCommand(String token, String previousCommand, String optionOne, String optionTwo){
		this.token = token;
		this.previousCommand = previousCommand;
		this.optionOne = optionOne;
		this.optionTwo = optionTwo;
	}

	public String toString(){
		return "Invalid command. The expected value following a " + previousCommand + "\nwas " + optionOne +
				extraOptionString() + ".\nCommand used was " + token + ".";
	}

	private String extraOptionString(){
		if(optionTwo != null){
			return " or " + optionTwo;
		}
		return "";
	}
}
