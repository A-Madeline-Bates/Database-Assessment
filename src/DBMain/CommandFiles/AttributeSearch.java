package DBMain.CommandFiles;
import DBMain.DBTokeniser.DBTokeniser;
import DBMain.ModelFiles.DBModelCells;
import DBMain.ModelFiles.DBModelColumns;
import DBMain.ModelFiles.DBModelPath;
import DBMain.ParseExceptions.*;

import java.io.IOException;

public abstract class AttributeSearch extends TemporaryModel {
	protected DBModelCells storageCells;
	protected DBModelCells temporaryCells;

	public AttributeSearch(DBTokeniser tokeniser, DBModelPath path) throws IOException, ParseExceptions {
		super(tokeniser, path);
		this.storageCells = new DBModelCells();
		this.temporaryCells = new DBModelCells();
	}

//	public AttributeSearch(){
//		this.storageCells = new DBModelCells();
//		this.temporaryCells = new DBModelCells();
//	}

	/******************************************************
	 ****************** ATTRIBUTE METHODS ****************
	 *****************************************************/

	protected int findAttribute(String nextCommand, DBModelColumns data) {
		//iterate through the columns of our table until we find a match
		for (int i = 0; i < data.getColumnNumber(); i++) {
			if (data.getColumnData().get(i).equalsIgnoreCase(nextCommand)) {
				return i;
			}
		}
		return -1;
	}

	protected int findAttributeTHROW(String nextCommand, DBModelPath path, DBModelColumns data) throws ParseExceptions {
		int attributeCoordinate = findAttribute(nextCommand, data);
		if (attributeCoordinate >= 0) {
			return attributeCoordinate;
		} else {
			throw new DoesNotExistAttribute(nextCommand, path.getFilename(), null);
		}
	}
}
