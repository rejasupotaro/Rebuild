package rejasupotaro.rebuild;

import com.activeandroid.ActiveAndroid;

import android.app.Application;

import rejasupotaro.rebuild.api.RssFeedClient;

public class RebuildApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ActiveAndroid.initialize(this);
        RssFeedClient.init(this);
    }

    @Override
    public void onTerminate() {
        ActiveAndroid.dispose();
        super.onTerminate();
    }
}
