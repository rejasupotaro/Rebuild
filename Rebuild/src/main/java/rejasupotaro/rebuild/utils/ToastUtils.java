package rejasupotaro.rebuild.utils;

import android.content.Context;
import android.widget.Toast;

public final class ToastUtils {

    private ToastUtils() {}

    public static void show(Context context, int resId) {
        show(context, context.getString(resId));
    }

    public static void show(Context context, String message) {
        if (message == null) return;
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
