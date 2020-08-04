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

import jla.filter.IFilter;
import jla.format.FormatHelpers.Soot;
import jla.format.IMemberFormatter;
import soot.ClassMember;
import soot.SootClass;
import soot.SootField;
import soot.SootMethod;

/**
 * Formatter for methods and fields in a Class in CSV format.
 *
 * @author Timothy Hoffman
 */
public class CSVDetailFormatter extends CSVFormatter {

    /**
     * Part of the format string that is identical for both Methods and Fields.
     *
     * NOTE: All properties shared between methods and fields are listed first
     * so that the columns align if both methods and fields are printed
     * together.
     */
    private static final String SHARED_FORMAT = "\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\"";

    /**
     * Format string for Methods. Arguments are as follows:
     * <ul>
     * <li>0 - Class Access Modifier</li>
     * <li>1 - Other Class Modifiers</li>
     * <li>2 - Class Name</li>
     * <li>3 - Method Access Modifier</li>
     * <li>4 - Other Method Modifiers</li>
     * <li>5 - Return Type</li>
     * <li>6 - Method Name</li>
     *
     * <li>7 - Is Varargs Method?</li>
     * <li>8 - Is Synthetic Method?</li>
     * <li>9 - Is Bridge Method?</li>
     * <li>10 - Parameter Types</li>
     * </ul>
     *
     * NOTE: Skip the bridge flag because it cannot apply to Constructor only to
     * Method
     */
    private static final String METHOD_FORMAT = "M," + SHARED_FORMAT + ",%d,%d,%d,\"%s\"%n";

    /**
     * Format string for Fields. Arguments are as follows:
     * <ul>
     * <li>0 - Class Access Modifier</li>
     * <li>1 - Other Class Modifiers</li>
     * <li>2 - Class Name</li>
     * <li>3 - Field Access Modifier</li>
     * <li>4 - Other Field Modifiers</li>
     * <li>5 - Type</li>
     * <li>6 - Field Name</li>
     * </ul>
     */
    private static final String FIELD_FORMAT = "F," + SHARED_FORMAT + "%n";

    /**
     *
     */
    private static final IMemberFormatter MEMBER_FORMATTER = new IMemberFormatter() {
        @Override
        public String format(SootClass c, ClassMember membr) {
            if (membr instanceof SootField) {
                return String.format(FIELD_FORMAT,
                        Soot.getAccessModifier(c),
                        Soot.getNonAccessModifiers(c),
                        Soot.getClassName(c),
                        Soot.getAccessModifier(membr),
                        Soot.getNonAccessModifiers(membr),
                        Soot.getReturnType(membr),
                        Soot.getMemberName(membr)
                );
            } else if (membr instanceof SootMethod) {
                SootMethod m = (SootMethod) membr;
                return String.format(METHOD_FORMAT,
                        Soot.getAccessModifier(c),
                        Soot.getNonAccessModifiers(c),
                        Soot.getClassName(c),
                        Soot.getAccessModifier(membr),
                        Soot.getNonAccessModifiers(membr),
                        Soot.getReturnType(membr),
                        Soot.getMemberName(membr),
                        Soot.isVargs(m) ? 1 : 0,
                        Soot.isSynthetic(m) ? 1 : 0,
                        Soot.isBridge(m) ? 1 : 0,
                        Soot.getParameterList(membr)
                );
            } else {
                throw new IllegalArgumentException(membr.getClass().getName());
            }
        }
    };

    /**
     *
     */
    public CSVDetailFormatter() {
        super(MEMBER_FORMATTER);
    }

    /**
     *
     * @param memberFilter
     */
    public CSVDetailFormatter(IFilter<ClassMember> memberFilter) {
        super(MEMBER_FORMATTER, memberFilter);
    }

    @Override
    public String header() {
        return ",CLASS,CLASS,CLASS,M/F,M/F,M/F Type,M/F Name,varg,syn,bridge,params" + System.lineSeparator();
    }

    @Override
    public String recommendedFilenameSuffix() {
        return addParentSuffix("detail");
    }
}
