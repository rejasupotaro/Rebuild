package rejasupotaro.rebuild.utils;

import java.util.List;

public final class ListUtils {

    public static boolean isEmpty(List<?> list) {
        return (list == null || list.size() == 0);
    }

    public static int max(int... values) {
        int max = values[0];
        for (int i = 1; i < values.length; i++) {
            if (max < values[i]) {
                max = values[i];
            }
        }
        return max;
    }

    public static int min(int... values) {
        int min = max(values);
        for (int i = 0; i < values.length; i++) {
            if (values[i] >= 0 && min > values[i]) {
                min = values[i];
            }
        }
        return min;
    }

    private ListUtils() {}
}
