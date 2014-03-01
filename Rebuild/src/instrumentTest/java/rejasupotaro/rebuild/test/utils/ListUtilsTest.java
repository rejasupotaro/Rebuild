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

    public void testMax() {
        assertEquals(3, ListUtils.max(1, 2, 3));
        assertEquals(3, ListUtils.max(3, 2, 1));
        assertEquals(3, ListUtils.max(2, 3, 1));
    }

    public void testMin() {
        assertEquals(1, ListUtils.min(1, 2, 3));
        assertEquals(1, ListUtils.min(3, 2, 1));
        assertEquals(1, ListUtils.min(2, 3, 1));
    }
}
