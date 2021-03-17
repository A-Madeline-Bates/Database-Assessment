package DBMain.ParseExceptions;

public class CommandMissingErr extends ParseExceptions {
	DomainType domain;

	public CommandMissingErr(DomainType domain){
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
