package jla.analyzer;

import java.util.Iterator;

/**
 * A container to iterate a list of class names.
 *
 * @author Timothy Hoffman
 */
public interface IClassHolder extends Iterable<String> {

    /**
     *
     * @return a {@link Iterator} over all class names in this
     *         {@link IClassHolder}
     */
    @Override
    public Iterator<String> iterator();

    /**
     * @return a short descriptor for this {@link IClassHolder} that is safe for
     *         inclusion in a file name
     */
    //NOTE: this signature align with the name() method in java.lang.Enum
    //  allowing less boilerplate for IFilter instances declared as enums.
    public String name();
}
