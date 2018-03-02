import java.net.*;
import java.io.*;
import java.lang.*;
// http://127.0.0.1:8111/index.html?asc=true

public class WServer {

    public static void main(String[] args) {

        ServerSocket serverSocket;
        Socket clientSocket;

        try{
            serverSocket = new ServerSocket(8111);
            while(true){
                //Client connection
                clientSocket = serverSocket.accept();
                //Created a new connection with the server
                Connection con;
                con = new Connection();
                try {
                    con.run(clientSocket);
                    con.join();
                 }catch (Exception e){
                    e.printStackTrace();
                 }
            clientSocket.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
