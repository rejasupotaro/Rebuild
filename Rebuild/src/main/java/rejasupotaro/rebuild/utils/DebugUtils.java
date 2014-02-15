package rejasupotaro.rebuild.utils;

import android.content.Context;
import android.widget.Toast;

import rejasupotaro.rebuild.R;

public final class DebugUtils {

    public static void notImplementedYet(Context context) {
        Toast.makeText(context, context.getString(R.string.not_implemented_yet), Toast.LENGTH_SHORT).show();
    }

    private DebugUtils() {}
}
