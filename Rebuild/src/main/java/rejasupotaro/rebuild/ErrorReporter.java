package rejasupotaro.rebuild;

import android.app.Activity;
import android.content.Intent;

import rejasupotaro.rebuild.activities.ReportActivity;

public class ErrorReporter implements Thread.UncaughtExceptionHandler {
    private static final String TAG = ErrorReporter.class.getSimpleName();

    private volatile boolean crashing = false;

    private Activity activity;
    private Thread.UncaughtExceptionHandler mUncaughtExceptionHandler = null;

    public void registerActivity(Activity activity) {
        if (shouldInit()) init();
        this.activity = activity;
    }

    public void unregisterActivity() {
        activity = null;
    }

    private boolean shouldInit() {
        return (mUncaughtExceptionHandler == null);
    }

    private void init() {
        mUncaughtExceptionHandler =
                Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        try {
            if (!crashing) {
                crashing = true;
                handleExcetion();
            }
        } finally {
            if (mUncaughtExceptionHandler != null) {
                mUncaughtExceptionHandler.uncaughtException(thread, ex);
            }
        }
    }

    private void handleExcetion() {
        if (activity != null) {
            activity.startActivity(new Intent(activity, ReportActivity.class));
        }
    }
}
