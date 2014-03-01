package rejasupotaro.rebuild.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import rejasupotaro.rebuild.R;
import rejasupotaro.rebuild.models.Tweet;

public class TweetListAdapter extends BindableAdapter<Tweet> {

    public TweetListAdapter(Context context,
            List<Tweet> episodeList) {
        super(context, episodeList);
    }

    @Override
    public View newView(LayoutInflater inflater, int position, ViewGroup container) {
        return inflater.inflate(R.layout.list_item_tweet, null);
    }

    @Override
    public void bindView(Tweet item, int position, View view) {
        TextView tweetTextView = (TextView) view.findViewById(R.id.tweet_text);
        tweetTextView.setText(item.getText());
    }
}
