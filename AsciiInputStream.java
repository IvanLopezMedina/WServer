import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

//Notas: Sobreescribir el metodo read. Envez de leer un caracter sobrecargar
public class AsciiInputStream extends FilterInputStream {

    /**
     * Creates a <code>FilterInputStream</code>
     * by assigning the  argument <code>in</code>
     * to the field <code>this.in</code> so as
     * to remember it for later use.
     *
     * @param in the underlying input stream, or <code>null</code> if
     *           this instance is to be created without an underlying stream.
     */

    public AsciiInputStream(InputStream in) {
        super(in);
        this.in = in;
    }

    StringBuilder buffer = new StringBuilder();
    String chain = "<!--";
    String endchain = "-->";
    Boolean startcomment = false;
    Boolean endcomment = false;

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