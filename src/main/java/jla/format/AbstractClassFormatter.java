package jla.format;

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

import java.util.Iterator;
import jla.filter.IFilter;
import jla.util.FilteredIterator;
import jla.util.SequentialIterator;
import soot.ClassMember;
import soot.SootClass;

/**
 * Abstract implementation of {@link IClassFormatter}.
 *
 * @author Timothy Hoffman
 */
public abstract class AbstractClassFormatter implements IClassFormatter {

    private final IMemberFormatter memberFormat;
    private final IFilter<ClassMember> memberFilter;

    /**
     *
     * @param memberFormat
     */
    public AbstractClassFormatter(IMemberFormatter memberFormat) {
        this(memberFormat, null);
    }

    /**
     *
     * @param memberFormat
     * @param memberFilter
     */
    public AbstractClassFormatter(IMemberFormatter memberFormat, IFilter<ClassMember> memberFilter) {
        this.memberFormat = memberFormat;
        this.memberFilter = memberFilter;
    }

    /**
     * @return header to print before any content (include newline if needed)
     */
    @Override
    public String header() {
        return "";
    }

    /**
     *
     * @param clazz
     *
     * @return formatted header for the given class (include newline if needed)
     */
    protected String formatHeader(SootClass clazz) {
        return "";
    }

    /**
     *
     * @param clazz
     *
     * @return formatted footer for the given class (include newline if needed)
     */
    protected String formatFooter(SootClass clazz) {
        return "";
    }

    /**
     * NOTE: The {@link AbstractClassFormatter} implementation returns the name
     * of the {@link IFilter} provided to the constructor or the empty string if
     * that was {@code null}. Subclasses wishing to easily retain this behavior
     * without all the boilerplate null checks, etc. can simply call
     * {@link #addParentSuffix(String)} with their added suffix.
     *
     * @return
     */
    @Override
    public String recommendedFilenameSuffix() {
        return addParentSuffix("");
    }

    /**
     *
     * @param suffix
     *
     * @return
     */
    protected final String addParentSuffix(String suffix) {
        if (memberFilter == null) {
            return suffix;
        } else if (suffix == null || suffix.isEmpty()) {
            return memberFilter.name();
        } else {
            return memberFilter.name() + '_' + suffix;
        }
    }

    @Override
    public final String format(SootClass clazz) {
        Iterator<ClassMember> members = getMembers(clazz);
        if (!members.hasNext()) {
            return null;
        }

        StringBuilder builder = new StringBuilder();
        builder.append(formatHeader(clazz));
        while (members.hasNext()) {
            ClassMember m = members.next();
            builder.append(memberFormat.format(clazz, m));
        }
        builder.append(formatFooter(clazz));
        return builder.toString();
    }

    private Iterator<ClassMember> getMembers(SootClass clazz) {
        @SuppressWarnings("unchecked")
        Iterator<ClassMember> ret = new SequentialIterator<>(
                clazz.getFields(),
                clazz.getMethods()
        );
        return (memberFilter == null) ? ret
                : new FilteredIterator<>(ret, memberFilter);
    }
}
