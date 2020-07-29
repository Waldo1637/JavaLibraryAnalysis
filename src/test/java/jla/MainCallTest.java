package jla;

import org.junit.Test;

/**
 *
 * @author Timothy Hoffman
 */
public class MainCallTest {

    public MainCallTest() {
    }

    @Test
    public void testClasses() {
        String[] args = new String[]{"-c", "java.nio.ByteBuffer", "java.nio.IntBuffer"};
        jla.main.Main.main(args);
    }

    @Test
    public void testInputFile() {
        String[] args = new String[]{"-f", "target/test-classes/DaCapoUsedClassList.txt"};
        jla.main.Main.main(args);
    }

    @Test
    public void testBoot() {
        String[] args = new String[]{"-boot"};
        jla.main.Main.main(args);
    }
}
