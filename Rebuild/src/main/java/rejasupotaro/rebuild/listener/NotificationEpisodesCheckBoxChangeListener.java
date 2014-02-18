package rejasupotaro.rebuild.listener;

import android.content.Context;
import android.preference.Preference;

import rejasupotaro.rebuild.utils.DebugUtils;

public class NotificationEpisodesCheckBoxChangeListener implements Preference.OnPreferenceChangeListener {

    private Context mContext;

    public NotificationEpisodesCheckBoxChangeListener(final Context context) {
        mContext = context;
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if ((Boolean) newValue) {
            // not implemented yet
            DebugUtils.notImplementedYet(mContext);
        }
        return true;
    }
}
