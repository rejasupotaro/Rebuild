package rejasupotaro.rebuild.test.utils;

import android.test.AndroidTestCase;

import rejasupotaro.rebuild.utils.StringUtils;

public class StringUtilsTest extends AndroidTestCase {

    public void testRemoveNewLines() {
        {
            String source = "Hello World!";
            assertEquals("Hello World!", StringUtils.removeNewLines(source));
        }
        {
            String source = "Hello World!\n";
            assertEquals("Hello World!", StringUtils.removeNewLines(source));
        }
        {
            String source = "Hello\nWorld!\n";
            assertEquals("HelloWorld!", StringUtils.removeNewLines(source));
        }
    }
}
