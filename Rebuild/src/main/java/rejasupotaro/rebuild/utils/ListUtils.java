package rejasupotaro.rebuild.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import rejasupotaro.rebuild.data.models.Episode;

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

    public static <T> List<T> filterByPage(int page, int perPage, List<T> list) {
        List<T> filteredList = new ArrayList<T>();
        if (page <= 0 || perPage <= 0 || list == null || list.size() == 0) {
            return filteredList;
        }

        int fromIndex = (page - 1) * perPage;
        int toIndex = ((page * perPage)  < list.size() ? (page * perPage) : list.size());

        if (fromIndex > list.size()) {
            return filteredList;
        }

        for (int i = fromIndex; i < toIndex; i++) {
            filteredList.add(list.get(i));
        }

        return filteredList;
    }

    public static List<Episode> orderByPostedAt(List<Episode> episodeList) {
        Collections.sort(episodeList, new Comparator<Episode>() {
            @Override
            public int compare(Episode lhs, Episode rhs) {
                return rhs.getPostedAt().compareTo(lhs.getPostedAt());
            }
        });
        return episodeList;
    }

    private ListUtils() {}
}
