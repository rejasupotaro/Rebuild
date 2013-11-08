package rejasupotaro.rebuild.api;

import android.util.Log;

import org.apache.http.Header;

import java.io.UnsupportedEncodingException;
import java.util.List;

import rejasupotaro.asyncrssclient.AsyncRssClient;
import rejasupotaro.asyncrssclient.AsyncRssResponseHandler;
import rejasupotaro.asyncrssclient.RssFeed;
import rejasupotaro.asyncrssclient.RssItem;
import rejasupotaro.rebuild.models.Episode;
import rejasupotaro.rebuild.utils.ListUtils;

public class EpisodeClient {

    private static final String TAG = EpisodeClient.class.getSimpleName();

    private static final String REBUILD_FEED_URL = "http://feeds.rebuild.fm/rebuildfm";

    private static final AsyncRssClient sAsyncRssClient = new AsyncRssClient();

    public static interface EpisodeClientResponseHandler {
        public void onSuccess(List<Episode> episodeList);

        public void onFailure();
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
        sAsyncRssClient.read(
                REBUILD_FEED_URL,
                new AsyncRssResponseHandler() {
                    @Override
                    public void onSuccess(RssFeed rssFeed) {
                        handleSuccessResponse(handler, shouldUpdateListView, rssFeed);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] body, Throwable throwable) {
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
            handler.onSuccess(episodeList);
        } else {
            // nothing to do
        }
    }

    private void handleErrorResponse(EpisodeClientResponseHandler handler, Header[] headers,
                                     byte[] body, Throwable throwable) {
        dumpError(headers, body, throwable);
        handler.onFailure();
    }

    private void dumpError(Header[] headers, byte[] body, Throwable throwable) {
        for (Header header : headers) {
            Log.e(TAG, header.getName() + " => " + header.getValue());
        }

        try {
            Log.e(TAG, new String(body, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        Log.e(TAG, throwable.toString());
    }
}
