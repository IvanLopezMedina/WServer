
import java.io.*;
import java.net.*;
import java.lang.*;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipOutputStream;


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
                FileInputStream fis = new FileInputStream(req.file);
                if (req.isGzip()){
                    os = new GZIPOutputStream(os);

                }
                if (req.isZip()){
                    os = new ZipOutputStream(os);
                }

                int c;
                while ((c = fis.read()) != -1){
                    os.write(c);
                }
                os.flush();
            }
            os.flush();
            os.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
