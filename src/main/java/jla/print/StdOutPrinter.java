package jla.print;

/**
 * Implementation of {@link IPrinter} that prints everything to
 * {@link System#out}.
 *
 * @author Timothy Hoffman
 */
public enum StdOutPrinter implements IPrinter {

    INST;

    @Override
    public void print(String s) {
        System.out.print(s);
    }

    @Override
    public void open() {
        //nothing to do
    }

    @Override
    public void close() {
        //nothing to do
    }
}
