package DBMain.ParseExceptions;

public class TokenDoesNotExistErr extends ParseExceptions {
	DomainType domain;

	public TokenDoesNotExistErr(DomainType domain){
		this.domain = domain;
	}

	public String toString(){
		return "Missing command. " + specifyDomain();
	}

	private String specifyDomain(){
		if(domain!=DomainType.UNKNOWN){
			return "You need to specify a " + domain + ".";
		}
		else{
			return "Please try again.";
		}
	}
}
