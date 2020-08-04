package jla.filter;

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
