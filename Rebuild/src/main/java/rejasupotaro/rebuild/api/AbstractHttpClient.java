package rejasupotaro.rebuild.api;

import android.util.Log;

import org.apache.http.Header;

import java.io.UnsupportedEncodingException;

public abstract class AbstractHttpClient {

    protected abstract String getTag();

    protected void dumpError(Header[] headers, byte[] body, Throwable throwable) {
        for (Header header : headers) {
            Log.e(getTag(), header.getName() + " => " + header.getValue());
        }

        try {
            Log.e(getTag(), new String(body, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        Log.e(getTag(), throwable.toString());
    }
}
