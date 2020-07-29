package jla.filter;

/**
 * @author Timothy Hoffman
 */
public enum NameFilters implements IFilter<String> {
    /**
     * {@link IFilter} that accepts all JDK classes.
     */
    JDK {
        @Override
        public boolean accept(String className) {
            return className.startsWith("java.")
                    || className.startsWith("javax.")
                    || className.startsWith("org.ietf.jgss.")
                    || className.startsWith("org.omg.")
                    || className.startsWith("org.w3c.dom.")
                    || className.startsWith("org.xml.sax.");
        }
    };
}
