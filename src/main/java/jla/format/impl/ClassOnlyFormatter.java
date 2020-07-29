package jla.format.impl;

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
