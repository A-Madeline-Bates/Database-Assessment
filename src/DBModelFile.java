public class DBModelFile extends DBModel implements DBModelInterface {

	public String getData(){
		return filename;
	}

	public void setData(String filename){
		this.filename = filename;
	}

//	public void setFilename(String filename){
//		this.filename = filename;
//	}
//
//	public String getFilename(){
//		return filename;
//	}
}
