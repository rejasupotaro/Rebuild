package rejasupotaro.rebuild.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import org.apache.http.protocol.HTTP;

import rejasupotaro.rebuild.data.models.Episode;

public final class IntentUtils {
    private static final String URL_TWITTER = "https://twitter.com/";
    private static final String URL_REBUILD = "http://rebuild.fm";

    private IntentUtils() {}

    public static void openGitHubRepository(Context context) {
        openBrowser(context, "https://github.com/rejasupotaro/Rebuild");
    }

    public static void openTwitter(Context context, long tweetId, String userName) {
        openBrowser(context, "https://twitter.com/" + userName + "/status/" + tweetId);
    }

    public static void openMiyagawaProfile(Context context) {
        openTwitterProfile(context, "miyagawa");
    }

    public static void openTwitterProfile(Context context, String name) {
        openBrowser(context, URL_TWITTER + name);
    }

    public static void openRebuildWeb(Context context) {
        openBrowser(context, URL_REBUILD);
    }

    public static void openBrowser(Context context, String url) {
        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        context.startActivity(intent);
    }

    public static void shareEpisode(Context context, Episode episode) {
        sendPostIntent(context, buildPostMessage(episode));
    }

    public static void sendPostIntent(Context context, String text) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType(HTTP.PLAIN_TEXT_TYPE);
        intent.putExtra(Intent.EXTRA_TEXT, text);
        context.startActivity(intent);
    }

    private static String buildPostMessage(Episode episode) {
        return " / " + episode.getTitle() + " " + episode.getLink() + " #rebuildfm";
    }
}
