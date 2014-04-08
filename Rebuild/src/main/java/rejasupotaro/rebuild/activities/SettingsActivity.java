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

    private void setUpNotificationEpisodesCheckBox() {
        String key = getString(R.string.pref_key_notification_episodes);
        CheckBoxPreference checkBox = (CheckBoxPreference) findPreference(key);
        boolean checkStatus = PreferenceUtils.getBoolean(this, key);
        checkBox.setChecked(checkStatus);
        checkBox.setOnPreferenceChangeListener(new NotificationEpisodesCheckBoxChangeListener(this));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                menuDelegate.pressHome();
                return true;
            case R.id.action_settings:
                menuDelegate.pressSettings();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
