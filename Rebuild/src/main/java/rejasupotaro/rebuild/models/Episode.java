package rejasupotaro.rebuild.models;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
import com.activeandroid.query.Update;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import rejasupotaro.asyncrssclient.MediaEnclosure;
import rejasupotaro.asyncrssclient.RssItem;
import rejasupotaro.rebuild.media.MediaFileManager;
import rejasupotaro.rebuild.utils.DateUtils;
import rejasupotaro.rebuild.utils.StringUtils;

@Table(name = "episodes")
public class Episode extends Model implements Parcelable {

    @Column(name = "episode_id", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    private int episodeId;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "link")
    private Uri link;

    @Column(name = "posted_at")
    private String postedAt;

    @Column(name = "enclosure")
    private Uri enclosure;

    @Column(name = "duration")
    private String duration;

    @Column(name = "show_notes")
    private String showNotes;

    @Column(name = "favorited")
    private boolean isFavorited;

    @Column(name = "played")
    private boolean hasPlayed;

    @Column(name = "media_local_path")
    private String mediaLocalPath;

    public int getEpisodeId() {
        return episodeId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Uri getLink() {
        return link;
    }

    public String getPostedAt() {
        return postedAt;
    }

    public Uri getEnclosure() {
        return enclosure;
    }

    public String getDuration() {
        return duration;
    }

    public String getShowNotes() {
        return showNotes;
    }

    public boolean isDownloaded() {
        if (TextUtils.isEmpty(mediaLocalPath)) {
            return false;
        }

        boolean result = MediaFileManager.exists(mediaLocalPath);
        if (!result) {
            mediaLocalPath = null;
        }
        return result;
    }

    public boolean isFavorited() {
        return isFavorited;
    }

    public void favorite() {
        isFavorited = true;
    }

    public boolean hasPlayed() {
        return hasPlayed;
    }

    public void play() {
        hasPlayed = true;
    }

    public String getMediaLocalPath() {
        return mediaLocalPath;
    }

    public void setMediaLocalPath(String mediaLocalPath) {
        this.mediaLocalPath = mediaLocalPath;
    }

    public void clearCache() {
        if (TextUtils.isEmpty(mediaLocalPath)) return;

        File file = new File(mediaLocalPath);
        file.delete();
        mediaLocalPath = null;
        save();
    }

    public Episode() {
        super();
    }

    public boolean isSameEpisode(Episode episode) {
        if (TextUtils.isEmpty(title) || episode == null) {
            return false;
        }

        return (title.equals(episode.getTitle()));
    }

    private Episode(int episodeId, String title, String description, Uri link, String postedAt,
                    Uri enclosure, String duration, String showNotes) {
        super();
        this.episodeId = episodeId;
        this.title = title;
        this.description = StringUtils.removeNewLines(description);
        this.link = link;
        this.postedAt = DateUtils.formatPubDate(postedAt);
        this.enclosure = enclosure;
        this.duration = duration;
        this.showNotes = showNotes;
    }

    public static Episode newEpisodeFromEntity(RssItem rssItem) {
        MediaEnclosure mediaEnclosure = rssItem.getMediaEnclosure();
        return new Episode(
                buildIdFromLink(rssItem.getLink()),
                rssItem.getTitle(),
                getDescription(rssItem),
                rssItem.getLink(),
                rssItem.getPubDate(),
                (mediaEnclosure == null ? null : mediaEnclosure.getUrl()),
                rssItem.getDuration(),
                rssItem.getDescription());
    }

    private static String getDescription(RssItem rssItem) {
        String text = rssItem.getDescription();
        int startIndex = text.indexOf("<h3>Show Notes</h3>");
        return StringUtils.removeNewLines(text.substring(0, startIndex));
    }

    public static List<Episode> newEpisodeFromEntity(List<RssItem> rssItemList) {
        List<Episode> episodeList = new ArrayList<Episode>();
        for (RssItem rssItem : rssItemList) {
            episodeList.add(newEpisodeFromEntity(rssItem));
        }
        return episodeList;
    }

    private static int buildIdFromLink(Uri uri) {
        String path = uri.getPath();
        String formattedId = path.substring(1);
        if (formattedId.endsWith("/")) {
            formattedId = formattedId.substring(0, formattedId.length() - 1);
        }
        return Integer.valueOf(formattedId);
    }

    public static List<Episode> find() {
        return new Select().from(Episode.class).orderBy("episode_id DESC").execute();
    }

    public static Episode findById(int episodeId) {
        return new Select().from(Episode.class).where("episode_id=?", episodeId).executeSingle();
    }

    public static boolean refreshTable(List<Episode> episodeList) {
        if (episodeList == null || episodeList.size() == 0) {
            return false;
        }

        int count = new Select().from(Episode.class).execute().size();
        for (Episode episode : episodeList) {
            episode.upsert();
        }

        return (count != episodeList.size());
    }

    private void upsert() {
        Episode episode =
                new Select().from(Episode.class).where("episode_id=?", episodeId).executeSingle();
        if (episode == null) {
            save();
        } else {
            update();
        }
    }

    public void update() {
        new Update(Episode.class)
                .set("title=?,description=?,link=?,posted_at=?,enclosure=?,duration=?,show_notes=?",
                        title,
                        description,
                        link,
                        postedAt,
                        enclosure,
                        duration,
                        showNotes)
                .where("episode_id=?", episodeId)
                .execute();
    }

    public void insertMediaLocalPath(String mediaLocalPath) {
        this.mediaLocalPath = mediaLocalPath;
        new Update(Episode.class)
                .set("media_local_path=?", mediaLocalPath)
                .where("episode_id=?", episodeId)
                .execute();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(episodeId);
        dest.writeString(title);
        dest.writeString(description);
        dest.writeString(link.toString());
        dest.writeString(postedAt);
        dest.writeString(enclosure == null ? "" : enclosure.toString());
        dest.writeString(duration);
        dest.writeString(showNotes);
        dest.writeInt(isFavorited ? 1 : 0);
        dest.writeInt(hasPlayed ? 1 : 0);
        dest.writeString(mediaLocalPath);
    }

    public static final Creator<Episode> CREATOR = new Creator<Episode>() {
        public Episode createFromParcel(Parcel in) {
            return new Episode(in);
        }

        public Episode[] newArray(int size) {
            return new Episode[size];
        }
    };

    private Episode(Parcel in) {
        episodeId = in.readInt();
        title = in.readString();
        description = in.readString();
        link = Uri.parse(in.readString());
        postedAt = in.readString();
        String uriString = in.readString();
        enclosure = (TextUtils.isEmpty(uriString) ? null : Uri.parse(uriString));
        duration = in.readString();
        showNotes = in.readString();
        isFavorited = (in.readInt() == 1);
        hasPlayed = (in.readInt() == 1);
        mediaLocalPath = in.readString();
    }
}
