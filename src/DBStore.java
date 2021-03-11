import java.io.*;

public class DBStore {
    DBModel model;

    public DBStore(DBModel model, String databaseName, String tableName){
        this.model = model;
        //it might be more sensible to find these from the model than to load them in? (databaseName/ tableName)
        String filePath = getFilePath(databaseName, tableName);
        File currentFile = new File(filePath);
        try{
            createFile(currentFile);
            writeToFile(currentFile);
        }
        //develop + catch a much broader set of exceptions!
        //this needs to represent the concerns of both createFile and fileWriter
        catch(IOException o) {
            System.out.println("IOException oh no");
        }
    }

    private String getFilePath(String databaseName, String tableName){
        return databaseName + File.separator + tableName;
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
        for (int i = 0; i < (model.getRowNumber() + 1); i++) {
            for (int j = 0; j < model.getColumnNumber(); j++) {
                writer.write(model.getDataArray().get(i).get(j) + "\t\t");
            }
            //Write a new line for every new row
            writer.write("\n");
        }
    }
}
