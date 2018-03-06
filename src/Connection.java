import com.sun.org.apache.xpath.internal.operations.Bool;

import java.awt.image.BufferedImage;
import java.io.*;
import java.net.*;
import java.lang.*;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.net.HttpURLConnection.*;
import java.net.URL;

public class Connection extends Thread {

    PrintWriter out;

    //Filtres ASC/ZIP/GZIP
    boolean asc = false;
    boolean zip = false;
    boolean gzip = false;

    //Files
    String filename;

    //Net Comm
    OutputStream os;
    String req_header;

    public void transf_txt(String header_t[]){
        try {
            //read source file
            String filesize = new String(Files.readAllBytes(Paths.get("java/files" + filename)));
            out = new PrintWriter(new OutputStreamWriter(os));
            out.println("HTTP/1.1 200 OK");
            System.out.println("Fitxer de text: "+filename.substring(1));
            out.println("Content-type: text/plain\n");
            //transfer file to Outputstream
            out.append(filesize);
            System.out.println("Fi transferencia");

        }catch (NullPointerException e){
            System.out.println("Excepcio no controlada");
        }catch (NoSuchFileException e){
            not_found();
            //Currently not working
        }catch (IOException e){
            not_found();
            e.printStackTrace();
        }
        out.flush();
        out.close();
    }

    public void transf_html(String header_t[]){
        try {
            //read source file
            String filesize = new String(Files.readAllBytes(Paths.get("java/files" + filename)));
            //Declare Printwriter
            out = new PrintWriter(new OutputStreamWriter(os));
            out.println("HTTP/1.1 200 OK");
            System.out.println("Fitxer HTML: "+filename.substring(1));
            out.println("Content-Type: text/html\n");
            //Print file
            out.println(filesize);
            out.println("Fi transferencia");

        }catch (NullPointerException e){
            System.out.println("Excepcio no controlada");
        }catch (NoSuchFileException e){
            not_found();
            //Currently not working
        }catch (IOException e){
            not_found();
            e.printStackTrace();
        }
        out.flush();
        out.close();
    }

    public void transf_file(String header_t[]){
        try {
            System.out.println("Requesting File: "+filename);
            //Open file
            File f = new File("java/files"+filename);

            //Open File Stream
            BufferedInputStream in = new BufferedInputStream(new FileInputStream(f));
            //Open Output Header Stream
            PrintStream pi = new PrintStream(os);
            pi.print("HTTP/1.1 200 OK\n");
            /*
                With this line we download the file, but the size and the data is not correct
                String filesize = new String(Files.readAllBytes(Paths.get("java/files" + filename)));
                pi.print(filesize);
                Type Filter:(agregar tipus)
            */

            String tipus = new String();
            //Tipus String contains the string to append to the HTTP Response Header
            if (filename.contains("png")){
                tipus = "image/png";
            }
            else if (filename.contains("jpeg")){
                tipus = "image/jpeg";

            }
            else if (filename.contains("gif")){
                tipus = "image/gif";
            }
            pi.print("Content-Type: "+tipus+"\n");
            pi.print("Content-Disposition: filename=\""+filename.substring(1)+"\"\n");
            pi.print("Content-Length: "+(int)f.length()+"\n\n");

            //Flush Header to Output Stream
            pi.flush();
            //LOOP for transfering file
            byte[] buffer = new byte[(int) f.length() ];
            int bytesRead;
            while ( (bytesRead = in.read( buffer )) != -1 ) {
                os.write( buffer, 0, bytesRead );
            }
            pi.close();
        }catch (NullPointerException e){
            System.out.println("Excepcio no controlada");
        }catch (NoSuchFileException e){
            not_found();
            //Currently not working
        }catch(IOException e){
            e.printStackTrace();
        }

    }

    //Returns Filename for when there are tags in the Header(asc,zip...)
    public String get_filename(String header_t[]){
        String file = new String();
        if(header_t[1].contains("?")) {
            int i = header_t[1].indexOf('?');
            file = header_t[1].substring(0,i);
            System.out.println(file);
        }
        else{
            file = header_t[1];
        }
        return file;
    }

    public void not_found(){
        out = new PrintWriter(new OutputStreamWriter(os));
        out.println("HTTP/1.1 404 Not Found");
        out.println("Fi transferencia");
        out.flush();
        out.close();
    }


    //Client main method
    public void run (Socket clientSocket) {
        //Definitions
        InputStream is;
        BufferedReader in;

        try {
            is = clientSocket.getInputStream();
            os = new BufferedOutputStream(clientSocket.getOutputStream());
            // Buffer read file data
            in = new BufferedReader(new InputStreamReader(is));
            String header = in.readLine();
            //Read browser request
            System.out.println(header);

            //Filter ASCI
            if (header.contains("asc=true")){
                System.out.println("ASCI CODE");
                asc = true;
            }
            //Filter ZIP
            if (header.contains("zip=true")){
                System.out.println("ZIP CODE");
                zip = true;
            }
            //Filter GZIP
            if (header.contains("gzip=true")){
                System.out.println("GZIP CODE");
                gzip = true;
            }

            //Control del thread

            //First Header Split we get requestmethod, filename, protocol
            String header_t[] = header.split(" ");
            //get file name
            String filename = get_filename(header_t);
            if (header.contains("favicon.ico")) { }
            else if (header.contains(".txt")){
               transf_txt(header_t);
            }
            else if (header.contains(".html")){
                transf_html(header_t);
            } else {
                transf_file(header_t);
            }

            os.flush();
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
