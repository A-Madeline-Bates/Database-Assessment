public class DBParser {
	//This will act as a factory for instances of CMDType
	public CMDType parse(){
		//Token nextToken = DDTokeniser.nextToken();
		//switch [nextToken]
		//case "USE": return new CMDUse();
		//case "CREATE": return new CMDCreate();
		//case "INSERT": return new CMDInsert();
		//case "SELECT": return new CMDSelect();
		//case "UPDATE": return new CMDUpdate();
		//case "ALTER": return new CMDAlter();
		//case "DELETE": return new CMDDelete();
		//case "DROP": return new CMDDrop();
		//case "JOIN": return new CMDJoin();
		//default:
		return new CMDCreate();
	}
}
