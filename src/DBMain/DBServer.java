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
    String exceptionMessage = "";

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

        processParse(tokeniser);
        socketWriter.write(exceptionMessage);

//        //this takes a copy of server so that we can execute our command
//        //in order to execute a command we must appropriately manipulate our data,
//        //store the result, and then print the appropriate message
//        //perhaps we should get command.query to return a whole set of data (like DBModel)
//        //so that a response can be set here
//        commandClass.query(this);
//
//        //set an error and response Class in model which can be set in DBParse and reread here with
//        //if(DBModelError.isError=true) then print "[ERROR] + DBModelError.getError"
//        //else print "[OK] + DBModelResponse.getResponse()";
//
//        //DBStore(DBModelData, DBModelPath);
//        //DBRespond(socketWriter, DBModelData, DBModelPath, DBModelResponse, DBModelError);
//        //(Definitely work out if can just pass DBModel as one)

        //clear exception message
        this.exceptionMessage = "";
        //This is used for EOF
        socketWriter.write("\n" + ((char)4) + "\n");
        socketWriter.flush();
    }

    private void buildDatabase(){
        //creating a test instance
        new DBTest();
        //creating classes that we don't want to re-instantiate with every new incoming command
        this.model = new DBModel();
        this.modelData = new DBModelData();
        this.modelPath = new DBModelPath();
        this.cmdFactory = new DBCommandFactory();
    }

    private void processParse(DBTokeniser tokeniser) {
        try {
            //this instantiates the command class we are using based on incomingCommand
            CMDType commandClass = cmdFactory.createCMD(tokeniser);
            //this is too many calls to commandClass- create a nice workaround
            //we could put getters and setting is DBServer and do it the other way around?
            //this gives the model to our class
            commandClass.setModel(modelData, modelPath);
            commandClass.setInstructionSet(tokeniser);
            commandClass.transformModel();
            modelData = commandClass.getModelData();
            modelPath = commandClass.getModelPath();
            //this has to be within process parse because we want to skip DBStore if an exception is called.
            //We need to create a fix for this.
            new DBStore(modelData, modelPath);
        } catch (ParseExceptions exception) {
            this.exceptionMessage = "[ERROR]\nCommand exception: " + exception;
//            System.out.println(exceptionMessage);
        }
//        CURRENTLY UNUSED:
//        catch (IOException ioParseException) {
//            this.exceptionMessage = "[ERROR]\nIO exception: Error when try to access file.";
//            System.out.println("IO exception: Error when try to access file.");
//        }
    }

    public static void main(String args[])
    {
        DBServer server = new DBServer(8888);
    }
}
