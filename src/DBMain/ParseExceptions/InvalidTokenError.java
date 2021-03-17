package DBMain.ParseExceptions;

public class InvalidTokenError extends ParseExceptions{
	String token;
	String previousCommand;
	String optionOne;
	String optionTwo;

	public InvalidTokenError(String token, String previousCommand, String optionOne, String optionTwo){
		this.token = token;
		this.previousCommand = previousCommand;
		this.optionOne = optionOne;
		this.optionTwo = optionTwo;
	}

	public String toString(){
		return "Invalid command. Expected values following a " + previousCommand + "command are " + optionOne +
				"or " + optionTwo + ". Command used was " + token + ".";
	}
}
