package rejasupotaro.rebuild.media;

import android.content.Context;

import rejasupotaro.rebuild.constants.PrefsKey;
import rejasupotaro.rebuild.utils.PreferenceUtils;

public class CurrentPositionStore {

    private Context applicationContext;

    public CurrentPositionStore(Context context) {
        this.applicationContext = context.getApplicationContext();
    }

    public void saveCurrentPosition(int currentPosition) {
        PreferenceUtils.saveInt(applicationContext, PrefsKey.CURRENT_POSITION, currentPosition);
    }

    public int loadCurrentPosition() {
        return PreferenceUtils.loadInt(applicationContext, PrefsKey.CURRENT_POSITION);
    }

    public void remove() {
        PreferenceUtils.remove(applicationContext, PrefsKey.CURRENT_POSITION);
    }
}
