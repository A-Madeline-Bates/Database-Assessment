package DBMain.ModelFiles;
public class DBModelPath extends DBModel {

	public String getFilename(){
		return filename;
	}

	public void setFilename(String filename){
		this.filename = filename;
	}

	public void setDatabaseName(String databaseName){
		this.databaseName = databaseName;
	}

	public String getDatabaseName() {
		return databaseName;
	}
}
