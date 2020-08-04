package jla.util;

/*-
 * #%L
 * JavaLibraryAnalysis
 * %%
 * Copyright (C) 2020 Timothy Hoffman
 * %%
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * #L%
 */

import java.util.Iterator;
import java.util.NoSuchElementException;
import jla.filter.IFilter;

/**
 *
 * @author Timothy Hoffman
 * 
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
