package DBMain.DBTokeniser;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.*;


public class DBTokeniser {
	final ArrayList<String> TokenList = new ArrayList<>();
	private int ArrayPosition = 0;

	public DBTokeniser(String incomingCommand){
		Pattern pattern = Pattern.compile("('.*?')|(==)|(>=)|(<=)|(!=)|[;,)(=<>*!]|[&^%$£@?}{/-]|[.a-zA-Z0-9]*");
		Matcher matcher = pattern.matcher(incomingCommand);
		while(matcher.find()){
			//This splits the string and turns it into tokens with no white space and keeps punctuation
			TokenList.add(matcher.group());
		}
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
			return TokenList.get(basePosition + lookForward);
		}
		else{
			return null;
		}
	}
}
