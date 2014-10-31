package rejasupotaro.rebuild.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.PreferenceFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;

import rejasupotaro.rebuild.R;
import rejasupotaro.rebuild.listener.NotificationEpisodesCheckBoxChangeListener;
import rejasupotaro.rebuild.utils.PreferenceUtils;

public class SettingsActivity extends ActionBarActivity {
    public static Intent createIntent(Context context) {
        return new Intent(context, SettingsActivity.class);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();
        setupActionBar();
    }

    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_settings:
                startActivity(SettingsActivity.createIntent(this));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static class SettingsFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle paramBundle) {
            super.onCreate(paramBundle);
            addPreferencesFromResource(R.xml.preferences);
            setUpNotificationEpisodesCheckBox();
        }

        private void setUpNotificationEpisodesCheckBox() {
            String key = getString(R.string.pref_key_notification_episodes);
            CheckBoxPreference checkBox = (CheckBoxPreference) findPreference(key);
            boolean checkStatus = PreferenceUtils.getBoolean(getActivity(), key);
            checkBox.setChecked(checkStatus);
            checkBox.setOnPreferenceChangeListener(new NotificationEpisodesCheckBoxChangeListener(getActivity()));
        }
    }
}
