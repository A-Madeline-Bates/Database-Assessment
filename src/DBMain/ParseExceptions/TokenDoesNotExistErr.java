package DBMain.ParseExceptions;

public class TokenDoesNotExistErr extends ParseExceptions {
	DomainType domain;

	public TokenDoesNotExistErr(DomainType domain){
		this.domain = domain;
	}

	public String toString(){
		return "Missing command. You need to specify a " + domain + ".";
	}
}
