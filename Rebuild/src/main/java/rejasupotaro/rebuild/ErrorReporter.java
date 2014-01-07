package rejasupotaro.rebuild;

import android.app.Activity;
import android.content.Intent;

import javax.inject.Singleton;

import rejasupotaro.rebuild.activities.ReportActivity;

@Singleton
public class ErrorReporter implements Thread.UncaughtExceptionHandler {

    private static final String TAG = ErrorReporter.class.getSimpleName();

    private volatile boolean mCrashing = false;

    private Activity mActivity;

    private Thread.UncaughtExceptionHandler mUncaughtExceptionHandler = null;

    public void registerActivity(Activity activity) {
        if (shouldInit()) init();
        mActivity = activity;
    }

    public void unregisterActivity() {
        mActivity = null;
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
            if (!mCrashing) {
                mCrashing = true;
                handleExcetion();
            }
        } finally {
            if (mUncaughtExceptionHandler != null) {
                mUncaughtExceptionHandler.uncaughtException(thread, ex);
            }
        }
    }

    private void handleExcetion() {
        if (mActivity != null) {
            mActivity.startActivity(new Intent(mActivity, ReportActivity.class));
        } else {

        }
    }
}
