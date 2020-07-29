package jla.filter;

import soot.ClassMember;
import soot.Modifier;
import soot.SootField;
import soot.SootMethod;

/**
 * @author Timothy Hoffman
 */
public enum ClassMemberFilters implements IFilter<ClassMember> {
    /**
     * {@link IFilter} that accepts all members.
     */
    ALL {
        @Override
        public boolean accept(ClassMember m) {
            return true;
        }
    },
    /**
     * {@link IFilter} that accepts only fields.
     */
    FIELD {
        @Override
        public boolean accept(ClassMember m) {
            return m instanceof SootField;
        }
    },
    /**
     * {@link IFilter} that accepts only methods that are not constructors or
     * static initializers.
     */
    METHOD {
        @Override
        public boolean accept(ClassMember m) {
            if (m instanceof SootMethod) {
                SootMethod name = (SootMethod) m;
                return !name.isConstructor() && !name.isStaticInitializer();
            } else {
                return false;
            }
        }
    },
    /**
     * {@link IFilter} that accepts only constructors.
     */
    INIT {
        @Override
        public boolean accept(ClassMember m) {
            return (m instanceof SootMethod)
                    && ((SootMethod) m).isConstructor();
        }
    },
    /**
     * {@link IFilter} that accepts only static initializers.
     */
    CLINIT {
        @Override
        public boolean accept(ClassMember m) {
            return (m instanceof SootMethod)
                    && ((SootMethod) m).isStaticInitializer();
        }
    },
    /**
     * {@link IFilter} that accepts only executable members (i.e. methods,
     * constructors, and static initializers).
     */
    EXECUTABLE {
        @Override
        public boolean accept(ClassMember m) {
            return (m instanceof SootMethod);
        }
    },
    /**
     * {@link IFilter} that accepts only members with the {@code native}
     * modifier.
     */
    NATIVE {
        @Override
        public boolean accept(ClassMember m) {
            return Modifier.isNative(m.getModifiers());
        }
    },
    /**
     * {@link IFilter} that accepts only members with the {@code public} or
     * {@code protected} modifier on the method and the containing class.
     */
    VISIBLE {
        @Override
        public boolean accept(ClassMember m) {
            return isPublicOrProtected(m.getDeclaringClass().getModifiers())
                    && isPublicOrProtected(m.getModifiers());
        }

    };

    private static boolean isPublicOrProtected(int modifiers) {
        return (modifiers & (Modifier.PUBLIC | Modifier.PROTECTED)) != 0;
    }
}
