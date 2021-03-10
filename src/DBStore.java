import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.*;

public class DBStore {
    DBController controller;
    DBModel model;

    public DBStore(DBModel model, DBController controller){
        this.model = model;
        this.controller = controller;
        createFile();
    }

    private void createFile(){
        String filePath = "productionFiles" + File.separator + model.getFilename();
        File currentFile = new File(filePath);
        if(currentFile.exists()){
            System.out.println("we've been here before");
            //it's not problem is the file already exists, but is worth noting for what we're doing
            //later
        }
        try{
            currentFile.createNewFile();
            writeToFile(currentFile);
        }
        //develop + catch a much broader set of exceptions!
        catch(IOException o){
            System.out.println("IOException oh no");
        }
    }

    private void writeToFile(File currentFile) throws IOException{
        FileWriter prewriter = new FileWriter(currentFile);
        BufferedWriter writer = new BufferedWriter(prewriter);
        writeColumnNames(currentFile, writer);
        writer.write("\n");
        writeDataArray(currentFile, writer);
        writer.flush();
        writer.close();
    }

    private void writeColumnNames(File currentFile, BufferedWriter writer) throws IOException{
        for (int k = 0; k < model.getColumnNumber(); k++) {
            writer.write(model.getColumnNames().get(k) + "\t\t");
        }
    }

    private void writeDataArray(File currentFile, BufferedWriter writer) throws IOException{
        for (int i = 0; i < model.getRowNumber(); i++) {
            for (int j = 0; j < model.getColumnNumber(); j++) {
                writer.write(model.getDataArray().get(i).get(j) + "\t\t");
            }
            writer.write("\n");
        }
    }
}
