package DBMain;
import DBMain.ModelFiles.*;

import java.io.*;

public class DBStore {
    final DBModelData modelData;
    final DBModelPath modelPath;

    public DBStore(DBModelData modelData, DBModelPath modelPath) throws IOException{
        this.modelData = modelData;
        this.modelPath  = modelPath;

        if(modelPath.getDatabaseName() != null){
            createDatabase();
            if(modelPath.getFilename() != null){
                processFile();
            }
        }
    }

    private String getFilePath(){
        return "databaseFiles" + File.separator + modelPath.getDatabaseName() + File.separator + modelPath.getFilename();
    }

    private String getDirectoryPath(){
        return "databaseFiles" + File.separator + modelPath.getDatabaseName();
    }

    private void createDatabase(){
        File newFolder = new File(getDirectoryPath());
        //if database doesn't exist then create it.
        if(!newFolder.exists()){
            newFolder.mkdirs();
        }
    }

    private void processFile() throws IOException{
        File currentFile = new File(getFilePath());
        createFile(currentFile);
        writeToFile(currentFile);
    }

    private void createFile(File currentFile) throws IOException{
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
