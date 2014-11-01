package rejasupotaro.rebuild.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import rejasupotaro.rebuild.R;
import rejasupotaro.rebuild.data.models.Tweet;
import rejasupotaro.rebuild.utils.PicassoHelper;
import rejasupotaro.rebuild.utils.Timer;
import rejasupotaro.rebuild.utils.UiAnimations;

public class RecentTweetView extends FrameLayout {
    private static final int SWITCH_TWEET_INTERVAL = 6000;

    private View rootView;
    private ImageView userProfileImage;
    private TextView userNameTextView;
    private TextView tweetTextView;
    private int index = 0;

    public RecentTweetView(Context context) {
        super(context);
        setupView(context);
    }

    public RecentTweetView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setupView(context);
    }

    public RecentTweetView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setupView(context);
    }

    private void setupView(Context context) {
        rootView = View.inflate(context, R.layout.view_recent_tweet, null);
        userProfileImage = (ImageView) rootView.findViewById(R.id.user_profile_image);
        userNameTextView = (TextView) rootView.findViewById(R.id.user_name_text);
        tweetTextView = (TextView) rootView.findViewById(R.id.tweet_text);
        addView(rootView);
    }

    public void setTweetList(final List<Tweet> tweetList) {
        if (tweetList.size() <= 0) {
            return;
        }

        new Timer(new Timer.Callback() {
            @Override
            public void tick(long timeMillis) {
                if (tweetList.size() <= index) {
                    index = 0;
                }

                Tweet tweet = tweetList.get(index++);
                updateView(tweet);
            }
        }, SWITCH_TWEET_INTERVAL).start();
    }

    private void updateView(Tweet tweet) {
        UiAnimations.fadeIn(rootView, 0, 500);
        UiAnimations.slideLeft(getContext(), rootView, 0, 800);
        PicassoHelper.load(getContext(), userProfileImage, tweet.getUserImageUrl());
        userNameTextView.setText(tweet.getUserName());
        tweetTextView.setText(tweet.getText());
    }
}
