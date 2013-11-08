package rejasupotaro.rebuild.test.utils;

import android.test.AndroidTestCase;

import java.util.ArrayList;

import rejasupotaro.rebuild.utils.ListUtils;

public class ListUtilsTest extends AndroidTestCase {

    public void testIsEmpty() {
        assertTrue(ListUtils.isEmpty(null));
        assertTrue(ListUtils.isEmpty(new ArrayList<String>()));
        assertFalse(ListUtils.isEmpty(new ArrayList<String>() {{
            add("dummy");
        }}));
    }
}
