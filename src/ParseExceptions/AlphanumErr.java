package ParseExceptions;

public class AlphanumErr extends ParseExceptions{
	DomainType domain;

	public AlphanumErr(String token, DomainType domain){
		this.domain = domain;
		this.token = token;
	}

	public String toString(){
		return "Invalid command given. You were trying to specify a " + domain +
				",\nbut commands of that type can only hold alphanumeric characters.\n" +
				"Command used was " + token;
	}
}
