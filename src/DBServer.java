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

//        DBTokeniser tokeniser = new DBTokeniser(incomingCommand);
//        parser.parse(tokeniser);
        //parser.executeCMD(DBServer ?)
        //socketWriter.write writes to the client
        //socketWriter.write(controller.getUserMessage());

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
