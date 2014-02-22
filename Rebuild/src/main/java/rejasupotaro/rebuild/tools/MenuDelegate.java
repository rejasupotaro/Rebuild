package rejasupotaro.rebuild.tools;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;

import javax.inject.Inject;

import rejasupotaro.rebuild.R;
import rejasupotaro.rebuild.activities.SettingsActivity;
import rejasupotaro.rebuild.models.Episode;
import rejasupotaro.rebuild.utils.DebugUtils;
import rejasupotaro.rebuild.utils.IntentUtils;

public class MenuDelegate {

    public static final String PARAM_EPISODE = "param_episode";

    private Activity mActivity;

    @Inject
    public MenuDelegate(Activity activity) {
        mActivity = activity;
    }

    public boolean onItemSelect(MenuItem item) {
        return onItemSelect(item, new Bundle());
    }

    public boolean onItemSelect(MenuItem item, Bundle params) {
        boolean result = true;
        switch (item.getItemId()) {
            case android.R.id.home: {
                mActivity.finish();
                break;
            }
            case R.id.action_settings: {
                mActivity.startActivity(SettingsActivity.createIntent(mActivity));
                break;
            }
            case R.id.action_share: {
                Episode episode = params.getParcelable(PARAM_EPISODE);
                String shareText = buildPostMessage(episode);
                IntentUtils.sendPostIntent(mActivity, shareText);
                break;
            }
            default: {
                result = mActivity.onOptionsItemSelected(item);
                break;
            }
        }
        return result;
    }

    private String buildPostMessage(Episode episode) {
        return " / " + episode.getTitle() + " " + episode.getLink() + " #rebuildfm";
    }

}
