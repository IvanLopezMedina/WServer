import java.awt.image.BufferedImage;
import java.io.*;
import java.net.*;
import java.lang.*;
import java.nio.file.Files;
public class Conexio extends Thread {


    public void transf_txt(String File_req,OutputStream os,String filetxt){
        PrintWriter out = null;
        out = new PrintWriter(new OutputStreamWriter(os));
        out.println("HTTP/1.1 200 OK");
        System.out.println("Fitxer de text: index.txt");
        out.println("Content-type: text/plain\n");
        out.append(filetxt);
        out.println("Fi transferencia");
        out.flush();
        out.close();
    }
    public void transf_html(String File_req,OutputStream os,String fileIndex){
        PrintWriter out = null;
        out = new PrintWriter(new OutputStreamWriter(os));
        out.println("HTTP/1.1 200 OK");
        System.out.println("Fitxer HTML: index.html");
        out.println("Content-Type: text/html\n");
        out.println(fileIndex);
        out.println("Fi transferencia");
        out.flush();
        out.close();
    }
    public void transf_imatges(String File_req,OutputStream os,byte []image){
            try {
                PrintStream pi = new PrintStream(os);
                pi.print("HTTP/1.1 200 OK\n");
                pi.print("Content-Type: image/png\n\n");
                pi.print("Content-Disposition: filename=\"D.png\"\n\n");
                pi.flush();
                pi.close();
                os.write(image, 0, image.length);
            }catch(IOException e){
                e.printStackTrace();
            }

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



    public void run (Socket clientSocket, String fileIndex, String filetxt, byte []image, InputStream is, OutputStream os, BufferedReader in) {
        try {
            is = clientSocket.getInputStream();
            os = clientSocket.getOutputStream();

            // Buffer read file data
            in = new BufferedReader(new InputStreamReader(is));

            String header = in.readLine();
            //Read browser request
            System.out.println(header);

            /*
        //Filter ASCI
        if (header.contains("asc=true")){
            System.out.println("ASCI CODE");
        }
        //Filter ZIP
        if (header.contains("zip=true")){
            System.out.println("ZIP CODE");
        }
        //Filter GZIP
        if (header.contains("gzip=true")){
            System.out.println("GZIP CODE");
        }
        */

        //Control del thread

        //Filtre de tipus d'arxiu

            if (header.contains(".txt")){
               transf_txt(header,os,filetxt);
            }
            else if (header.contains(".html")){
                transf_html(header,os,fileIndex);
            }
            else if (header.contains(".png")) {
                transf_imatges(header,os,image);
            }
            else {
                not_found(os);
            }
            os.flush();
            os.close();
        }catch (IOException e){
            e.printStackTrace();
        }


        //out.println("Content-Disposition: filename");

    }
}
