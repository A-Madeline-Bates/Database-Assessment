import java.io.*;
import java.net.*;

class DBServer
{
    DBModel model;
    DBModelData modelData;
    DBModelPath modelPath;
    DBParser parser;

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
        //this instantiates the command class we are using based on incomingCommand
        CMDType commandClass = parser.parse(tokeniser);

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
        this.parser = new DBParser();
    }

    public static void main(String args[])
    {
        DBServer server = new DBServer(8888);
    }
}
