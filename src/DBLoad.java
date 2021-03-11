import java.io.*;

public class DBLoad {
    DBController controller;
    String databaseName;
    String tablename;
    public DBLoad(DBController controller, String databaseName, String tablename){
        this.controller = controller;
        this.databaseName = databaseName;
        this.tablename = tablename;
    }

    private void readFile(){
        try {
            String tabFile = "tabFiles" + File.separator + "testfile1.tab";
            File fileToOpen = new File(tabFile);
            FileReader prereader = new FileReader(fileToOpen);
            BufferedReader reader = new BufferedReader(prereader);
            //the first line need to be treated differently because it holds column information
            //rather than data
            controller.handleFirstCommand(reader.readLine());
            String currentLine;
            while((currentLine = reader.readLine()) != null){
                controller.handleIncomingCommand(currentLine);
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