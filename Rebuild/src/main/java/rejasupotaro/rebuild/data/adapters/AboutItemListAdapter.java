package rejasupotaro.rebuild.data.adapters;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import rejasupotaro.rebuild.R;
import rejasupotaro.rebuild.data.models.AboutItem;
import rejasupotaro.rebuild.utils.IntentUtils;
import rejasupotaro.rebuild.utils.PicassoHelper;
import rejasupotaro.rebuild.utils.ViewUtils;

public class AboutItemListAdapter extends ArrayAdapter<AboutItem> {
    public AboutItemListAdapter(Context context, List<AboutItem> aboutItemList) {
        super(context, 0, aboutItemList);
    }

    @Override
    public final View getView(int position, View view, ViewGroup container) {
        AboutItem item = getItem(position);
        if (item instanceof AboutItem.AboutItemHeader) {
            view = getHeaderView((AboutItem.AboutItemHeader) item);
        } else if (item instanceof AboutItem.AboutItemContent) {
            view = getContentView((AboutItem.AboutItemContent) item);
        }
        return view;
    }

    private View getHeaderView(AboutItem.AboutItemHeader item) {
        View view = View.inflate(getContext(), R.layout.list_item_about_header, null);
        TextView titleTextView = (TextView) view.findViewById(R.id.header_title);
        titleTextView.setText(item.getTitle());
        ViewUtils.disable(view);
        return view;
    }

    private View getContentView(final AboutItem.AboutItemContent item) {
        View view = View.inflate(getContext(), R.layout.list_item_about_content, null);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentUtils.openBrowser(getContext(), item.getLink());
            }
        });

        if (!TextUtils.isEmpty(item.getImageUrl())) {
            ImageView imageView = (ImageView) view.findViewById(R.id.content_image);
            PicassoHelper.loadAndCircleTransform(getContext(), imageView, item.getImageUrl());
        }

        if (!TextUtils.isEmpty(item.getText())) {
            TextView textView = (TextView) view.findViewById(R.id.content_text);
            textView.setText(item.getText());
        }

        return view;
    }
}
