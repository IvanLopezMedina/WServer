/**
 * This class will send all the headers and will analyse the URL
 * Analyse the URL, and sends zip, gzip, asc headers and filenames
 * Some methods have void returns
 *
 * @author Ivan - Miquel
 * @version 9876655
 */
import java.io.*;

public class Request {

    /**
     * The atribute contains all the header that the client sends with the URL
     * The atribute header[t] contains a part of the header in which the filename and arguments are included
     * The atribute filename will contain the name of the file
     * The atribute tipus will contain if we are working with zip, gzip or asc
     * The atribute asc is a boolean that tells if the client want the code in asc
     * The atribute zip is a boolean that tells if the client wants the file compressed in zip
     * The atribute gzip is a boolean that tells if the client wants the file compressed in gzip
     */
    String header;
    String header_t[];
    String filename;
    String tipus;
    File file;
    boolean asc;
    boolean zip;
    boolean gzip;
    PrintWriter header_response;

    /**
     * Constructor that filters the request and splits it to get all the data that is needed
     * @param header is the header received from the client with the info to provide his file
     */
    Request(String header){
        try {
            this.header = header;
            header_t = header.split(" ");
            filename = getFilename();
            file = new File("java/files"+filename);

        }catch (NullPointerException e){
            e.printStackTrace();
        }
    }
    /**
     * Method that provides info about the client preferences
     * @return Returns a boolean true if the requestor wants the file in asc
     */
    boolean isAsc(){
        return this.asc;
    }
    /**
     * Method that provides info about the client preferences
     * @return Returns a boolean true if the requestor wants the file in zip
     */
    boolean isZip(){
        return this.zip;
    }
    /**
     * Method that provides info about the client preferences
     * @return Returns a boolean true if the requestor wants the file in gzip
     */
    boolean isGzip(){
        return this.gzip;
    }

    /**
     * Method that is played when the file requested does not exist
     * @param os Outputstream to tell the client that the file is not found sending the header 404
     * @return Returns a boolean true if the file is not found
     */
    public Boolean notFound(OutputStream os){
        header_response = new PrintWriter(new OutputStreamWriter(os));
        header_response.println("HTTP/1.1 404 Not Found");
        header_response.flush();
        return true;
    }
    /**
     * Method getter to know the kind of file
     * @return tipus
     */
    String getTipus (){
        return tipus;
    }

    /**
     * Method that filters all the data needed, sends all the header with zip,asc,gzip and which kind of file
     * @param os Outputstream to tell the client that the file is not found sending the header 404
     * @return Returns a boolean true if the response is send
     */
    public Boolean sendResponse(OutputStream os){
        try {
            header_response = new PrintWriter(new OutputStreamWriter(os));
            if (file.exists()) {
                header_response.println("HTTP/1.1 200 OK");
                //Tipus String contains the string to append to the HTTP Response Header
                if (filename.contains("png")) {
                    tipus = "image/png";
                } else if (filename.contains("txt")) {
                    tipus = "text/plain";
                } else if (filename.contains("html")) {
                    tipus = "text/html";
                } else if (filename.contains("jpeg") || filename.contains("jpg")) {
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
                if ((zip || gzip)){
                    header_response.print("Content-Disposition: filename=\"" + filename.substring(1));
                    if (asc && (header.contains("txt") || header.contains("html"))) header_response.print(".asc");
                }
                if (zip && !gzip) {
                    header_response.print(".zip\"\n");
                    header_response.print("Content-Length: " + (int) file.length()+1 + "\n\n");
                    System.out.println(file.length());
                } else if (!zip && gzip) {
                    header_response.print( ".gz\"\n");
                    header_response.print("Content-Length: " + (int) file.length()+1 + "\n\n");
                } else if (zip && gzip) {
                    header_response.print( ".zip.gz\"\n");
                    if (asc && (header.contains("txt") || header.contains("html"))) header_response.print(".asc");
                    header_response.print("Content-Length: " + (int) file.length()+1 + "\n\n");
                } else if (!tipus.contains("text") && !tipus.contains("html") && (zip || gzip)) {
                    header_response.print('\n');
                    header_response.print("Content-Length: " + (int) file.length() + "\n\n");
                } else {
                    header_response.print('\n');
                }

            } else {
                notFound(os);
                return false;
            }
            header_response.flush();

        }catch(NullPointerException e){
            e.printStackTrace();
        }
        return true;
    }

    /**
     * Method that filters the header and extract zip,gzip and asc
     * @return void
     */
    void filterReq(){
        try {
            //Filter ASCI
            if (header.contains("asc=true")) {
                System.out.println("ASCI CODE");
                asc = true;
            }
            if (header.contains("gzip=true")) {
                System.out.println("GZIP CODE");
                gzip = true;
                header = header.replace("gzip=true", "");

            }
            //Filter ZIP
            if (header.contains("zip=true")) {
                System.out.println("ZIP CODE");
                zip = true;
            }
        }catch(NullPointerException e){
            e.printStackTrace();
        }
    }

    /**
     * Method that gets the filename requested
     * @return String fith the filename
     */
    public String getFilename(){
        String file = new String();
            try {
                if (header_t[1].contains("?")) {
                    int i = header_t[1].indexOf('?');
                    file = header_t[1].substring(0, i);
                    System.out.println(file);
                } else {
                    file = header_t[1];
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        return file;
    }
}
