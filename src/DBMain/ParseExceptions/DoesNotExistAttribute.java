package DBMain.ParseExceptions;

public class DoesNotExistAttribute extends ParseExceptions{
	String tableName;
	String tableName2;

	public DoesNotExistAttribute(String token, String tableName, String tableName2) {
		this.token = token;
		this.tableName = tableName;
		this.tableName2 = tableName2;
	}

	public String toString(){
		return "Invalid attribute. Attribute name given does not exist in table " + displayTableName() +
				". \nAttribute name given was " + token + ".";
	}

	private String displayTableName(){
		if(tableName2 == null){
			return tableName;
		}
		else{
			return tableName + "or" + tableName2;
		}
	}
}
