package rejasupotaro.rebuild.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import org.apache.http.protocol.HTTP;

public final class IntentUtils {

    private IntentUtils() {}

    public static void openBrowser(Context context, String url) {
        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        context.startActivity(intent);
    }

    public static void sendPostIntent(Context context, String text) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType(HTTP.PLAIN_TEXT_TYPE);
        intent.putExtra(Intent.EXTRA_TEXT, text);
        context.startActivity(intent);
    }
}
