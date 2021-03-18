package DBMain;
import DBMain.ModelFiles.*;
import DBMain.ParseExceptions.NotBuiltDB;

import java.io.*;

public class DBStore {
    DBModelData modelData;
    DBModelPath modelPath;

    public DBStore(DBModelData modelData, DBModelPath modelPath){
        //THIS MIGHT BE TOO CLOSELY COUPLED WITH MODELDATA
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

    private void processFile(){
        File currentFile = new File(getFilePath());
        try{
            createFile(currentFile);
            writeToFile(currentFile);
        }
        catch(IOException exception) {
            System.out.println("IOException trying to store file " + modelPath.getFilename());
        }
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
