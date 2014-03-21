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

public class EpisodeTweetListAdapter extends BindableAdapter<Tweet> {

    private static class ViewHolder {
        ImageView userProfileImageView;
        TextView createdAtTextView;
        TextView userNameTextView;
        TextView tweetTextView;

        public ViewHolder(View view) {
            userProfileImageView = (ImageView) view.findViewById(R.id.user_profile_image);
            createdAtTextView = (TextView) view.findViewById(R.id.created_at_text);
            userNameTextView = (TextView) view.findViewById(R.id.user_name_text);
            tweetTextView = (TextView) view.findViewById(R.id.tweet_text);
        }
    }

    public EpisodeTweetListAdapter(Context context) {
        this(context, new ArrayList<Tweet>());
    }

    public EpisodeTweetListAdapter(Context context,
            List<Tweet> episodeList) {
        super(context, episodeList);
    }

    @Override
    public View newView(LayoutInflater inflater, int position, ViewGroup container) {
        View view = inflater.inflate(R.layout.list_item_episode_tweet, null);
        ViewHolder holder = new ViewHolder(view);
        view.setTag(holder);
        return view;
    }

    @Override
    public void bindView(Tweet item, int position, View view) {
        ViewHolder holder = (ViewHolder) view.getTag();

        PicassoHelper.load(getContext(), holder.userProfileImageView, item.getUserImageUrl());
        holder.createdAtTextView.setText(item.getCreatedAtText());
        holder.userNameTextView.setText(item.getUserName());
        holder.tweetTextView.setText(item.getText());
    }
}

