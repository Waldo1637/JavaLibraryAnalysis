package jla.format.impl;

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

import jla.filter.BasicFilters;
import jla.filter.ClassMemberFilters;
import jla.filter.IFilter;
import jla.format.AbstractClassFormatter;
import jla.format.FormatHelpers.Soot;
import jla.format.IMemberFormatter;
import soot.ClassMember;
import soot.SootClass;
import soot.SootMethod;

/**
 * Formatter for class, method in PScout format.
 *
 * @author Timothy Hoffman
 */
public class PscoutFormatter extends AbstractClassFormatter {

    /**
     * Format string for the insert statements. Arguments are as follows:
     * <ul>
     * <li>0 - Class Name</li>
     * <li>1 - Return Type</li>
     * <li>2 - Method Name</li>
     * <li>3 - Parameter Types</li>
     * </ul>
     */
    private static final String INSERT_STMT_FORMAT = "<%s: %s %s(%s)> %n";

    /**
     *
     */
    private static final IMemberFormatter MEMBER_FORMATTER = new IMemberFormatter() {

        @Override
        public String format(SootClass c, ClassMember m) {
            if (m instanceof SootMethod) {
                return String.format(INSERT_STMT_FORMAT,
                        Soot.getClassName(c),
                        Soot.getReturnType(m),
                        Soot.getMemberName(m),
                        Soot.getParameterList(m));
            } else {
                throw new IllegalArgumentException(m.getClass().getName());
            }
        }
    };

    /**
     *
     */
    public PscoutFormatter() {
        super(MEMBER_FORMATTER, ClassMemberFilters.EXECUTABLE);
    }

    /**
     *
     * @param memberFilter
     */
    public PscoutFormatter(IFilter<ClassMember> memberFilter) {
        super(MEMBER_FORMATTER, BasicFilters.and(memberFilter, ClassMemberFilters.EXECUTABLE));
    }

    @Override
    public String recommendedFileExtension() {
        return ".pscout";
    }
}
