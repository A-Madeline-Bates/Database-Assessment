package DBMain;
import java.util.ArrayList;
import java.util.Arrays;

public class DBTokeniser {
	final ArrayList<String> TokenList = new ArrayList<>();
	private int ArrayPosition = 0;

	public DBTokeniser(String incomingCommand){
		//This splits the string and turns it into tokens with no white space
		// It will also catch illegal characters?
		TokenList.addAll(Arrays.asList(incomingCommand.split("\\s+")));
	}

	//Find a way to create defined String type 'token'
	public String nextToken(){
		if(ArrayPosition < TokenList.size()){
			String Token = TokenList.get(ArrayPosition);
			ArrayPosition++;
			return Token;
		}
		else{
			return null;
		}
	}
}
