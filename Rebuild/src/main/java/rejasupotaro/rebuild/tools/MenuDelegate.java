package rejasupotaro.rebuild.tools;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;

import javax.inject.Inject;

import rejasupotaro.rebuild.R;
import rejasupotaro.rebuild.activities.SettingsActivity;

public class MenuDelegate {

    public static final String PARAM_EPISODE = "param_episode";

    private Activity activity;

    @Inject
    public MenuDelegate(Activity activity) {
        this.activity = activity;
    }

    public boolean onItemSelect(MenuItem item) {
        return onItemSelect(item, new Bundle());
    }

    public boolean onItemSelect(MenuItem item, Bundle params) {
        boolean result = true;
        switch (item.getItemId()) {
            case android.R.id.home: {
                activity.finish();
                break;
            }
            case R.id.action_settings: {
                activity.startActivity(SettingsActivity.createIntent(activity));
                break;
            }
            default: {
                result = activity.onOptionsItemSelected(item);
                break;
            }
        }
        return result;
    }
}
