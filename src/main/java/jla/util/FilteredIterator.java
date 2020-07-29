package jla.util;

import java.util.Iterator;
import java.util.NoSuchElementException;
import jla.filter.IFilter;

/**
 *
 * @author Timothy Hoffman
 * @param <T>
 */
public class FilteredIterator<T> implements Iterator<T> {

    private final Iterator<? extends T> iterator;
    private final IFilter<T> filter;
    private T nextElement;
    private boolean hasNext;//separate flag so 'null' elements can be supported

    /**
     * Creates a new {@link FilteredIterator} wrapping the given
     * {@link Iterator} and returning only elements matching the
     * {@link IFilter}.
     *
     * @param iterator the iterator to wrap
     * @param filter   elements must match this filter to be returned
     */
    public FilteredIterator(Iterator<? extends T> iterator, IFilter<T> filter) {
        this.iterator = iterator;
        this.filter = filter;
        nextMatch();
    }

    @Override
    public boolean hasNext() {
        return hasNext;
    }

    @Override
    public T next() {
        if (!hasNext) {
            throw new NoSuchElementException();
        }
        return nextMatch();
    }

    private T nextMatch() {
        T oldMatch = nextElement;
        while (iterator.hasNext()) {
            T o = iterator.next();
            if (filter.accept(o)) {
                hasNext = true;
                nextElement = o;
                return oldMatch;
            }
        }
        hasNext = false;
        return oldMatch;
    }

    @Override
    public void remove() {
        //It's not possible to support remove given just an Iterator. The inner
        //  Iterator has ALWAYS advanced beyond the last element from #next().
        throw new UnsupportedOperationException();
    }
}
