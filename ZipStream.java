import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
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
                this.zip = new ZipOutputStream(this.gzip);
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
            if(is_zip) {
                ZipEntry ini_zip = new ZipEntry(req.filename.substring(1));
                //LOOP for transfering file
                zip.putNextEntry(ini_zip);
                int bytesRead;
                int num = 0;
                while ((bytesRead = buff.read(buffer, 0, 1)) != -1) {
                    System.out.println("Escribiendo caracter num "+num+": "+(char)buffer[0]);
                    zip.write(buffer, 0, 1);
                    if (is_gzip && !is_zip) {
                        gzip.write(buffer, 0, 1);
                    }
                    num++;
                }
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



}

