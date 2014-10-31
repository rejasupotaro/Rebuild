package rejasupotaro.rebuild.api;

import org.apache.http.Header;

import android.util.Log;

import java.io.UnsupportedEncodingException;

public abstract class AbstractHttpClient {
    protected abstract String getTag();

    protected void dumpError(Header[] headers, byte[] body, Throwable throwable) {
        if (headers != null) {
            for (Header header : headers) {
                Log.e(getTag(), header.getName() + " => " + header.getValue());
            }
        } else {
            Log.e(getTag(), "headers: null");
        }

        if (body != null) {
            try {
                Log.e(getTag(), new String(body, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } else {
            Log.e(getTag(), "body: null");
        }

        if (throwable != null) {
            Log.e(getTag(), throwable.toString());
        } else {
            Log.e(getTag(), "throwable: null");
        }
    }
}
