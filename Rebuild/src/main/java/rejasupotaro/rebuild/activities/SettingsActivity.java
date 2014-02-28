package rejasupotaro.rebuild.activities;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.PreferenceActivity;
import android.view.MenuItem;

import rejasupotaro.rebuild.R;
import rejasupotaro.rebuild.listener.NotificationEpisodesCheckBoxChangeListener;
import rejasupotaro.rebuild.tools.MenuDelegate;
import rejasupotaro.rebuild.utils.PreferenceUtils;

public class SettingsActivity extends PreferenceActivity {

    private MenuDelegate menuDelegate;

    public static Intent createIntent(Context context) {
        return new Intent(context, SettingsActivity.class);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        menuDelegate = new MenuDelegate(this);

        addPreferencesFromResource(R.xml.preferences);

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        setUpNotificationEpisodesCheckBox();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return menuDelegate.onItemSelect(item);
    }

    private void setUpNotificationEpisodesCheckBox() {
        String key = getString(R.string.pref_key_notification_episodes);
        CheckBoxPreference checkBox = (CheckBoxPreference) findPreference(key);
        boolean checkStatus = PreferenceUtils.getBoolean(this, key);
        checkBox.setChecked(checkStatus);
        checkBox.setOnPreferenceChangeListener(new NotificationEpisodesCheckBoxChangeListener(this));
    }

}
