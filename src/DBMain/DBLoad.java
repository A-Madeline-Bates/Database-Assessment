package DBMain;
import DBMain.ModelFiles.*;
import java.io.*;

public class DBLoad {
    DBModelData modelData;
    String databaseName;
    String tableName;

    public DBLoad(DBModelData modelData, String databaseName, String tableName){
        this.modelData = modelData;
        this.databaseName = databaseName;
        this.tableName = tableName;
        readFile();
    }

    private void readFile(){
        try {
            String tabFile = "databaseFiles" + File.separator + databaseName + File.separator + tableName;
            File fileToOpen = new File(tabFile);
            FileReader prereader = new FileReader(fileToOpen);
            BufferedReader reader = new BufferedReader(prereader);
            //the first line need to be treated differently because it holds column information
            //rather than data
            String firstLine = reader.readLine();
            modelData.setColumnDataFromLoad(firstLine);
            String currentLine;
            while((currentLine = reader.readLine()) != null){
                modelData.setRowsDataFromLoad(currentLine);
            }
            reader.close();
        }
        catch(IOException exception){
            System.out.println("IOException trying to load file " + tableName);
        }
    }
}