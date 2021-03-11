import java.io.*;

public class DBLoad {
    DBModel model;
    String databaseName;
    String tableName;

    public DBLoad(DBModel model, String databaseName, String tableName){
        this.model = model;
        this.databaseName = databaseName;
        this.tableName = tableName;
        readFile();
    }

    private void readFile(){
        try {
            String tabFile = databaseName + File.separator + tableName;
            File fileToOpen = new File(tabFile);
            FileReader prereader = new FileReader(fileToOpen);
            BufferedReader reader = new BufferedReader(prereader);
            //the first line need to be treated differently because it holds column information
            //rather than data
            model.setColumnNames(reader.readLine());
            String currentLine;
            while((currentLine = reader.readLine()) != null){
                model.setDataArray(currentLine);
            }
            reader.close();
        }
        //Look into adding more exceptions + making this more secure
        catch(FileNotFoundException e){
            System.out.println("File not found");
        }
        catch(IOException ex){
            System.out.println("IO Ex");
        }
    }
}