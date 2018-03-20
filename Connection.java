
import java.io.*;
import java.net.*;
import java.lang.*;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
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
                is = new FileInputStream(req.file);

                if (req.isGzip()){
                    os = new FileOutputStream(filename+".gz");
                    os = new GZIPOutputStream(os);
                }
                if (req.isZip()){
                    os = new FileOutputStream(filename+".zip");
                    os = new ZipOutputStream(os);
                    ((ZipOutputStream) os).putNextEntry(new ZipEntry(req.filename));
                }
                int c;
                while ((c = is.read()) != -1){
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
