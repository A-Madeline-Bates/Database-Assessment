package DBMain.ParseExceptions;

import DBMain.DBEnums.DomainType;

public class NotAlphanum extends DomainException{
	public NotAlphanum(String token, DomainType domain){
		this.domain = domain;
		this.token = token;
	}

	public String toString(){
		return "Invalid command given. You were trying to specify a " + domain +
				",\nbut commands of that type can only hold alphanumeric characters.\n" +
				"Command used was " + token + ".";
	}
}
