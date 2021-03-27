package DBMain;
import DBMain.DBTokeniser.DBTokeniser;
import DBMain.ModelFiles.*;
import DBMain.ParseExceptions.*;
import DBMain.CommandFiles.*;

import java.io.*;
import java.net.*;

public class DBServer
{
    private DBModel model;
    private DBModelData modelData;
    private DBModelPath modelPath;
    private DBCommandFactory cmdFactory;
    private String exitMessage = "";

    public DBServer(int portNumber)
    {
        buildDatabase();
        try {
            ServerSocket serverSocket = new ServerSocket(portNumber);
            System.out.println("Server Listening");
            while(true) processNextConnection(serverSocket);
        } catch(IOException ioe) {
            System.err.println(ioe);
        }
    }

    private void processNextConnection(ServerSocket serverSocket)
    {
        try {
            Socket socket = serverSocket.accept();
            BufferedReader socketReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedWriter socketWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            System.out.println("Connection Established");
            while(true) processNextCommand(socketReader, socketWriter);
        } catch(IOException ioe) {
            System.err.println(ioe);
        } catch(NullPointerException npe) {
            System.out.println("Connection Lost");
        }
    }

    private void processNextCommand(BufferedReader socketReader, BufferedWriter socketWriter) throws IOException, NullPointerException
    {
        String incomingCommand = socketReader.readLine();
        DBTokeniser tokeniser = new DBTokeniser(incomingCommand);
        processCommand(tokeniser);
        socketWriter.write(exitMessage);
        //clear exitMessage
        this.exitMessage = "";
        //This is used for EOF
        socketWriter.write("\n" + ((char)4) + "\n");
        socketWriter.flush();
    }

    private void buildDatabase(){
        //creating classes that we don't want to re-instantiate with every new incoming command
        this.modelPath = new DBModelPath();
        this.cmdFactory = new DBCommandFactory();
        testDBFiles();
    }

    private void testDBFiles(){
        File newFolder = new File("databaseFiles");
        //if database doesn't exist then create it.
        if(!newFolder.exists()){
            newFolder.mkdirs();
        }
    }

    private void processCommand(DBTokeniser tokeniser) {
        try {
            //we never automatically use the same file twice, so we're letting go of the previous file
            modelPath.setFilename(null);
            //this instantiates the command class we are using based on incomingCommand
            CMDType commandClass = cmdFactory.createCMD(tokeniser, modelPath);
            modelData = commandClass.getStorageData();
            modelPath = commandClass.getStoragePath();
            execute(commandClass, modelData, modelPath);
        } catch (ParseExceptions exception) {
            this.exitMessage = "[ERROR]\nCommand exception: " + exception;
        }  catch (IOException ioException) {
            this.exitMessage = "[ERROR]\nIOException trying to use file " + modelPath.getFilename();
        }
    }

    private void execute(CMDType commandClass, DBModelData modelData, DBModelPath modelPath) throws ParseExceptions, IOException{
        new DBStore(modelData, modelPath);
        exitMessage = "[OK]\n" + commandClass.getExitMessage();
    }

    public static void main(String args[])
    {
        DBServer server = new DBServer(8888);
    }
}
