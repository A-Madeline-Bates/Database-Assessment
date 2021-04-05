package DBMain.CommandFiles;
import DBMain.DBTokeniser.DBTokeniser;
import DBMain.ModelFiles.DBModelTable;
import DBMain.ModelFiles.DBModelPath;
import DBMain.ParseExceptions.NoAttributeFound;
import DBMain.DBEnums.DomainType;
import DBMain.ParseExceptions.ParseExceptions;

import java.io.IOException;
import java.util.ArrayList;

public class CMDJoin extends AttributeSearch {
	//this is a second 'temporary' version of the model that can be used to check data without running the risk of
	//storing the data you're working on (or wiping a file)
	final DBModelTable temporaryDataModel2 = new DBModelTable();
	final DBModelPath temporaryPathModel2 = new DBModelPath();
	//a separate model to create our 'join' table
	final DBModelTable joinModel = new DBModelTable();

	public CMDJoin(DBTokeniser tokeniser, DBModelPath path) throws IOException, ParseExceptions {
		buildCommand(tokeniser, path);
	}

	public void transformModel() throws ParseExceptions, IOException {
		String firstCommand = getTokenSafe(DomainType.TABLENAME);
		if(doesTableExist(firstCommand)){
			setTemporaryModel(firstCommand, temporaryPathModel, temporaryDataModel);
			String secondCommand = getTokenSafe(DomainType.AND);
			if(stringMatcherTHROW("AND", secondCommand, "JOIN [tablename]")){
				String thirdCommand = getTokenSafe(DomainType.TABLENAME);
				if(doesTableExist(thirdCommand)) {
					setTemporaryModel(thirdCommand, temporaryPathModel2, temporaryDataModel2);
					String fourthCommand = getTokenSafe(DomainType.ON);
					if (stringMatcherTHROW("ON", fourthCommand, "JOIN [tablename] AND [tablename]")) {
						findColMatches();
						setExitMessage();
					}
				}
			}
		}
	}

	private void findColMatches() throws ParseExceptions {
		String attributeCommand = getTokenSafe(DomainType.ATTRIBUTENAME);
		int firstMatch = findAttribute(attributeCommand, temporaryDataModel);
		//attempt to find attribute in our first table
		if(firstMatch>= 0) {
			//this passes on which coordinate gave the match, and which table it was in
			findSecondMatch(firstMatch, 1);
		}
		//if it's not found in table 1, search in table 2
		else{
			firstMatch = findAttribute(attributeCommand, temporaryDataModel2);
			//attempt to find attribute in second table
			if(firstMatch>= 0) {
				//this passes on which coordinate gave the match, and which table it was in
				findSecondMatch(firstMatch, 2);
			}
			else{
				throw new NoAttributeFound(attributeCommand, temporaryPathModel.getFilename(),
						temporaryPathModel2.getFilename());
			}
		}
	}

	private void findSecondMatch(int firstMatch, int whichTable) throws ParseExceptions{
		String nextCommand = getTokenSafe(DomainType.AND);
		if(stringMatcherTHROW("AND", nextCommand, "ON [attribute]")) {
			nextCommand = getTokenSafe(DomainType.ATTRIBUTENAME);
			int secondMatch;
			//if our first match was in table 1, search table 2 for the next attribute
			if (whichTable == 1) {
				secondMatch = findAttributeTHROW(nextCommand, temporaryPathModel2, temporaryDataModel2);
			} //if our first match wasn't in table 1, it must be table 2- look in table 1 for next attribute.
			else {
				secondMatch = findAttributeTHROW(nextCommand, temporaryPathModel, temporaryDataModel);
			}
			processMatches(whichTable, firstMatch, secondMatch);
		}
	}

	//whichTable identifies which table the first match belongs to
	private void processMatches(int whichTable, int firstMatch, int secondMatch){
		//if whichTable == 0, then firstMatch corresponds to the table in temporaryDataModel- which is what we want to
		//use for the primary column. If whichTable == 1, secondMatch corresponds to the table in temporaryDataModel
		int primaryColumn, secondaryColumn;
		if(whichTable == 1){
			primaryColumn = firstMatch;
			secondaryColumn = secondMatch;
		} else{
			primaryColumn = secondMatch;
			secondaryColumn = firstMatch;
		}
		fillJoin(primaryColumn, secondaryColumn);
	}

	private void fillJoin(int primaryColumn, int secondaryColumn){
		//first table row will have an ID set, but not the second
		joinModel.setColumnsFromSQL(temporaryDataModel.getColumnData());
		joinModel.setColumnsSQLNoID(temporaryDataModel2.getColumnData());
		for(int i=0; i<temporaryDataModel.getRowNumber(); i++){
			for(int j=0; j<temporaryDataModel2.getRowNumber(); j++){
				if(temporaryDataModel.getCell(i, primaryColumn).equals(temporaryDataModel2.getCell(j, secondaryColumn))){
					ArrayList<String> joinRow = new ArrayList<>();
					joinRow.addAll(temporaryDataModel.getSingleRow(i));
					joinRow.addAll(temporaryDataModel2.getSingleRow(j));
					//this will set one ID column for our joinRow
					joinModel.setRowsFromSQL(joinRow);
				}
			}
		}
	}

	public void setExitMessage(){
		setColumnsMessage();
		exitMessage = exitMessage + "\n";
		for (int i = 0; i < joinModel.getRowNumber(); i++) {
			for (int j = 0; j < joinModel.getColumnNumber(); j++) {
				exitMessage = exitMessage + joinModel.getCell(i, j) + "\t";
			}
			//Write a new line for every new row
			exitMessage = exitMessage + "\n";
		}
	}

	private void setColumnsMessage(){
		for (int k = 0; k < joinModel.getColumnNumber(); k++) {
			if(exitMessage == null){
				exitMessage = joinModel.getColumnAttribute(k) + "\t";
			} else {
				exitMessage = exitMessage + joinModel.getColumnAttribute(k) + "\t";
			}
		}
	}
}
