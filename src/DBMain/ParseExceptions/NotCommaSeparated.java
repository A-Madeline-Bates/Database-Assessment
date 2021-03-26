package DBMain.ParseExceptions;

public class NotCommaSeparated extends ParseExceptions{
	final DomainType domain;

	public NotCommaSeparated(DomainType domain) {
		this.domain = domain;
	}

	public String toString(){
		return "List is not comma separated. Commas needed when listing elements of \n" +
				"type " + domain + ".";
	}
}
