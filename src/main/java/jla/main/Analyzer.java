package jla.main;

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

import java.io.File;
import jla.analyzer.IClassHolder;
import jla.filter.IFilter;
import jla.format.IClassFormatter;
import jla.format.impl.CSVDetailFormatter;
import jla.format.impl.CSVFormatter;
import jla.format.impl.ClassOnlyFormatter;
import jla.format.impl.PscoutFormatter;
import jla.print.FilePrinter;
import jla.print.IPrinter;
import soot.ClassMember;
import soot.Scene;
import soot.SootClass;
import soot.options.Options;

/**
 * Prints an {@link IClassHolder} to an {@link IPrinter} formatted as defined by
 * an {@link IClassFormatter}.
 *
 * @author Timothy Hoffman
 */
public class Analyzer {

    public static final boolean DEBUG = false;

    private static final Scene SOOT_SCENE;

    static {
        final Options opts = Options.v();
        opts.set_drop_bodies_after_load(true);//clear up memory
        opts.set_full_resolver(false);//resolve only on demand
        opts.set_allow_phantom_refs(false);//don't swallow errors
        opts.set_no_bodies_for_excluded(false);//otherwise, allows phandom refs
        opts.set_permissive_resolving(false);//don't swallow errors
        opts.set_include_all(true);//empty default excluded packages list
//        opts.set_debug_resolver(true);

        //NOTE: options must be set before initializing the Scene
        SOOT_SCENE = Scene.v();
    }

    /**
     *
     */
    public enum OutputType {
        /**
         * Supports only method and initializer type members.
         */
        MEMBER_PSCOUT,
        /**
         * Supports method, initializer, and field type members.
         */
        MEMBER_CSV,
        /**
         * Supports method, initializer, and field type members.
         */
        MEMBER_CSV_DETAIL,
        /**
         *
         */
        CLASS_ONLY,
    };

    /**
     *
     * @param paths
     */
    public static void setClasspath(Iterable<String> paths) {
        StringBuilder sb = new StringBuilder();
        for (String s : paths) {
            sb.append(s).append(File.pathSeparatorChar);
        }
        SOOT_SCENE.setSootClassPath(sb.toString());
    }

    /**
     * @param classes
     * @param formatter
     * @param printer
     */
    public static void print(IClassHolder classes, IClassFormatter formatter, IPrinter printer) {
        try {
            printer.open();
            {
                String header = formatter.header();
                if (header != null && !header.isEmpty()) {
                    printer.print(header);
                }
            }
            for (String className : classes) {
                if (Analyzer.DEBUG) {
                    System.out.println("[Analyzer] Printing class: " + className);
                }
                SootClass clazz;
                try {
                    clazz = SOOT_SCENE.loadClassAndSupport(className);
                } catch (Exception | Error ex) {
                    System.out.println("[Analyzer] Unable to find class " + className + " -> " + ex);
                    if (Analyzer.DEBUG) {
                        ex.printStackTrace(System.out);
                    }
                    continue;
                }

                //if the class is found without exception, then format and print it
                String format = formatter.format(clazz);
                if (format != null) {
                    printer.print(format);
                }
            }
        } finally {
            printer.close();
        }
    }

    /**
     * @param classes
     * @param toGenerate
     * @param memberFilter
     * @param printer
     */
    public static void print(IClassHolder classes, OutputType toGenerate, IFilter<ClassMember> memberFilter, IPrinter printer) {
        Analyzer.print(classes, toFormatter(toGenerate, memberFilter), printer);
    }

    /**
     *
     * @param classes
     * @param formatter
     */
    public static void print(IClassHolder classes, IClassFormatter formatter) {
        Analyzer.print(classes, formatter, new FilePrinter(fileName(classes, formatter)));
    }

    /**
     * @param classes
     * @param toGenerate
     * @param memberFilter
     */
    public static void print(IClassHolder classes, OutputType toGenerate, IFilter<ClassMember> memberFilter) {
        Analyzer.print(classes, toFormatter(toGenerate, memberFilter));
    }

    private static IClassFormatter toFormatter(OutputType toGenerate, IFilter<ClassMember> memberFilter) {
        switch (toGenerate) {
            case MEMBER_PSCOUT:
                return new PscoutFormatter(memberFilter);
            case MEMBER_CSV:
                return new CSVFormatter(memberFilter);
            case MEMBER_CSV_DETAIL:
                return new CSVDetailFormatter(memberFilter);
            case CLASS_ONLY:
                return new ClassOnlyFormatter(memberFilter);
            default:
                throw new IllegalArgumentException("Not yet supported: " + toGenerate);
        }
    }

    private static String fileName(IClassHolder classes, IClassFormatter formatter) {
        StringBuilder sb = new StringBuilder();
        sb.append(classes.name());
        String suffix = formatter.recommendedFilenameSuffix();
        if (suffix != null && !suffix.isEmpty()) {
            sb.append('_').append(suffix);
        }
        sb.append(formatter.recommendedFileExtension());
        return sb.toString();
    }

    private Analyzer() {
    }
}
