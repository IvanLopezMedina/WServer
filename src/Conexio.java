import java.io.*;
import java.net.*;
import java.lang.*;
public class Conexio extends Thread {

    public void run (Socket clientSocket,String fileIndex,String filetxt,InputStream is,OutputStream os,BufferedReader in) {
        try {
            is = clientSocket.getInputStream();
            os = clientSocket.getOutputStream();

        // Buffer read file data
        in = new BufferedReader(new InputStreamReader(is));

        String header = in.readLine();
        //Read browser request

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
        PrintWriter out = null;
        out = new PrintWriter(new OutputStreamWriter(os));
        //Control del thread
        System.out.println(header);

        //Filtre de tipus d'arxiu
            out.println("HTTP/1.1 200 OK");
            if (header.contains(".txt")){
                System.out.println("Fitxer de text: index.txt");
                out.println("Content-type: text/plain\n");
                out.append(filetxt);
            }
            else if (header.contains(".html")){
                System.out.println("Fitxer HTML: index.html");
                out.println("Content-Type: text/html\n");
                out.println(fileIndex);
            }
            else {

            }

        //out.println("Content-Disposition: filename");

        out.println("Fi transferencia");
        out.flush();
        out.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
