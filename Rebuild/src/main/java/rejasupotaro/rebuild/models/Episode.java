package rejasupotaro.rebuild.models;

import android.net.Uri;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.ArrayList;
import java.util.List;

import rejasupotaro.asyncrssclient.RssItem;
import rejasupotaro.rebuild.utils.DateUtils;
import rejasupotaro.rebuild.utils.StringUtils;

@Table(name = "episodes")
public class Episode extends Model {

    public static final String TAG = Episode.class.getSimpleName();

    @Column(name = "title")
    private String mTitle;

    @Column(name = "posted_at")
    private String mPostedAt;

    @Column(name = "description")
    private String mDescription;

    @Column(name = "enclosure")
    private Uri mEnclosure;

    @Column(name = "duration")
    private String mDuration;

    @Column(name = "show_notes")
    private String mShowNotes;

    @Column(name = "favorited_count")
    private int mFavoritedCount;

    @Column(name = "commented_count")
    private int mCommentedCount;

    @Column(name = "media_local_path")
    private String mMediaLocalPath;

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

    public String getMediaLocalPath() {
        return mMediaLocalPath;
    }

    public Episode() {
        super();
    }

    private Episode(String title, String description, String postedAt, Uri enclosure,
                    String duration, String showNotes) {
        super();
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

    public static List<Episode> find() {
        return new Select().from(Episode.class).orderBy("id ASC").execute();
    }

    public void upsert() {
        Episode other = new Select().from(Episode.class).where("title=?", mTitle).executeSingle();
        if (other == null) {
            save();
        } else {
            // TODO: update
        }
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
