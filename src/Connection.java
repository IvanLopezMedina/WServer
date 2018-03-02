import java.awt.image.BufferedImage;
import java.io.*;
import java.net.*;
import java.lang.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.net.HttpURLConnection.*;
import java.net.URL;

public class Connection extends Thread {

    public void transf_txt(String header_t[], String filename, OutputStream os,boolean tags[]){
        try {
            //read source file
            String filetxt = new String(Files.readAllBytes(Paths.get("java/files" + filename)));
            PrintWriter out = null;
            out = new PrintWriter(new OutputStreamWriter(os));
            out.println("HTTP/1.1 200 OK");
            System.out.println("Fitxer de text: "+filename.substring(1));
            out.println("Content-type: text/plain\n");
            //transfer file to Outputstream
            out.append(filetxt);
            System.out.println("Fi transferencia");
            out.flush();
            out.close();
        }catch (IOException e){
            not_found(os);
            e.printStackTrace();
        }
    }

    public void transf_html(String []header_t, String filename, OutputStream os,boolean tags[]){
        try {
            //read source file
            String fileIndex = new String(Files.readAllBytes(Paths.get("java/files" + filename)));
            //Declare Printwriter
            PrintWriter out = null;
            out = new PrintWriter(new OutputStreamWriter(os));
            out.println("HTTP/1.1 200 OK");
            System.out.println("Fitxer HTML: "+filename.substring(1));
            out.println("Content-Type: text/html\n");
            //Print file
            out.println(fileIndex);
            out.println("Fi transferencia");
            out.flush();
            out.close();
        }catch (IOException e){
            not_found(os);
            e.printStackTrace();
        }
    }

    public void transf_file(String header_t[],  String filename, BufferedOutputStream os,boolean tags[]){
        try {
            System.out.println("Requesting File: "+filename);
            //Open file
            File f = new File("java/files"+filename);
            //Open File Stream
            BufferedInputStream in = new BufferedInputStream(new FileInputStream(f));
            //Open Output Header Stream
            PrintStream pi = new PrintStream(os);
            pi.print("HTTP/1.1 200 OK\n");
            //Type Filter:(agregar tipus)

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

    public void not_found(OutputStream os){
        PrintWriter out = null;
        out = new PrintWriter(new OutputStreamWriter(os));
        //out.println("HTTP/1.1 200 OK");
        out.println("HTTP/1.1 404 Not Found");
        out.println("Fi transferencia");
        out.flush();
        out.close();
    }


    //Client main method
    public void run (Socket clientSocket) {
        //Definitions
        InputStream is;
        BufferedOutputStream os;
        BufferedReader in;

        try {
            is = clientSocket.getInputStream();
            os = new BufferedOutputStream(clientSocket.getOutputStream());
            // Buffer read file data
            in = new BufferedReader(new InputStreamReader(is));
            String header = in.readLine();
            //Read browser request
            System.out.println(header);
            //Filtres tags
            boolean tags[] = new boolean[3];

            //Filter ASCI
            if (header.contains("asc=true")){
                System.out.println("ASCI CODE");
                tags[0] = true;
            }
            //Filter ZIP
            if (header.contains("zip=true")){
                System.out.println("ZIP CODE");
                tags[1] = true;
            }
            //Filter GZIP
            if (header.contains("gzip=true")){
                System.out.println("GZIP CODE");
                tags[2] = true;
            }


            //Control del thread

            //First Header Split we get requestmethod, filename, protocol
            String header_t[] = header.split(" ");
            //get file name
            String filename = get_filename(header_t);
            if (header.contains("favicon.ico")) { }
            else if (header.contains(".txt")){
               transf_txt(header_t,filename,os,tags);
            }
            else if (header.contains(".html")){
                transf_html(header_t,filename,os,tags);
            } else {
                transf_file(header_t,filename,os,tags);
            }

            os.flush();
            os.close();
        }catch (IOException e){
            e.printStackTrace();
        }

    }
}
