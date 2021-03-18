package DBMain.ParseExceptions;

public class CommandMissing extends ParseExceptions {
	DomainType domain;

	public CommandMissing(DomainType domain){
		this.domain = domain;
	}

	public String toString(){
		return "Missing command. " + specifyDomain();
	}

	private String specifyDomain(){
		if(domain!=DomainType.UNKNOWN){
			return "\nYou need to specify a " + domain + ".";
		}
		else{
			return "\nPlease try again.";
		}
	}
}
