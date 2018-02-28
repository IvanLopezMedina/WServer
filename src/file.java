import java.io.*;

public class file {
    public static void main(String[] args) {

        try {
            FileInputStream fis = new FileInputStream("files/index.html");
            FileOutputStream fos = new FileOutputStream("files/destination.txt");
            int c;

            while ((c = fis.read()) != -1) {
                fos.write(c);
            }
            fis.close();
            fos.close();

        } catch (FileNotFoundException ioe) {
            //System.Out.Println("File not found");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}