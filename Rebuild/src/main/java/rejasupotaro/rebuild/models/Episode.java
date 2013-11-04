package rejasupotaro.rebuild.models;

import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

import rejasupotaro.asyncrssclient.RssItem;
import rejasupotaro.rebuild.utils.DateUtils;
import rejasupotaro.rebuild.utils.StringUtils;

public class Episode {

    public static final String TAG = Episode.class.getSimpleName();

    private String mTitle;

    private String mPostedAt;

    private String mDescription;

    private Uri mEnclosure;

    private String mDuration;

    private String mShowNotes;

    private int mFavoritedCount;

    private int mCommentedCount;

    public String getTitle() {
        return mTitle;
    }

    public String getDescription() {
        return mDescription;
    }

    public String getPostedAt() {
        return mPostedAt;
    }

    public Uri getEnclosure() {
        return mEnclosure;
    }

    public String getDuration() {
        return mDuration;
    }

    public String getShowNotes() {
        return mShowNotes;
    }

    public int getFavoritedCount() {
        return mFavoritedCount;
    }

    public int getCommentedCount() {
        return mCommentedCount;
    }

    private Episode(String title, String description, String postedAt, Uri enclosure,
                    String duration, String showNotes) {
        mTitle = title;
        mDescription = StringUtils.removeNewLines(description);
        mPostedAt = DateUtils.formatPubDate(postedAt);
        mEnclosure = enclosure;
        mDuration = duration;
        mShowNotes = showNotes;
    }

    public static Episode newEpisodeFromEntity(RssItem rssItem) {
        return new Episode(
                rssItem.getTitle(),
                rssItem.getSubtitle(),
                rssItem.getPubDate(),
                rssItem.getMediaEnclosure().getUrl(),
                rssItem.getDuration(),
                "");
    }

    public static List<Episode> newEpisodeFromEntity(List<RssItem> rssItemList) {
        List<Episode> episodeList = new ArrayList<Episode>();
        for (RssItem rssItem : rssItemList) {
            episodeList.add(newEpisodeFromEntity(rssItem));
        }
        return episodeList;
    }

    public static Episode newDummyInstance() {
        return new Episode(
                "24: Go, Mavericks, LinkedIn Intro (typester)",
                "Daisuke Muraseさん (@typester) をゲストに迎えて、Go, OS X Mavericks, Safari Notifications, LinkedIn Intro, Tweetbot などについて話しました。",
                "Thu, 31 Oct 2013 00:00:00 -0700",
                Uri.parse("http://tracking.feedpress.it/link/1949/5437/podcast-ep24.mp3"),
                "54:32",
                "");
    }
}
