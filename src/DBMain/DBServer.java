package DBMain;
import DBMain.ModelFiles.*;
import DBMain.ParseExceptions.*;
import DBMain.CommandFiles.*;

import java.io.*;
import java.net.*;

public class DBServer
{
    DBModel model;
    DBModelData modelData;
    DBModelPath modelPath;
    DBCommandFactory cmdFactory;
    String exitMessage = "";

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
        //incomingCommand contains the message we're going to use.
        String incomingCommand = socketReader.readLine();
        DBTokeniser tokeniser = new DBTokeniser(incomingCommand);
        processCommand(tokeniser);
        //replace exceptionMessage with general message
        socketWriter.write(exitMessage);
        //clear exception message
        this.exitMessage = "";
        //This is used for EOF
        socketWriter.write("\n" + ((char)4) + "\n");
        socketWriter.flush();
    }

    private void buildDatabase(){
        //creating a test instance
        new DBTest();
        //creating classes that we don't want to re-instantiate with every new incoming command
        this.model = new DBModel();
        this.modelPath = new DBModelPath();
        this.cmdFactory = new DBCommandFactory();
    }

    private void processCommand(DBTokeniser tokeniser) {
        try {
            //this instantiates the command class we are using based on incomingCommand
            CMDType commandClass = cmdFactory.createCMD(tokeniser);
            parseData(commandClass, tokeniser);
            modelData = commandClass.getStorageData();
            modelPath = commandClass.getStoragePath();
            execute(commandClass, modelData, modelPath);
        } catch (ParseExceptions exception) {
            this.exitMessage = "[ERROR]\nCommand exception: " + exception;
        }  catch (IOException ioException) {
            this.exitMessage = "[ERROR]\nIOException trying to use file " + modelPath.getFilename();
        }
    }

    private void parseData(CMDType commandClass, DBTokeniser tokeniser) throws ParseExceptions, IOException{
        commandClass.setModel(modelPath);
        commandClass.setTokeniser(tokeniser);
        commandClass.transformModel();
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
