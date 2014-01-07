package rejasupotaro.rebuild;

import android.app.Application;

import com.activeandroid.ActiveAndroid;

public class RebuildApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ActiveAndroid.initialize(this);
    }

    @Override
    public void onTerminate() {
        ActiveAndroid.dispose();
        super.onTerminate();
    }
}
