package rejasupotaro.rebuild.data.adapters;

public class ViewType {
    public static final int HEADER = 1;
    public static final int ITEM = 2;
    public static final int FOOTER = 3;

    public static boolean isHeader(int i) {
        return (i == HEADER);
    }

    public static boolean isItem(int i) {
        return (i == ITEM);
    }

    public static boolean isFooter(int i) {
        return (i == FOOTER);
    }
}
