import java.io.*;

public class DBInitMain {

    public static void main(String[] args)
    {
        new DBInitMain();
    }

    private DBInitMain(){
        //this is calling our model
        DBModel model = new DBModel();
        //this is calling an empty constructor
        DBController controller = new DBController(model);
        //creating a test instance
        DBTest test = new DBTest();
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
            controller.handleFirstCommand(buffReader.readLine());
            String currentLine;
            while((currentLine = buffReader.readLine()) != null){
                controller.handleIncomingCommand(currentLine);
            }
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