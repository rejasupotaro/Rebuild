package rejasupotaro.rebuild.api;

import org.apache.http.Header;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Looper;

import java.util.List;

import rejasupotaro.asyncrssclient.AsyncRssClient;
import rejasupotaro.asyncrssclient.AsyncRssResponseHandler;
import rejasupotaro.asyncrssclient.RssFeed;
import rejasupotaro.asyncrssclient.RssItem;
import rejasupotaro.rebuild.data.models.Episode;
import rejasupotaro.rebuild.utils.ListUtils;
import rejasupotaro.rebuild.utils.NetworkUtils;

public class RssFeedClient extends AbstractHttpClient {
    private static final String TAG = RssFeedClient.class.getSimpleName();
    private static final String REBUILD_FEED_URL = "http://feeds.rebuild.fm/rebuildfm";

    protected String getTag() {
        return TAG;
    }

    private static AsyncRssClient client;

    public static interface EpisodeClientResponseHandler {
        public void onSuccess(List<Episode> episodeList);
        public void onFailure();
    }

    public static void init(final Context context) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                client = new AsyncRssClient();
                setUserAgent(NetworkUtils.getUserAgent(context));
            }
        }).start();
    }

    public static void setUserAgent(String userAgent) {
        client.setUserAgent(userAgent);
    }

    public void request(final EpisodeClientResponseHandler handler) {
        List<Episode> episodeList = ListUtils.orderByPostedAt(Episode.find());
        if (!ListUtils.isEmpty(episodeList)) {
            handler.onSuccess(episodeList);
            requestNetwork(handler, false);
        } else {
            requestNetwork(handler, true);
        }
    }

    private void requestNetwork(final EpisodeClientResponseHandler handler,
                                final boolean shouldUpdateListView) {
        client.read(
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

    private void handleSuccessResponse(final EpisodeClientResponseHandler handler,
                                       final boolean shouldUpdateListView, final RssFeed rssFeed) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                List<RssItem> rssItemList = rssFeed.getRssItems();
                List<Episode> episodeList = Episode.newEpisodeFromEntity(rssItemList);

                if (Episode.refreshTable(episodeList) || shouldUpdateListView) {
                    handler.onSuccess(ListUtils.orderByPostedAt(Episode.find()));
                } else {
                    // nothing to do
                }
            }
        });
    }

    private void handleErrorResponse(EpisodeClientResponseHandler handler, Header[] headers,
                                     byte[] body, Throwable throwable) {
        dumpError(headers, body, throwable);
        handler.onFailure();
    }
}
