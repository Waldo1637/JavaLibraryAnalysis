package jla.format.impl;

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
