import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.net.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.lang.*;
import java.util.Scanner;
// http://127.0.0.1:8111/index.html?asc=true

public class WServer {

    public static void main(String[] args) {

        ServerSocket serverSocket;
        Socket clientSocket;
        FileInputStream fis = null;
        File im = new File("java/files/D.png");
        try {
            fis = new FileInputStream(im);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        InputStream is = null;
        OutputStream os = null;
        BufferedReader in = null;

        try{
            serverSocket = new ServerSocket(8111);
            String fileIndex = new String(Files.readAllBytes(Paths.get("java/files/index.html")));
            String filetxt = new String(Files.readAllBytes(Paths.get("java/files/index.txt")));
            byte image[] = new byte[(int)im.length()];
            fis.read(image);


            while(true){
                //Client connection
                clientSocket = serverSocket.accept();

            // PrintWriter better than BufferedWriter
                Conexio html;
                html = new Conexio();
                try {
                    html.run(clientSocket,fileIndex,filetxt,image,is,os,in);
                    html.join();
                 }catch (Exception e){
                    e.printStackTrace();
                 }
            clientSocket.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
/*
    public String fileReader(String file) {

        StringBuilder text = new StringBuilder();
        String NL = System.getProperty("line.separator");
        Scanner scanner;
        try {
            scanner = new Scanner(new FileInputStream(file));
            while (scanner.hasNextLine()) {
                text.append(scanner.nextLine() + NL);
            }

            return text.toString();
        } catch (FileNotFoundException e) {
            System.out.println("ERROR: File Not Found");
            System.exit(-1);
            return null;
        }
    }*/


}
