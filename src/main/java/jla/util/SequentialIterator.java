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

import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Provides functionality to traverse multiple Iterators in sequence. That is,
 * the {@link #next()} method will return elements from
 * {@link Iterator} {@code n} until that Iterator becomes empty. Then it will
 * return elements from Iterator {@code n+1}.
 *
 * @author Timothy Hoffman
 * 
 * @param <N>
 */
public class SequentialIterator<N> implements Iterator<N> {

    private final Iterator<Iterator<? extends N>> superItr;
    private final Iterator<? extends Iterable<? extends N>> superItrb;
    private Iterator<? extends N> currItr;
    private Iterator<? extends N> removePtr;

    @SafeVarargs
    public SequentialIterator(Iterator<? extends N>... iterators) {
        this(Arrays.asList(iterators));
    }

    public SequentialIterator(Iterable<Iterator<? extends N>> iterators) {
        this.superItr = iterators.iterator();
        this.superItrb = null;
        this.currItr = superItr.hasNext() ? superItr.next() : null;
        this.removePtr = null;
    }

    @SafeVarargs
    public SequentialIterator(Iterable<? extends N>... iterables) {
        this(Arrays.asList(iterables).iterator());
    }

    public SequentialIterator(Iterator<? extends Iterable<? extends N>> iterables) {
        this.superItr = null;
        this.superItrb = iterables;
        this.currItr = superItrb.hasNext() ? superItrb.next().iterator() : null;
        this.removePtr = null;
    }

    private Iterator<? extends N> getCurrentIterator() {
        //If the current iterator is not null (which will be true until we reach
        //  the end of the last iterator) but has no more elements, then proceed
        //  to the next iterator (or null if the final iterator is completed).
        while (currItr != null && !currItr.hasNext()) {
            if (superItr == null) {
                this.currItr = superItrb.hasNext() ? superItrb.next().iterator() : null;
            } else {
                this.currItr = superItr.hasNext() ? superItr.next() : null;
            }
        }
        return currItr;
    }

    @Override
    public boolean hasNext() {
        Iterator<? extends N> itr = getCurrentIterator();
        return itr == null ? false : itr.hasNext();
    }

    @Override
    public N next() {
        removePtr = getCurrentIterator();
        if (removePtr == null) {
            throw new NoSuchElementException();
        }
        return removePtr.next();
    }

    @Override
    public void remove() {
        if (removePtr == null) {
            throw new IllegalStateException();
        } else {
            removePtr.remove();
            removePtr = null;
        }
    }
}
