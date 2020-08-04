package jla.print;

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

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

/**
 * Implementation of {@link IPrinter} that prints everything to a file with the
 * given name.
 *
 * @author Timothy Hoffman
 */
public class FilePrinter implements IPrinter {

    private final String fileName;
    private PrintStream stream;

    public FilePrinter(String fileName) {
        this.fileName = fileName;
        this.stream = null;
    }

    @Override
    public void open() {
        if (stream == null) {
            try {
                stream = new PrintStream(new BufferedOutputStream(new FileOutputStream(fileName)));
            } catch (FileNotFoundException ex) {
                stream = null;
                throw new RuntimeException(ex);
            }
        }
    }

    @Override
    public void close() {
        if (stream != null) {
            stream.close();
            stream = null;
        }
    }

    @Override
    public void print(String s) {
        if (stream != null) {
            stream.print(s);
        } else {
            throw new RuntimeException("Stream is not open");
        }
    }
}
