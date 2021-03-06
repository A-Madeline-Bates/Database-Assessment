package DBMain.CommandFiles;

import DBMain.ModelFiles.DBModelTable;
import DBMain.ModelFiles.DBModelPath;
import DBMain.ParseExceptions.*;

public abstract class AttributeSearch extends MainDataClasses {

	/******************************************************
	 ****************** ATTRIBUTE METHODS ****************
	 *****************************************************/

	protected int findAttribute(String nextCommand, DBModelTable data) {
		//iterate through the columns of our table until we find a match
		for (int i = 0; i < data.getColumnNumber(); i++) {
			if (data.getColumnData().get(i).equalsIgnoreCase(nextCommand)) {
				return i;
			}
		}
		return -1;
	}

	protected int findAttributeTHROW(String nextCommand, DBModelPath path, DBModelTable data) throws NoAttributeFound {
		int attributeCoordinate = findAttribute(nextCommand, data);
		if (attributeCoordinate >= 0) {
			return attributeCoordinate;
		} else {
			throw new NoAttributeFound(nextCommand, path.getFilename(), null);
		}
	}
}
