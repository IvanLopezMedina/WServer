import java.io.FilterInputStream;
import java.io.InputStream;

public class AsciiInputStream extends FilterInputStream{


    /**
     * Creates a <code>FilterInputStream</code>
     * by assigning the  argument <code>in</code>
     * to the field <code>this.in</code> so as
     * to remember it for later use.
     *
     * @param in the underlying input stream, or <code>null</code> if
     *           this instance is to be created without an underlying stream.
     */
    protected AsciiInputStream(InputStream in) {
        super(in);
        AsciiInputStream asc;


    }


    public int read(byte b[]){
        FilterInputStream f;
        //f.read();

    }
    public void convertToAsci(){
//Sobreescribir el metodo read. Envez de leer un caracter, sobrecargar.
    }

}
