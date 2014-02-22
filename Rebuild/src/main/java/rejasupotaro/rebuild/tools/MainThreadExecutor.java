package rejasupotaro.rebuild.tools;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Executor;

import roboguice.inject.ContextSingleton;

@ContextSingleton
public class MainThreadExecutor implements Executor {

    private static final Handler mHandler = new Handler(Looper.getMainLooper());

    @Override
    public void execute(Runnable r) {
        mHandler.post(r);
    }
}

