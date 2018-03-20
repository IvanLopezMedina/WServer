

import java.io.InputStream;
import java.io.*;
import java.lang.*;
import java.nio.file.*;
import java.util.zip.ZipOutputStream;

public class FileIO {


    void write_stream(OutputStream os,Request req){
        try{
            OutputStream o = os;
            FileInputStream fis = new FileInputStream(req.file);
            if (req.is_Zip()){
                o = new ZipOutputStream(os);
            }
            int c;
            while ((c = fis.read()) != -1){
                o.write(c);
            }
            o.flush();


        }catch (IOException e){
            e.printStackTrace();
        }
    }




}
