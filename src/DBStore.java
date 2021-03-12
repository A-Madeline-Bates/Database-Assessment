import java.io.*;

public class DBStore {
    DBModelFileData modelData;

    public DBStore(DBModelFileData modelData, String databaseName, String tableName){
        //THIS MIGHT BE TOO CLOSELY COUPLED WITH MODEL
        //RECEIVES MODEL DATA FROM BOTH CONTROLLER AND MODEL- WHICH IS BAD?
        //it might be more sensible to find databaseName/ tableName from the model than to load them in?
        //EXCEPTIONS: IOEXCEPTION - DATABASE NOT FOUND - MAYBE FILENOTFOUND FOR FILE WRITER?
        //SHOULD WE BYPASS CREATING FILE IF FILE ALREADY EXISTS?
        this.modelData = modelData;
        String filePath = getFilePath(databaseName, tableName);
        File currentFile = new File(filePath);
        try{
            createFile(currentFile);
            writeToFile(currentFile);
        }
        catch(IOException o) {
            System.out.println("IOException oh no");
        }
    }

    private String getFilePath(String databaseName, String tableName){
        return "databaseFiles" + File.separator + databaseName + File.separator + tableName;
    }

    private void createFile(File currentFile) throws IOException{
        if(currentFile.exists()){
            System.out.println("file overwrite");
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
        for (int k = 0; k < modelData.getColumnNumber(); k++) {
            writer.write(modelData.getColumnData().get(k) + "\t\t");
        }
    }

    private void writeDataArray(BufferedWriter writer) throws IOException{
        for (int i = 0; i < modelData.getRowNumber(); i++) {
            for (int j = 0; j < modelData.getColumnNumber(); j++) {
                writer.write(modelData.getRowsData().get(i).get(j) + "\t\t");
            }
            //Write a new line for every new row
            writer.write("\n");
        }
    }
}
