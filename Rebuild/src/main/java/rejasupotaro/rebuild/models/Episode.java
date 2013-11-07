package rejasupotaro.rebuild.models;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import rejasupotaro.asyncrssclient.RssItem;
import rejasupotaro.rebuild.utils.DateUtils;
import rejasupotaro.rebuild.utils.StringUtils;

@Table(name = "episodes")
public class Episode extends Model implements Parcelable {

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

    public static boolean deleteAndSave(List<Episode> episodeList) {
        boolean isUpdated = false;
        if (episodeList == null || episodeList.size() == 0) {
            return isUpdated;
        }

        int count = new Select().from(Episode.class).execute().size();
        if (count == episodeList.size()) {
            return isUpdated;
        }

        new Delete().from(Episode.class).execute();
        for (Episode episode : episodeList) {
            episode.save();
        }
        isUpdated = true;

        return isUpdated;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mTitle);
        dest.writeString(mPostedAt);
        dest.writeString(mDescription);
        dest.writeString(mEnclosure.toString());
        dest.writeString(mDuration);
        dest.writeString(mShowNotes);
        dest.writeInt(mFavoritedCount);
        dest.writeInt(mCommentedCount);
        dest.writeString(mMediaLocalPath);
    }

    public static final Parcelable.Creator<Episode> CREATOR
            = new Parcelable.Creator<Episode>() {
        public Episode createFromParcel(Parcel in) {
            return new Episode(in);
        }

        public Episode[] newArray(int size) {
            return new Episode[size];
        }
    };

    private Episode(Parcel in) {
        mTitle = in.readString();
        mPostedAt = in.readString();
        mDescription = in.readString();
        mEnclosure = Uri.parse(in.readString());
        mDuration = in.readString();
        mShowNotes = in.readString();
        mFavoritedCount = in.readInt();
        mCommentedCount = in.readInt();
        mMediaLocalPath = in.readString();
    }
}
