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

import soot.*;

/**
 * Helper methods for formatting code constructs as Strings.
 *
 * @author Sean Newell
 * @author Timothy Hoffman
 */
public final class FormatHelpers {

    /**
     * Format code constructs in the format used by Soot for these constructs.
     */
    public static final class Soot {

        private static final int ACCESS_MODIFIERS
                = Modifier.PUBLIC | Modifier.PROTECTED | Modifier.PRIVATE;

        /**
         * @param c
         *
         * @return
         */
        public static String getAccessModifier(SootClass c) {
            return Modifier.toString(c.getModifiers() & ACCESS_MODIFIERS);
        }

        /**
         * @param m
         *
         * @return
         */
        public static String getAccessModifier(ClassMember m) {
            return Modifier.toString(m.getModifiers() & ACCESS_MODIFIERS);
        }

        /**
         * @param c
         *
         * @return
         */
        public static String getNonAccessModifiers(SootClass c) {
            return Modifier.toString(c.getModifiers() & ~ACCESS_MODIFIERS);
        }

        /**
         * @param m
         *
         * @return
         */
        public static String getNonAccessModifiers(ClassMember m) {
            int mods = m.getModifiers() & ~ACCESS_MODIFIERS;
            if (m instanceof SootMethod) {
                //Don't print "transient" or "volatile" on methods because they
                //  actually mean VARARGS and BRIDGE (respectively).
                mods &= ~(Modifier.TRANSIENT | Modifier.VOLATILE);
            }
            return Modifier.toString(mods);
        }

        /**
         * @param m
         *
         * @return
         */
        public static boolean isVargs(SootMethod m) {
            return Modifier.isTransient(m.getModifiers());
        }

        /**
         * @param m
         *
         * @return
         */
        public static boolean isSynthetic(SootMethod m) {
            return Modifier.isSynthetic(m.getModifiers());
        }

        /**
         * @param m
         *
         * @return
         */
        public static boolean isBridge(SootMethod m) {
            return Modifier.isVolatile(m.getModifiers());
        }

        /**
         * @param c
         *
         * @return the Soot formatted String representation of the name of the
         *         given {@link SootClass}
         */
        public static String getClassName(SootClass c) {

            return getTypeName(c.getType());
        }

        /**
         * @param t
         *
         * @return
         */
        public static String getTypeName(Type t) {

            return t.toString();
        }

        /**
         * @param m
         *
         * @return the Soot formatted String for the return type of the given
         *         {@link ClassMember}
         */
        public static String getReturnType(ClassMember m) {
            if (m instanceof SootField) {
                return getTypeName(((SootField) m).getType());
            } else if (m instanceof SootMethod) {
                SootMethod method = (SootMethod) m;
                if (method.isStaticInitializer() || method.isConstructor()) {
                    return "void";
                } else {
                    return getTypeName(method.getReturnType());
                }
            } else {
                throw new UnsupportedOperationException("The type " + m.getClass() + " is not supported.");
            }
        }

        /**
         * @param m
         *
         * @return the Soot formatted String representation of the name of the
         *         given {@link ClassMember} of a class
         */
        public static String getMemberName(ClassMember m) {
            if (m instanceof SootField) {
                return ((SootField) m).getName();
            } else if (m instanceof SootMethod) {
                return ((SootMethod) m).getName();
            } else {
                throw new UnsupportedOperationException("The type " + m.getClass() + " is not supported.");
            }
        }

        /**
         * @param m
         *
         * @return Soot formatted String for the parameter list of the given
         *         {@link ClassMember} (or empty String if it is not a member
         *         that has parameters).
         */
        public static String getParameterList(ClassMember m) {
            if (m instanceof SootField) {
                return "";
            } else if (m instanceof SootMethod) {
                StringBuilder b = new StringBuilder();
                for (Type param : ((SootMethod) m).getParameterTypes()) {
                    if (b.length() > 0) {
                        b.append(',');
                    }
                    b.append(getTypeName(param));
                }
                return b.toString();
            } else {
                throw new UnsupportedOperationException("The type " + m.getClass() + " is not supported.");
            }
        }

        /**
         * Private constructor to prevent instance creation.
         */
        private Soot() {
        }
    }
}
