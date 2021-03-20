package DBMain.ParseExceptions;

public class DoesNotExistAttribute extends ParseExceptions{
	String tableName;
	public DoesNotExistAttribute(String token, String tableName) {
		this.token = token;
		this.tableName = tableName;
	}

	public String toString(){
		return "Invalid attribute. Attribute name given does not exist in table " + tableName +
				". \nAttribute name given was " + token + ".";
	}
}
