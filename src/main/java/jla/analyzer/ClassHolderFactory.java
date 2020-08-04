package jla.analyzer;

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

import java.io.*;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import jla.filter.IFilter;
import jla.main.Analyzer;
import jla.util.SequentialIterator;

/**
 * Factory for creating instances of {@link IClassHolder}.
 *
 * @author Timothy Hoffman
 */
public final class ClassHolderFactory {

    /**
     *
     */
    public static class Path {

        /**
         *
         */
        public static final List<String> BOOT = fromProperty("sun.boot.class.path");

        /**
         *
         */
        public static final List<String> JAVA = fromProperty("java.class.path");

        /**
         * @param name
         *
         * @return
         */
        public static List<String> fromProperty(String name) {
            String[] paths = System.getProperty(name).split(File.pathSeparator);
            return Collections.unmodifiableList(Arrays.asList(paths));
        }

        private Path() {
        }
    }

    private static class PathClassHolder implements IClassHolder {

        private final Iterable<String> paths;
        private final IFilter<String> classNameFilter;

        public PathClassHolder(Iterable<String> paths, IFilter<String> classNameFilter) {
            if (paths == null || classNameFilter == null) {
                throw new IllegalArgumentException("Parameters cannot be null!");
            }
            this.paths = paths;
            this.classNameFilter = classNameFilter;
        }

        @Override
        public String name() {
            return "CP-" + classNameFilter.name();
        }

        @Override
        public Iterator<String> iterator() {
            return new SequentialIterator<>(new Iterable<Iterator<? extends String>>() {
                @Override
                public Iterator<Iterator<? extends String>> iterator() {
                    return new Iterator<Iterator<? extends String>>() {

                        final Iterator<String> pathItr = paths.iterator();

                        @Override
                        public boolean hasNext() {
                            return pathItr.hasNext();
                        }

                        @Override
                        public Iterator<String> next() {
                            String nextPath = pathItr.next();
                            if (Analyzer.DEBUG) {
                                System.out.println("Opening: " + nextPath);
                            }
                            return getClassesFrom(nextPath, classNameFilter).iterator();
                        }
                    };
                }
            });
        }

        /**
         * @param path
         *
         * @return {@link List} of names of all classes found in the Zip, Jar,
         *         or directory with the given name (if it exists)
         */
        private static List<String> getClassesFrom(String path, IFilter<String> classNameFilter) {
            ArrayList<String> retVal = new ArrayList<>();
            File f = new File(path);
            if (f.isDirectory()) {
                System.err.println("[PathClassHolder] did not traverse directory: " + path);
//            f.list();//TODO: they will likely be nested within folders so traversal is a bit complex
            } else if (f.isFile()) {
                try (ZipInputStream zip = new ZipInputStream(new BufferedInputStream(new FileInputStream(f)))) {
                    for (ZipEntry entry = zip.getNextEntry(); entry != null; entry = zip.getNextEntry()) {
                        if (!entry.isDirectory() && entry.getName().endsWith(".class")) {
                            String className = entry.getName().replace('/', '.'); // including ".class"
                            className = className.substring(0, className.length() - ".class".length());
                            if (classNameFilter.accept(className)) {
                                retVal.add(className);
                            }
                        }
                    }
                } catch (IOException ex) {
                    //ignore if unable to find/read class path item
                    System.err.println("[PathClassHolder] while processing " + path + ": " + ex);
                }
            } else {
                //NOTE: it's possible the classpath could contain paths that don't exist
            }
            return retVal;
        }

    }

    /**
     * @param paths
     * @param filter
     *
     * @return {@link IClassHolder} containing classes from the given
     *         {@link List} of paths that are accepted by the given
     *         {@link IFilter}
     */
    public static IClassHolder classesFromPath(Iterable<String> paths, IFilter<String> filter) {
        Analyzer.setClasspath(paths);
        return new PathClassHolder(paths, filter);
    }

    /**
     * @param filter
     *
     * @return {@link IClassHolder} containing classes from the boot classpath
     *         that are accepted by the given {@link IFilter}
     */
    public static IClassHolder classesFromBootPath(IFilter<String> filter) {
        return classesFromPath(Path.BOOT, filter);
    }

    /**
     * @param filename
     * @param filter
     *
     * @return {@link IClassHolder} containing classes that are accepted by the
     *         given {@link IFilter} and found via traversing the paths in the
     *         given file
     */
    public static IClassHolder readClasspathsFromFile(String filename, IFilter<String> filter) {
        return classesFromPath(readAllLines(filename), filter);
    }

    /**
     * Reads class names (like "java.util.String") from the given file, one
     * class name per line.
     *
     * @param filename name of file to read classes from
     *
     * @return a new {@link IClassHolder} containing all class names from the
     *         given file
     */
    public static IClassHolder readClassNamesFromFile(String filename) {
        return new IClassHolder() {

            //cache data read from file in case iterator() is repeated
            private List<String> data;

            @Override
            public Iterator<String> iterator() {
                List<String> retVal = data;
                if (retVal == null) {
                    retVal = readAllLines(filename);
                    data = Collections.unmodifiableList(retVal);
                }
                return retVal.iterator();
            }

            @Override
            public String name() {
                return "FILE_C";
            }
        };
    }

    /**
     * @param names
     *
     * @return a new {@link IClassHolder} containing the given list of names
     */
    public static IClassHolder classNames(String[] names) {
        return new IClassHolder() {
            @Override
            public Iterator<String> iterator() {
                return Arrays.asList(names).iterator();
            }

            @Override
            public String name() {
                return "LIST";
            }
        };
    }

    /**
     * @param filename
     *
     * @return
     */
    private static List<String> readAllLines(String filename) {
        ArrayList<String> retVal = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                retVal.add(line);
            }
        } catch (IOException ex) {
            System.err.println("[readLines] error processing " + filename + ": " + ex);
            throw new UncheckedIOException(ex);
        }
        return retVal;
    }

    private ClassHolderFactory() {
    }
}
