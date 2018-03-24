/**
 * Class that overrides the read from inputstream, that way we can modify the method and filter some characters that we dont want to read
 * @author Ivan Lopez Medina - Miquel Martin Ezquerra
 * @version 999999999
 */
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

public class AsciiInputStream extends FilterInputStream {

    /**
     * atribute buffer String that saves a string with all the data from the file to analyse it. It resets the chain when the comment ends
     * atribute chain String that let us know when the commentary in a html file begins
     * atribute endchain String that let us know when the commentary in a html file ends
     * @param in Inputstream to read the data of the file
     */

    /**
     * Constructor that assigns the inputstream and the polymorphism with the super class InputStream
     * @param in Inputstream to read the data of the file
     */

    public AsciiInputStream(InputStream in) {
        super(in);
        this.in = in;
    }

    StringBuilder buffer = new StringBuilder();
    String chain = "<!--";
    String endchain = "-->";

    /**
     * Method that overwrites the read method to add the functionality to not write html comments and tags
     * It filters for html tags with the start of the comment and stop reading them until the close comment is found.
     * For the comments, we save all the text and look for the chain, and endchain Strings
     * @return Returns the asci code of the character read
     */
    public int read() throws IOException {
        int c = this.in.read();
        buffer.append(((char) c));
        if (c == '<') {
            while ((c = this.in.read()) != '>') {
                if (c == -1){
                    return -1; //cuando se llega al final
                }else{
                    buffer.append(((char) c));
                    if(buffer.toString().contains(chain)){
                        while(!buffer.toString().contains(endchain)){
                            c = this.in.read();
                            buffer.append(((char) c));
                        }
                        buffer = new StringBuilder();
                    }
                }
            }
            if (c == '>') c = this.in.read();
        }
        return c;
    }
}