import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipStream {

    ZipOutputStream zip;
    GZIPOutputStream gzip;
    boolean is_gzip;
    boolean is_zip;

    ZipStream(OutputStream o,boolean gzip,boolean zip){
        try {
            is_gzip = gzip;
            is_zip = zip;

            if (is_gzip) {
                this.gzip = new GZIPOutputStream(o);
                if(is_zip) this.zip = new ZipOutputStream(this.gzip);
            }
            else {
                this.zip = new ZipOutputStream(o);
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    void compress_write(byte file[], int filesize, Request req, BufferedInputStream buff){
        try {
            byte[] buffer = new byte[filesize];

                if (is_zip) {
                    ZipEntry ini_zip = new ZipEntry(req.filename.substring(1));
                    //LOOP for transfering file
                    zip.putNextEntry(ini_zip);
                }
                    int bytesRead;
                    int num = 0;
                    while ((bytesRead = buff.read(buffer, 0, 1)) != -1) {
                        System.out.println("Escribiendo caracter num " + num + ": " + (char) buffer[0]);

                        if (is_gzip && !is_zip) {
                            gzip.write(buffer, 0, 1);
                        }
                        else zip.write(buffer,0,1);
                        num++;
                    }

                if(is_zip){
                    zip.closeEntry();
                    zip.flush();
                    zip.close();
                }
            if(is_gzip) {
                gzip.flush();
                gzip.close();
            }

        }catch (IOException e){
            e.printStackTrace();
        }
    }

    void compress_txt(Request req){
        try {

            File file = new File("java/files"+req.filename);
            FileInputStream fis = new FileInputStream(file);
            byte []bytes = new byte[(int)file.length()];
            fis.read(bytes);

            if(is_zip) {
                this.zip.putNextEntry(new ZipEntry(file.getName()));
                zip.write(bytes, 0, bytes.length);
                zip.closeEntry();
                zip.flush();
                zip.close();
            }
            else{

                gzip.write(bytes,0,bytes.length);
                gzip.flush();
                gzip.finish();
            }

        }catch ( IOException e){
            e.printStackTrace();
        }
    }



}

