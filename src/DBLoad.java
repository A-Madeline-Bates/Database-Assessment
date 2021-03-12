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
            modelData.setColumnData(firstLine);
            String currentLine;
            while((currentLine = reader.readLine()) != null){
                modelData.setRowsData(currentLine);
            }
            reader.close();
        }
        catch(IOException ex){
            System.out.println("IOException trying to store file " + tableName);
        }
    }
}