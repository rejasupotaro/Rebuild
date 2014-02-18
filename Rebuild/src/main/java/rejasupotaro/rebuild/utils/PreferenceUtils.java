package rejasupotaro.rebuild.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public final class PreferenceUtils {

    public static SharedPreferences getDefaultSharedPreferences(final Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static boolean getBoolean(final Context context, final String key) {
        return getDefaultSharedPreferences(context).getBoolean(key, false);
    }

    private PreferenceUtils() {
    }
}
