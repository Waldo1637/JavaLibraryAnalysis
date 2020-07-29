package jla.format;

import soot.ClassMember;
import soot.SootClass;

/**
 * Interface for formatting a single {@link ClassMember} of a {@link SootClass}.
 *
 * @author Timothy Hoffman
 */
public interface IMemberFormatter {

    /**
     * Formats the given {@link ClassMember} of the given {@link SootClass}.
     *
     * @param clazz
     * @param member
     *
     * @return
     */
    public String format(SootClass clazz, ClassMember member);
}
