package rejasupotaro.rebuild.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import rejasupotaro.rebuild.R;
import rejasupotaro.rebuild.models.Episode;

public class EpisodeListAdapter extends ArrayAdapter<Episode> {

    private LayoutInflater mLayoutInflater;

    public EpisodeListAdapter(Context context, int resource, List<Episode> objects) {
        super(context, resource, objects);
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Episode episode = (Episode) getItem(position);
        if (null == convertView) {
            convertView = mLayoutInflater.inflate(R.layout.list_item_episode, null);
        }

        TextView textView = (TextView) convertView.findViewById(R.id.episode_title);
        textView.setText("Hello");

        return convertView;
    }
}
