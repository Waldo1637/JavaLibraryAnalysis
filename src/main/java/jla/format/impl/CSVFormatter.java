package jla.format.impl;

import jla.filter.IFilter;
import jla.format.AbstractClassFormatter;
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
public class CSVFormatter extends AbstractClassFormatter {

    /**
     * Format string for the insert statements. Arguments are as follows:
     * <ul>
     * <li>0 - Class Name</li>
     * <li>1 - Return Type</li>
     * <li>2 - Method Name</li>
     * <li>3 - Parameter Types</li>
     * </ul>
     */
    private static final String METHOD_FORMAT = "\"%s\",\"%s %s(%s)\"%n";

    /**
     * Format string for the insert statements. Arguments are as follows:
     * <ul>
     * <li>0 - Class Name</li>
     * <li>1 - Type</li>
     * <li>2 - Field Name</li>
     * </ul>
     */
    private static final String FIELD_FORMAT = "\"%s\",\"%s %s\"%n";

    /**
     *
     */
    private static final IMemberFormatter MEMBER_FORMATTER = new IMemberFormatter() {

        @Override
        public String format(SootClass c, ClassMember m) {
            if (m instanceof SootField) {
                return String.format(FIELD_FORMAT,
                        Soot.getClassName(c),
                        Soot.getReturnType(m),
                        Soot.getMemberName(m));
            } else if (m instanceof SootMethod) {
                return String.format(METHOD_FORMAT,
                        Soot.getClassName(c),
                        Soot.getReturnType(m),
                        Soot.getMemberName(m),
                        Soot.getParameterList(m));
            } else {
                throw new IllegalArgumentException(m.getClass().getName());
            }
        }
    };

    @Override
    public String header() {
        return "class,method/field sig" + System.lineSeparator();
    }

    @Override
    public String recommendedFileExtension() {
        return ".csv";
    }

    /**
     *
     */
    public CSVFormatter() {
        super(MEMBER_FORMATTER);
    }

    /**
     *
     * @param memberFilter
     */
    public CSVFormatter(IFilter<ClassMember> memberFilter) {
        super(MEMBER_FORMATTER, memberFilter);
    }

    /**
     * @param memberFormat
     * @param memberFilter
     */
    protected CSVFormatter(IMemberFormatter memberFormat, IFilter<ClassMember> memberFilter) {
        super(memberFormat, memberFilter);
    }

    /**
     * @param memberFormat
     */
    protected CSVFormatter(IMemberFormatter memberFormat) {
        super(memberFormat);
    }
}
