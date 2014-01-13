package rejasupotaro.rebuild.activities;

import android.os.Bundle;

import rejasupotaro.rebuild.R;
import rejasupotaro.rebuild.views.TwitterWidgetWebView;
import roboguice.inject.InjectView;

public class TwitterWidgetActivity extends RoboActionBarActivity {

    @InjectView(R.id.twitter_widget)
    private TwitterWidgetWebView twitterWidgetView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_twitter_widget);
        twitterWidgetView.init(getApplicationContext());
    }
}
