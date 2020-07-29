package jla.print;

import java.io.Closeable;

/**
 * Interface for printing {@link String Strings}. The implementation defines
 * where they are actually printed.
 *
 * @author Timothy Hoffman
 */
public interface IPrinter extends Closeable {

    public void open();

    @Override //removed IOException
    public void close();

    public void print(String s);
}
