package rejasupotaro.rebuild.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.Random;

import rejasupotaro.rebuild.R;
import rejasupotaro.rebuild.models.Tweet;
import rejasupotaro.rebuild.utils.PicassoHelper;
import rejasupotaro.rebuild.utils.Timer;
import rejasupotaro.rebuild.utils.UiAnimations;

public class RecentlyTweetView extends FrameLayout {

    private View rootView;

    private ImageView userProfileImage;

    private TextView tweetTextView;

    private int index = 0;

    public RecentlyTweetView(Context context) {
        super(context);
        setupView(context);
    }

    public RecentlyTweetView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setupView(context);
    }

    public RecentlyTweetView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setupView(context);
    }

    private void setupView(Context context) {
        rootView = View.inflate(context, R.layout.view_recently_tweet, null);
        userProfileImage = (ImageView) rootView.findViewById(R.id.user_profile_image);
        tweetTextView = (TextView) rootView.findViewById(R.id.tweet_text);
        addView(rootView);
    }

    public void setTweetList(final List<Tweet> tweetList) {
        new Timer(new Timer.Callback() {
            @Override
            public void tick(long timeMillis) {
                if (tweetList.size() <= index) {
                    index = 0;
                }

                Tweet tweet = tweetList.get(index++);
                updateView(tweet);
            }
        }, 8000).start();
    }

    private void updateView(Tweet tweet) {
        UiAnimations.fadeIn(rootView, 0, 500);
        UiAnimations.slideLeft(getContext(), rootView, 0, 800);
        PicassoHelper.load(getContext(), userProfileImage, tweet.getUserImageUrl());
        tweetTextView.setText(tweet.getText());
    }
}
