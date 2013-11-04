package rejasupotaro.rebuild.utils;

public final class StringUtils {

    private StringUtils() {}

    public static String removeNewLines(String source) {
        return source.replaceAll("\n", "");
    }
}
