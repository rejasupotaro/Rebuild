package rejasupotaro.rebuild.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import rejasupotaro.rebuild.R;
import rejasupotaro.rebuild.models.Tweet;
import rejasupotaro.rebuild.utils.PicassoHelper;
import rejasupotaro.rebuild.utils.ViewUtils;

public class TweetListAdapter extends BindableAdapter<Tweet> {

    private static class ViewHolder {
        ImageView userProfileImageView;
        TextView userNameTextView;
        TextView tweetTextView;
        TextView favoriteCountTextView;
        TextView retweetCountTextView;

        public ViewHolder(View view) {
            userProfileImageView = (ImageView) view.findViewById(R.id.user_profile_image);
            userNameTextView = (TextView) view.findViewById(R.id.user_name_text);
            tweetTextView = (TextView) view.findViewById(R.id.tweet_text);
            favoriteCountTextView = (TextView) view.findViewById(R.id.favorite_count_text);
            retweetCountTextView = (TextView) view.findViewById(R.id.retweet_count_text);
        }
    }

    public TweetListAdapter(Context context) {
        this(context, new ArrayList<Tweet>());
    }

    public TweetListAdapter(Context context,
            List<Tweet> episodeList) {
        super(context, episodeList);
    }

    @Override
    public View newView(LayoutInflater inflater, int position, ViewGroup container) {
        View view = inflater.inflate(R.layout.list_item_tweet, null);
        ViewHolder holder = new ViewHolder(view);
        view.setTag(holder);
        return view;
    }

    @Override
    public void bindView(Tweet item, int position, View view) {
        ViewHolder holder = (ViewHolder) view.getTag();

        PicassoHelper.load(getContext(), holder.userProfileImageView, item.getUserImageUrl());
        holder.userNameTextView.setText(item.getUserName());
        ViewUtils.setTweetText(holder.tweetTextView, item.getText());
    }
}
