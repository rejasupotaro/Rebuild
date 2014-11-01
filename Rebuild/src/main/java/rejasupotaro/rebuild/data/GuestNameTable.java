package rejasupotaro.rebuild.data;

import java.util.HashMap;

public class GuestNameTable {
    private static final HashMap<String, String> table = new HashMap<String, String>() {{
        put("Naoya Ito", "naoya_ito");
        put("伊藤直也", "naoya_ito");
        put("Matz", "yukihiro_matz");
        put("まつもとゆきひろ", "yukihiro_matz");
        put("zzak", "_zzak");
        put("gfx", "__gfx__");
        put("Kenn Ejima", "kenn");
        put("Naoki Hiroshima", "N");
        put("Gosuke Miyashita", "gosukenator");
        put("Hakuro Matsuda", "hak");
        put("Jesse Vincent", "obra");
        put("高林哲", "");
    }};

    public static String inquire(String name) {
        if (!table.containsKey(name)) {
            return name;
        }
        return table.get(name);
    }
}
