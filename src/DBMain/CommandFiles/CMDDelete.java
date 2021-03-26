package DBMain.CommandFiles;
import DBMain.DBLoad.DBLoad;
import DBMain.ParseExceptions.DomainType;
import DBMain.ParseExceptions.ParseExceptions;
import DBMain.ParseExceptions.RequestedCell;

import java.io.IOException;
import java.util.ArrayList;

public class CMDDelete extends ProcessWhere {

	public void transformModel() throws ParseExceptions, IOException {
		String firstCommand = getTokenSafe(DomainType.FROM);
		if(stringMatcherTHROW("FROM", firstCommand, "DELETE")){
			String secondCommand = getTokenSafe(DomainType.TABLENAME);
			if (doesTableExist(secondCommand)) {
				setTemporaryModel(secondCommand, temporaryPathModel, temporaryDataModel);
				new DBLoad(storageData, temporaryPathModel.getDatabaseName(), secondCommand);
				String thirdCommand = getTokenSafe(DomainType.WHERE);
				if(stringMatcherTHROW("WHERE", thirdCommand, "DELETE FROM [table]")) {
					splitIfBrackets(this);
				}
			}
		}
	}

	protected void returnToCMD(ArrayList<RequestedCell> finalRows){
		removeRows(finalRows);
		//using storagePath.setFilename to indicate that we want to store changes
		storagePath.setFilename(temporaryPathModel.getFilename());
		setExitMessage();
	}

	//we have to iterate downwards otherwise the row numbers won't correspond to those held by finalRows
	private void removeRows(ArrayList<RequestedCell> finalRows){
		for (int i = (storageData.getRowNumber() - 1); i >= 0; i--) {
			if (finalRows.get(i).equals(RequestedCell.TRUE)) {
				storageData.deleteRow(i);
			}
		}
	}
}
