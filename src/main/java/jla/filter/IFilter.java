package jla.filter;

/**
 * @author Timothy Hoffman
 * @param <T>
 */
public interface IFilter<T> {

    /**
     * @param m
     *
     * @return {@code true} iff the given Object should be accepted by this
     *         filter
     */
    public boolean accept(T m);

    /**
     * @return a short name for this filter that is safe for inclusion in a file
     *         name
     */
    //NOTE: this signature aligns with the name() method in java.lang.Enum
    //  allowing less boilerplate for IFilter instances declared as enums!
    public String name();
}
