package jla.format;

import soot.SootClass;

/**
 * Interface for formatting a {@link SootClass}.
 *
 * @author Timothy Hoffman
 */
public interface IClassFormatter {

    /**
     * @return header to print before any content (include newline if needed)
     */
    public String header();

    /**
     * Formats all members of the given {@link SootClass}.
     *
     * @param clazz
     *
     * @return
     */
    public String format(SootClass clazz);

    /**
     * Gives an optional filename suffix to be inserted before the extension.
     *
     * @return
     */
    public String recommendedFilenameSuffix();

    /**
     * Gives the file extension (ex: {@code .txt}) that should be used for
     * saving formatted Classes to file.
     *
     * @return
     */
    public String recommendedFileExtension();
}
