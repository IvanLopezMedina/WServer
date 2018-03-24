/**
 * Wserver main class that creates the socket and the connection with the client
 * @author Ivan Lopez Medina - Miquel Martin Ezquerra
 * @version 999999999
 */

import java.net.*;
import java.io.*;
import java.lang.*;

public class WServer {
    /**
     * In the main method, the sockets are created and the port is assigned. The connection is created.
     * atribute serverSocket ServerSocket that will indicate the port used
     * atribute clientSocket ClientSocket that will be used to communicate and accept the connection
     * atribute con Connection object that will run with the clientsocket and will create the thread
     * @param args arguments
     * @throws IOException exception
     */
    public static void main(String[] args) throws IOException {

        ServerSocket serverSocket;
        Socket clientSocket;
        serverSocket = new ServerSocket(8111);

        while(true){
            //Client connection
            clientSocket = serverSocket.accept();
            //Created a new connection with the server
            Connection con;
            con = new Connection(clientSocket);

            try {
                con.run();
                con.join();
             }catch (Exception e){
                e.printStackTrace();
             }
        clientSocket.close();
        }
    }
}
