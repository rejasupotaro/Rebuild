package rejasupotaro.rebuild.utils;

import java.util.List;

public final class ListUtils {

    private ListUtils() {}

    public static boolean isEmpty(List<?> list) {
        return (list == null || list.size() == 0);
    }
}
