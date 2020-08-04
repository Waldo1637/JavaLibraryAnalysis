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
import jla.format.AbstractClassFormatter;
import static jla.format.FormatHelpers.Soot;
import jla.format.IMemberFormatter;
import soot.ClassMember;
import soot.SootClass;

/**
 * Formatter for printing class names only using / instead of . as package
 * separators.
 *
 * @author Timothy Hoffman
 */
public class ClassOnlyFormatter extends AbstractClassFormatter {

    /**
     *
     */
    private static final IMemberFormatter MEMBER_FORMATTER = new IMemberFormatter() {

        @Override
        public String format(SootClass c, @Deprecated ClassMember m) {
            return Soot.getClassName(c).replaceAll("\\.", "/") + ".class\n";
        }
    };

    /**
     *
     */
    public ClassOnlyFormatter() {
        super(MEMBER_FORMATTER);
    }

    /**
     *
     * @param memberFilter
     */
    public ClassOnlyFormatter(IFilter<ClassMember> memberFilter) {
        super(MEMBER_FORMATTER, memberFilter);
    }

    @Override
    public String recommendedFilenameSuffix() {
        return addParentSuffix("ClassNames");
    }

    @Override
    public String recommendedFileExtension() {
        return ".txt";
    }
}
