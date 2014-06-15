package rejasupotaro.rebuild.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public final class PreferenceUtils {

    private static SharedPreferences getDefaultSharedPreferences(final Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static void saveInt(Context context, String key, int value) {
        SharedPreferences.Editor editor = getDefaultSharedPreferences(context).edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public static void saveString(Context context, String key, String value) {
        SharedPreferences.Editor editor = getDefaultSharedPreferences(context).edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static int loadInt(Context context, String key) {
        return getDefaultSharedPreferences(context).getInt(key, -1);
    }

    public static void remove(Context context, String key) {
        SharedPreferences.Editor editor = getDefaultSharedPreferences(context).edit();
        editor.remove(key);
        editor.apply();
    }

    public static boolean getBoolean(final Context context, final String key) {
        return getDefaultSharedPreferences(context).getBoolean(key, false);
    }

    private PreferenceUtils() {
    }
}
