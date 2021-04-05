package DBMain.ParseExceptions;
import DBMain.DBEnums.DomainType;

public class NotCommaSeparated extends DomainException{

	public NotCommaSeparated(DomainType domain) {
		this.domain = domain;
	}

	public String toString(){
		return "List is not comma separated. Commas needed when listing elements of \n" +
				"type " + domain + ".";
	}
}
