package rejasupotaro.rebuild.utils;

import android.os.Handler;
import android.os.Message;

public class Timer extends Handler {

    private static final int DEFAULT_INTERVAL_MILLIS = 1000;

    private boolean mIsUpdate;

    private int mInstervalMillis = DEFAULT_INTERVAL_MILLIS;

    private Callback mCallback;

    private long mStartTimeMillis;

    public Timer(Callback callback) {
        mCallback = callback;
    }

    public Timer(Callback callback, int intervalMillis) {
        mCallback = callback;
        mInstervalMillis = intervalMillis;
    }

    private void init() {
        mStartTimeMillis = System.currentTimeMillis();
    }

    public void start() {
        init();
        mIsUpdate = true;
        handleMessage(new Message());
    }

    public void stop() {
        mIsUpdate = false;
    }

    @Override
    public void handleMessage(Message msg) {
        this.removeMessages(0);
        if (mIsUpdate) {
            sendMessageDelayed(obtainMessage(0), mInstervalMillis);
            mCallback.tick(System.currentTimeMillis() - mStartTimeMillis);
        }
    }

    public interface Callback {

        public void tick(long timeMillis);
    }
}

