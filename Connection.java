
import java.io.*;
import java.net.*;
import java.lang.*;


public class Connection extends Thread {

    Socket clientSocket;

    PrintWriter out;

    //Files
    String filename;
    InputStream is;
    BufferedReader in;

    //Net Comm
    OutputStream os;
    String header_t[];
    FileIO file;

    Request req;


    public Connection (Socket Socket){
        try {
            this.clientSocket = Socket;
            is = clientSocket.getInputStream();
            os = clientSocket.getOutputStream();
            // Buffer read file data
            in = new BufferedReader(new InputStreamReader(is));


            //read and filter header
            req = new Request( in.readLine());
            req.filter_Req();
            filename = req.get_filename();
            file = new FileIO();



        }catch (IOException e){
            e.printStackTrace();
        }

    }

    public void run () {
        try {
            System.out.println(" Header " + filename);
            if (filename.contains("favicon.ico")) {
            } else {
                req.send_Response(os);
                file.write_stream(os,req);
            }
            os.flush();
            os.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
