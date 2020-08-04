package jla.filter;

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

/**
 * @author Timothy Hoffman
 */
public enum BasicFilters implements IFilter<Object> {

    /**
     * {@link IFilter} that accepts everything.
     */
    ALL {
        @Override
        public boolean accept(Object m) {
            return true;
        }
    },
    /**
     * {@link IFilter} that accepts nothing.
     */
    NONE {
        @Override
        public boolean accept(Object m) {
            return false;
        }
    };

    @SuppressWarnings("unchecked")
    public static <N> IFilter<N> all() {
        return (IFilter<N>) ALL;
    }

    @SuppressWarnings("unchecked")
    public static <N> IFilter<N> none() {
        return (IFilter<N>) NONE;
    }

    /**
     * @param <N>
     * @param one
     * @param two
     *
     * @return
     */
    public static <N> IFilter<N> or(IFilter<? super N> one, IFilter<? super N> two) {
        if (one == two) {
            @SuppressWarnings("unchecked")
            IFilter<N> ret = (IFilter<N>) one;
            return ret;
        } else {
            return new IFilter<N>() {
                @Override
                public boolean accept(N m) {
                    return one.accept(m) || two.accept(m);
                }

                @Override
                public String name() {
                    return one.name() + "-OR-" + two.name();
                }
            };
        }
    }

    /**
     * @param <N>
     * @param one
     * @param two
     *
     * @return
     */
    public static <N> IFilter<N> and(IFilter<? super N> one, IFilter<? super N> two) {
        if (one == two) {
            @SuppressWarnings("unchecked")
            IFilter<N> ret = (IFilter<N>) one;
            return ret;
        } else {
            return new IFilter<N>() {
                @Override
                public boolean accept(N m) {
                    return one.accept(m) && two.accept(m);
                }

                @Override
                public String name() {
                    return one.name() + "-AND-" + two.name();
                }
            };
        }
    }
}
