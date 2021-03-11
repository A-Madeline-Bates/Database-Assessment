import java.io.*;

public class DBStore {
    DBModel model;

    public DBStore(DBModel model){
        this.model = model;
        String filePath = getFilePath();
        File currentFile = new File(filePath);
        try{
            createFile(currentFile);
            writeToFile(currentFile);
        }
        //develop + catch a much broader set of exceptions!
        //this needs to represent the concerns of createFile and fileWriter
        catch(IOException o) {
            System.out.println("IOException oh no");
        }
    }

    private String getFilePath(){
        return "productionFiles" + File.separator + model.getFilename();
    }

    private void createFile(File currentFile) throws IOException{
        if(currentFile.exists()){
            System.out.println("we've been here before");
            //it's not problem is the file already exists, but is worth noting for what we're doing
            //later
        }
        currentFile.createNewFile();
    }

    private void writeToFile(File currentFile) throws IOException{
        FileWriter prewriter = new FileWriter(currentFile);
        BufferedWriter writer = new BufferedWriter(prewriter);
        writeColumnNames(writer);
        //write new line between col names and the data
        writer.write("\n");
        writeDataArray(writer);
        writer.flush();
        writer.close();
    }

    private void writeColumnNames(BufferedWriter writer) throws IOException{
        for (int k = 0; k < model.getColumnNumber(); k++) {
            writer.write(model.getColumnNames().get(k) + "\t\t");
        }
    }

    private void writeDataArray(BufferedWriter writer) throws IOException{
        for (int i = 0; i < model.getRowNumber(); i++) {
            for (int j = 0; j < model.getColumnNumber(); j++) {
                writer.write(model.getDataArray().get(i).get(j) + "\t\t");
            }
            //Write a new line for every new row
            writer.write("\n");
        }
    }
}
