

import java.io.InputStream;
import java.io.*;
import java.lang.*;
import java.nio.file.*;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
import java.util.stream.*;

public class FileIO {


    void write_stream(OutputStream os,Request req){
        try{
            ZipOutputStream o;
            GZIPOutputStream o2;
            if (req.is_Zip()){
                o = new ZipOutputStream(os);
                o.putNextEntry(new ZipEntry("archivo"+req.filename));
            }
            if (req.is_Gzip()) o2 = new GZIPOutputStream(os);
            FileInputStream fis = new FileInputStream(req.file);

            int c = 0;

            while ((c = fis.read()) != -1){
                if (req.is_Zip()){
                    o.write(c);
                }
                else if (req.is_Gzip()){
                    o2.write(c);
                }
                else {
                    os.write(c);
                }

            }
            if (req.is_Zip()){
                o.flush();
                o.closeEntry();
                o.close();
            }
            else if(req.is_Gzip()){
                o2.flush();
                o2.close();
            }


        }catch (IOException e){
            e.printStackTrace();
        }
        catch (NullPointerException e){
            e.printStackTrace();
        }
    }




}
