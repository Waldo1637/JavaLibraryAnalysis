package jla.filter;

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
