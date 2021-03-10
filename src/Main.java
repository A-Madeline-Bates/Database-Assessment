import java.io.*;

public class Main {

    public static void main(String[] args) {
        try {
            String tabFile = "tabFiles" + File.separator + "contact-details.tab";
            File fileToOpen = new File(tabFile);
            FileReader reader = new FileReader(fileToOpen);
            BufferedReader buffReader = new BufferedReader(reader);
            String currentLine;
            while((currentLine = buffReader.readLine()) != null){
                System.out.println(currentLine);
            }
            buffReader.close();
        }
        catch(FileNotFoundException e){
            System.out.println("File not found");
        }
        catch(IOException ex){
            System.out.println("IO Ex");
        }
    }
}