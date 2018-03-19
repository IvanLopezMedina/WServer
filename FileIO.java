import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.*;
import java.lang.*;
import java.nio.file.*;

public class FileIO {

    //Filtres ASC/ZIP/GZIP
    boolean asc = false;
    boolean zip = false;
    boolean gzip = false;

    //Files
    String filename;
    InputStream is;
    BufferedReader in;

    FileIO (Socket ClientSocket) {

    }

}
