package jla.print;

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

/**
 * Implementation of {@link IPrinter} that prints everything to a file with the
 * given name.
 *
 * @author Timothy Hoffman
 */
public class FilePrinter implements IPrinter {

    private final String fileName;
    private PrintStream stream;

    public FilePrinter(String fileName) {
        this.fileName = fileName;
        this.stream = null;
    }

    @Override
    public void open() {
        if (stream == null) {
            try {
                stream = new PrintStream(new BufferedOutputStream(new FileOutputStream(fileName)));
            } catch (FileNotFoundException ex) {
                stream = null;
                throw new RuntimeException(ex);
            }
        }
    }

    @Override
    public void close() {
        if (stream != null) {
            stream.close();
            stream = null;
        }
    }

    @Override
    public void print(String s) {
        if (stream != null) {
            stream.print(s);
        } else {
            throw new RuntimeException("Stream is not open");
        }
    }
}
