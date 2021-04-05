package DBMain.DBLoad;
import DBMain.ModelFiles.*;
import java.io.*;

public class DBLoad {
    final DBModelRows modelRows;
    final DBModelColumns modelColumns;
    final String databaseName;
    final String tableName;

    public DBLoad(DBModelColumns modelColumns, DBModelRows modelRows, String databaseName, String tableName) throws IOException{
        this.modelColumns = modelColumns;
        this.modelRows = modelRows;
        this.databaseName = databaseName;
        this.tableName = tableName;
        readFile();
    }

    private void readFile() throws IOException{
        String tabFile = "databaseFiles" + File.separator + databaseName + File.separator + tableName;
        File fileToOpen = new File(tabFile);
        FileReader prereader = new FileReader(fileToOpen);
        BufferedReader reader = new BufferedReader(prereader);
        //the first line need to be treated differently because it holds column information
        //rather than data
        String firstLine = reader.readLine();
        modelColumns.setColumnsFromFile(firstLine);
        String currentLine;
        while((currentLine = reader.readLine()) != null){
            modelRows.setRowsFromFile(currentLine);
        }
        reader.close();
    }
}