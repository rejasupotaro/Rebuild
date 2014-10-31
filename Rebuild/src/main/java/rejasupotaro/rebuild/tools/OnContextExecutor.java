package rejasupotaro.rebuild.tools;

import android.content.Context;

public final class OnContextExecutor extends MainThreadExecutor {
    public void execute(Context context, Runnable r) {
        if (context == null) {
            return;
        }
        super.execute(r);
    }
}

