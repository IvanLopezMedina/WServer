import java.io.*;
import java.nio.file.*;
public class Request {
    String header;
    String header_t[];
    String filename;
    String tipus;
    boolean asc;
    boolean zip;
    boolean gzip;

    PrintWriter header_response;

    Request(String header){
        this.header = header;
        header_t = header.split(" ");
        filename = get_filename();
    }

    boolean is_Asc(){
        return asc;
    }

    boolean is_Zip(){
        return zip;
    }

    boolean is_Gzip(){
        return gzip;
    }

    public void not_found(OutputStream os){
        header_response = new PrintWriter(new OutputStreamWriter(os));
        header_response.println("HTTP/1.1 404 Not Found");
    }

    String get_tipus (){
        return tipus;
    }

    void send_Response(OutputStream os){
        try {
            header_response = new PrintWriter(new OutputStreamWriter(os));
            File aux = new File("java/files" + filename);
            if (aux.exists()) {
                header_response.println("HTTP/1.1 200 OK");
                //Tipus String contains the string to append to the HTTP Response Header
                if (filename.contains("png")) {
                    tipus = "image/png";
                } else if (filename.contains("txt")) {
                    tipus = "text/plain";
                } else if (filename.contains("html")) {
                    tipus = "text/html";
                } else if (filename.contains("jpeg")) {
                    tipus = "image/jpeg";

                } else if (filename.contains("gif")) {
                    tipus = "image/gif";
                } else if (filename.contains("txt")) {
                    tipus = "text/plain";
                }
                if (zip == true) {
                    tipus = "applications/zip";
                }
                if (gzip == true) {
                    tipus = "applications/x-gzip";
                }

                header_response.print("Content-Type: " + tipus + "\n");
                if (zip == true && gzip == false) {
                    header_response.print("Content-Disposition: filename=\"" + filename.substring(1) + ".zip\"\n");
                    header_response.print("Content-Length: " + (int) aux.length() + "\n\n");
                    System.out.println(aux.length());
                } else if (zip == false && gzip == true) {
                    header_response.print("Content-Disposition: filename=\"" + filename.substring(1) + ".gz\"\n");
                    header_response.print("Content-Length: " + (int) aux.length() + "\n\n");
                } else if (zip == true && gzip == true) {
                    header_response.print("Content-Disposition: filename=\"" + filename.substring(1) + ".zip.gz\"\n");
                    header_response.print("Content-Length: " + (int) aux.length() + "\n\n");
                } else if (!tipus.contains("text") && !tipus.contains("html")) {
                    header_response.print("Content-Disposition: filename=\"" + filename.substring(1) + "\"\n");
                    header_response.print("Content-Length: " + (int) aux.length() + "\n\n");
                } else {
                    header_response.print('\n');
                }

            } else {
                not_found(os);
            }
            header_response.flush();
        }catch(NullPointerException e){
            e.printStackTrace();
        }


    }

    void filter_Req(){
        //Filter ASCI
        if (header.contains("asc=true")) {
            System.out.println("ASCI CODE");
            asc = true;
        }
        if (header.contains("gzip=true")) {
            System.out.println("GZIP CODE");
            gzip = true;
            header = header.replace("gzip=true","");

        }
        //Filter ZIP
        if (header.contains("zip=true")) {
            System.out.println("ZIP CODE");
            zip = true;
        }


    }

    //Returns Filename for when there are tags in the Header(asc,zip...)
    public String get_filename(){
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


}
