package rejasupotaro.rebuild.api;

import org.apache.http.Header;

import android.content.Context;

import java.util.List;

import rejasupotaro.asyncrssclient.AsyncRssClient;
import rejasupotaro.asyncrssclient.AsyncRssResponseHandler;
import rejasupotaro.asyncrssclient.RssFeed;
import rejasupotaro.asyncrssclient.RssItem;
import rejasupotaro.rebuild.models.Episode;
import rejasupotaro.rebuild.utils.ListUtils;
import rejasupotaro.rebuild.utils.NetworkUtils;

public class RssFeedClient extends AbstractHttpClient {

    private static final String TAG = RssFeedClient.class.getSimpleName();

    protected String getTag() {
        return TAG;
    }

    private static final String REBUILD_FEED_URL = "http://feeds.rebuild.fm/rebuildfm";

    private static final AsyncRssClient CLIENT = new AsyncRssClient();

    public static interface EpisodeClientResponseHandler {
        public void onSuccess(List<Episode> episodeList);

        public void onFailure();
    }

    public static void init(Context context) {
        setUserAgent(NetworkUtils.getUserAgent(context));
    }

    public static void setUserAgent(String userAgent) {
        CLIENT.setUserAgent(userAgent);
    }

    public void request(final EpisodeClientResponseHandler handler) {
        List<Episode> episodeList = Episode.find();
        if (!ListUtils.isEmpty(episodeList)) {
            handler.onSuccess(episodeList);
            requestNetwork(handler, false);
        } else {
            requestNetwork(handler, true);
        }
    }

    private void requestNetwork(final EpisodeClientResponseHandler handler,
                                final boolean shouldUpdateListView) {
        CLIENT.read(
                REBUILD_FEED_URL,
                new AsyncRssResponseHandler() {
                    @Override
                    public void onSuccess(RssFeed rssFeed) {
                        handleSuccessResponse(handler, shouldUpdateListView, rssFeed);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] body,
                            Throwable throwable) {
                        handleErrorResponse(handler, headers, body, throwable);
                    }
                }
        );
    }

    private void handleSuccessResponse(EpisodeClientResponseHandler handler,
                                       boolean shouldUpdateListView, RssFeed rssFeed) {
        List<RssItem> rssItemList = rssFeed.getRssItemList();
        List<Episode> episodeList = Episode.newEpisodeFromEntity(rssItemList);

        if (Episode.refreshTable(episodeList) || shouldUpdateListView) {
            handler.onSuccess(Episode.find());
        } else {
            // nothing to do
        }
    }

    private void handleErrorResponse(EpisodeClientResponseHandler handler, Header[] headers,
                                     byte[] body, Throwable throwable) {
        dumpError(headers, body, throwable);
        handler.onFailure();
    }
}
