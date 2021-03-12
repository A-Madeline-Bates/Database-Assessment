public class DBModelDatebase extends DBModel implements DBModelInterface {

	public void setData(String databaseName){
		this.databaseName = databaseName;
	}

	public String getData(){
		return databaseName;
	}
//	public void setDatabaseName(String databaseName){
//		this.databaseName = databaseName;
//	}
//
//	public String getDatabaseName(){
//		return databaseName;
//	}
}
