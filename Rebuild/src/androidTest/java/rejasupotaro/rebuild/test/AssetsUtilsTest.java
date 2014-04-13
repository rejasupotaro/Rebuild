package rejasupotaro.rebuild.test;

import android.test.InstrumentationTestCase;

public class AssetsUtilsTest extends InstrumentationTestCase {

    public void testRead() {
        String text = AssetsUtils.read(getInstrumentation(), "assets_utils_test.txt");
        assertEquals("Hello World!", text);
    }
}
