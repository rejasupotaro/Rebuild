package rejasupotaro.rebuild.utils;

import android.os.Handler;
import android.os.Message;

public class Timer extends Handler {
    private static final int DEFAULT_INTERVAL_MILLIS = 1000;

    private boolean isUpdate;
    private int instervalMillis = DEFAULT_INTERVAL_MILLIS;
    private Callback callback;
    private long startTimeMillis;

    public Timer(Callback callback) {
        this.callback = callback;
    }

    public Timer(Callback callback, int intervalMillis) {
        this.callback = callback;
        instervalMillis = intervalMillis;
    }

    private void init() {
        startTimeMillis = System.currentTimeMillis();
    }

    public void start() {
        init();
        isUpdate = true;
        handleMessage(new Message());
    }

    public void stop() {
        isUpdate = false;
    }

    @Override
    public void handleMessage(Message msg) {
        this.removeMessages(0);
        if (isUpdate) {
            sendMessageDelayed(obtainMessage(0), instervalMillis);
            callback.tick(System.currentTimeMillis() - startTimeMillis);
        }
    }

    public interface Callback {

        public void tick(long timeMillis);
    }
}

