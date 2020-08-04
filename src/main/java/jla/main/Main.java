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

import jla.analyzer.ClassHolderFactory;
import jla.analyzer.IClassHolder;
import jla.filter.BasicFilters;
import jla.filter.ClassMemberFilters;
import jla.format.IClassFormatter;
import jla.format.impl.PscoutFormatter;
import jla.print.FilePrinter;
import jla.print.IPrinter;

/**
 * Generate PSCOUT format for all visible members in the classes given as
 * program arguments (-c option) or the named file (-f option). Or generate CSV
 * detail format for all visible classes on the boot classpath (-boot option).
 *
 * @author Timothy Hoffman
 */
public class Main {
    //TODO: the true command line interface should be able to give
    //  1. the input classes via one of:
    //      A. list of class names
    //      B. list of paths (jar, dir, directory)
    //      C. filename containing class names
    //      D. filename containing paths
    //      * perhaps C/D are combined by forcing each file line to contain
    //          a flag indicating if it's a class or a path but that does
    //          complicate which method should be called to create IClassHolder.
    //      E. perhaps, IClassHolder implementation to dynamically load
    //  2. IClassFormatter implementation to dynamically load OR both of:
    //      A. OutputType flag
    //      B. IFilter<ClassMember> enum name or implementation to load
    //  3. Optional IPrinter implementation to dynamically load

    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println("Must give -c and a list of class names, -f and a file listing class names, or -boot");
            return;
        }

        IClassHolder classHolder;
        switch (args[0]) {
            case "-c":
                if (args.length < 2) {
                    System.err.println("-c option must be followed by one or more java class names");
                    return;
                }
                int numClasses = args.length - 1;
                String[] classList = new String[numClasses];
                System.arraycopy(args, 1, classList, 0, numClasses);
                classHolder = ClassHolderFactory.classNames(classList);
                break;
            case "-f":
                if (args.length < 2) {
                    System.err.println("-f option must be followed by a filename containing class names (one per line)");
                    return;
                }
                classHolder = ClassHolderFactory.readClassNamesFromFile(args[1]);
                break;
            case "-boot":
                gen_CSVDetail_FromBoot_AllVisible();
                return;
            default:
                System.err.println("Unsupported option: " + args[0]);
                return;
        }

        IClassFormatter formatter = new PscoutFormatter(ClassMemberFilters.VISIBLE);
        IPrinter printer = new FilePrinter("classes_" + System.currentTimeMillis() + formatter.recommendedFileExtension());
        Analyzer.print(classHolder, formatter, printer);
    }

    /**
     *
     */
    private static void gen_CSVDetail_FromBoot_AllVisible() {
        Analyzer.print(
                ClassHolderFactory.classesFromBootPath(BasicFilters.all()),
                Analyzer.OutputType.MEMBER_CSV_DETAIL,
                ClassMemberFilters.VISIBLE
        );
    }
}
