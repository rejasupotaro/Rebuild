package rejasupotaro.rebuild.utils;

import com.squareup.picasso.Picasso;

import android.content.Context;
import android.widget.ImageView;

import rejasupotaro.rebuild.R;

public class PicassoHelper {

    public static void load(Context context, ImageView target, String url) {
        Picasso.with(context)
                .load(url)
                .into(target);
    }

    public static void loadThumbnail(Context context, ImageView target, String url) {
        Picasso.with(context)
                .load(buildSiteThumbnailUrl(url))
                .placeholder(R.drawable.loading)
                .into(target);
    }

    public static final String buildSiteThumbnailUrl(String linkUrl) {
        return "http://capture.heartrails.com/256x256?" + linkUrl;
    }
}
