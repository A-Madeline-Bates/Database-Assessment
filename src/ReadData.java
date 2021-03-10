import java.io.*;

public class ReadData {

    public static void main(String[] args)
    {
        new ReadData();
    }

    private ReadData(){
        //this is calling an empty constructor
        DBController controller = new DBController();
        readFile(controller);
    }

    private void readFile(DBController controller){
        try {
            String tabFile = "tabFiles" + File.separator + "contact-details.tab";
            File fileToOpen = new File(tabFile);
            FileReader reader = new FileReader(fileToOpen);
            BufferedReader buffReader = new BufferedReader(reader);
            //the first line need to be treated differently because it holds column information
            //rather than data
            controller.setColNameArray(buffReader.readLine());
            String currentLine;
            while((currentLine = buffReader.readLine()) != null){
                controller.handleIncomingCommand(currentLine);
            }
            controller.printTable();
            buffReader.close();
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