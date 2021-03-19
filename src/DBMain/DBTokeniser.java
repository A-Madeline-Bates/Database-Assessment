package DBMain;
import java.util.ArrayList;
import java.util.Arrays;

public class DBTokeniser {
	final ArrayList<String> TokenList = new ArrayList<>();
	private int ArrayPosition = 0;

	public DBTokeniser(String incomingCommand){
		//This splits the string and turns it into tokens with no white space
		TokenList.addAll(Arrays.asList(incomingCommand.split("\\s+|(?=[;,()])|(?<=[;,()])")));
		TokenList.removeAll(Arrays.asList("", null));
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

	//Peak at the next token
	public String peakToken(int lookForward){
		//ArrayPosition is always one ahead of us, so we have to rewind it to see our current position.
		int basePosition = ArrayPosition-1;
		if((basePosition + lookForward) < TokenList.size()){
			String Token = TokenList.get(basePosition + lookForward);
			return Token;
		}
		else{
			return null;
		}
	}
}
