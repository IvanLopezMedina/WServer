/**
 * This class will do all the the sending and receiving requests, will also send the files requested
 * Asks and send files
 * Some methods have void returns
 *
 * @author Ivan - Miquel
 * @version 9876655
 */
import java.io.*;
import java.net.*;
import java.lang.*;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Connection extends Thread {
    /**
     * The atribute clientSocket is the socket that communicate with the client
     * The atribute out is a printwriter to print
     * The atribute filename will contain the name of the file requested
     * The atribute in is the bufferreader from the inputstream
     * The atribute os will contain the outpustream to print into the client browser
     * The object req runs all the header comprobations and all the file manipulations
     */
    Socket clientSocket;
    PrintWriter out;
    String filename;
    InputStream is;
    BufferedReader in;
    OutputStream os;
    String header_t[];
    Request req;

    /**
     * Constructor
     * @param Socket contains the client socket that we got in the WServer class
     */
    public Connection (Socket Socket){
        try {
            this.clientSocket = Socket;
            is = clientSocket.getInputStream();
            os = clientSocket.getOutputStream();
            // Buffer read file data
            in = new BufferedReader(new InputStreamReader(is));

            //read and filter header
            req = new Request( in.readLine());
            req.filterReq();
            filename = req.getFilename();
            if (!filename.isEmpty()) filename = filename.substring(1);

        }catch (IOException e){
            e.printStackTrace();
        }catch (NullPointerException e){
            e.printStackTrace();
        }
    }

    /**
     * Method that sends the file to the client in the format that they ask in the request
     * @throws IOException exception
     */
    public void send() throws IOException{
            System.out.println(" Header " + filename);
            if (filename.contains("favicon.ico")) {
            } else {
                if (req.sendResponse(os)) {
                    is = new FileInputStream(req.file);

                    if (req.isAsc() && (filename.contains(".html") || filename.contains("txt")))
                        is = new AsciiInputStream(new FileInputStream(req.file));

                    if (req.isGzip()) os = new GZIPOutputStream(os);

                    if (req.isZip()) {
                        os = new ZipOutputStream(os);
                        if (req.isAsc()) {
                            ((ZipOutputStream) os).putNextEntry(new ZipEntry(filename + ".asc"));
                        } else {
                            ((ZipOutputStream) os).putNextEntry(new ZipEntry(filename));
                        }
                    }
                    int c;
                    while ((c = is.read()) != -1) os.write(c);
                    os.flush();
                }
            }
            os.flush();
            os.close();
    }
    /**
     * Create threads to filter the URL, analyse the request and send the response with the files
     */
    public void run (){
        try {
            this.send();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
