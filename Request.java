import java.io.*;

public class Request {
    String header;
    String header_t[];
    String filename;
    String tipus;
    File file;
    boolean asc;
    boolean zip;
    boolean gzip;

    PrintWriter header_response;

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

    boolean isAsc(){ return asc; }

    boolean isZip(){
        return zip;
    }

    boolean isGzip(){
        return gzip;
    }

    public void not_found(OutputStream os){
        header_response = new PrintWriter(new OutputStreamWriter(os));
        header_response.println("HTTP/1.1 404 Not Found");
    }

    String getTipus (){
        return tipus;
    }

    void sendResponse(OutputStream os){
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
                if (zip) {
                    tipus = "applications/zip";
                }
                if (gzip) {
                    tipus = "applications/x-gzip";
                }

                header_response.print("Content-Type: " + tipus + "\n");
                if (zip || gzip || (!tipus.contains("text")&&!tipus.contains("html"))){
                    header_response.print("Content-Disposition: filename=\"" + filename.substring(1));
                    if (asc && (header.contains("txt") || header.contains("html"))) header_response.print(".asc");
                }
                if (zip && !gzip) {
                    header_response.print(".zip\"\n");
                    header_response.print("Content-Length: " + (int) file.length()+1 + "\n\n");
                    System.out.println(file.length());
                } else if (!zip && gzip) {
                    header_response.print( ".gz\"\n");
                    header_response.print("Content-Length: " + (int) file.length() + "\n\n");
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
                not_found(os);
            }
            header_response.flush();
        }catch(NullPointerException e){
            e.printStackTrace();
        }


    }

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

    //Returns Filename for when there are tags in the Header(asc,zip...)
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
