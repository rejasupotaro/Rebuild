package rejasupotaro.rebuild.models;

import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

import rejasupotaro.asyncrssclient.models.RssItem;

public class Episode {

    private String mTitle;

    private String mPostedAt;

    private String mDescription;

    private Uri mEnclosure;

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
                    String showNotes) {
        mTitle = title;
        mDescription = description;
        mPostedAt = postedAt;
        mEnclosure = enclosure;
        mShowNotes = showNotes;
    }

    public static Episode newEpisodeFromEntity(RssItem rssItem) {
        return new Episode(
                rssItem.getTitle(),
                rssItem.getDescription(),
                rssItem.getPubDate(),
                rssItem.getMediaEnclosure().getUrl(),
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
                "");
    }
}
