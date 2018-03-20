import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.*;
import java.lang.*;
import java.nio.file.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class FileIO {


    //Files
    String filename;
    File file;
    BufferedInputStream in;
    ZipStream zout;

    FileIO (Request req) {
        try {
            filename = req.get_filename();
            file = new File("java/files" + filename);

            if(file.exists()) {
                in = new BufferedInputStream(new FileInputStream(file));
            }

        }catch (IOException e){
            e.printStackTrace();
        }
        catch (NullPointerException n){
            n.printStackTrace();
        }
    }

    void write_stream(OutputStream os,Request req){
        try{
            if(req.get_tipus().contains("text") && !req.is_Zip() && !req.is_Gzip() || req.get_tipus().contains("html") && !req.is_Zip() && !req.is_Gzip()){
                PrintWriter p = new PrintWriter(new OutputStreamWriter(os));
                if (req.is_Asc()){
                    InputStream inputaux = new FileInputStream("java/files"+req.get_filename());
                    InputStream inputasc = new AsciiInputStream(inputaux);
                    int c;
                    while ((c = inputasc.read())!= -1) p.write(c);
                }
                else{
                    p.println(new String(Files.readAllBytes(Paths.get("java/files"+filename))));
                }
                p.flush();

            }
            else {
                byte[] buffer = new byte[(int) file.length()];

                if(!req.is_Gzip() && !req.is_Zip()){

                    int bytesRead;
                    while ((bytesRead = in.read(buffer)) != -1) {
                        os.write(buffer, 0, bytesRead);
                    }

                }
                else {
                    zout = new ZipStream(os,req.is_Gzip(),req.is_Zip());

                    if (req.filename.contains("txt")||req.filename.contains("html")){
                        zout.compress_txt(req);
                    }
                    else{
                        zout.compress_write(buffer, (int) file.length(), req, in);
                    }

                }

            }


        }catch (IOException e){
            e.printStackTrace();
        }
    }




}
