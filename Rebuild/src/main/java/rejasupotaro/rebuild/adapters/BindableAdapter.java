package rejasupotaro.rebuild.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;

public abstract class BindableAdapter<T> extends ArrayAdapter<T> {

    private LayoutInflater mInflater;

    public BindableAdapter(Context context, int resource) {
        super(context, resource);
        setup(context);
    }

    public BindableAdapter(Context context, int resource, List<T> episodeList) {
        super(context, resource, episodeList);
        setup(context);
    }

    private void setup(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public final View getView(int position, View view, ViewGroup container) {
        if (view == null) {
            view = newView(mInflater, position, container);
            if (view == null) {
                throw new IllegalStateException("newView result must not be null.");
            }
        }
        bindView(getItem(position), position, view);
        return view;
    }

    public abstract View newView(LayoutInflater inflater, int position, ViewGroup container);

    public abstract void bindView(T item, int position, View view);
}
