package rejasupotaro.rebuild.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
import com.activeandroid.query.Update;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import rejasupotaro.asyncrssclient.MediaEnclosure;
import rejasupotaro.asyncrssclient.RssItem;
import rejasupotaro.rebuild.media.MediaFileManager;
import rejasupotaro.rebuild.utils.DateUtils;
import rejasupotaro.rebuild.utils.StringUtils;

@Table(name = "episodes")
public class Episode extends Model implements Parcelable {

    private static final String TAG = Episode.class.getSimpleName();

    @Column(name = "eid", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    private String id;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "link")
    private Uri link;

    @Column(name = "posted_at")
    private Date postedAt;

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

    public String getEpisodeId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Uri getLink() {
        return  link;
    }

    public Date getPostedAt() {
        return postedAt;
    }

    public String getPostedAtAsString() {
        return DateUtils.dateToString(postedAt);
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

    public boolean isFavorited() {
        return isFavorited;
    }

    public boolean hasPlayed() {
        return hasPlayed;
    }

    public String getMediaLocalPath() {
        return mediaLocalPath;
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
    public void play() {
        hasPlayed = true;
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

    private Episode(String id, String title, String description, Uri link, String pubDate,
                    Uri enclosure, String duration, String showNotes) {
        super();
        this.id = id;
        this.title = title;
        this.description = StringUtils.removeNewLines(description);
        this.link = link;
        this.postedAt = DateUtils.pubDateToDate(pubDate);
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
            try {
                Episode episode = newEpisodeFromEntity(rssItem);
                if (episode != null) {
                    episodeList.add(episode);
                }
            } catch (Exception e) {
                Log.e(TAG, e.getMessage(), e);
            }
        }
        return episodeList;
    }

    private static String buildIdFromLink(Uri uri) {
        String path = uri.getPath();
        String formattedId = path.substring(1);
        if (formattedId.endsWith("/")) {
            formattedId = formattedId.substring(0, formattedId.length() - 1);
        }
        return formattedId;
    }

    public static List<Episode> find() {
        List<Episode> episodeList = new Select().from(Episode.class).orderBy("Id ASC").execute();
        return episodeList;
    }

    public static Episode findById(String episodeId) {
        return new Select().from(Episode.class).where("eid=?", episodeId).executeSingle();
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
                new Select().from(Episode.class).where("eid=?", id).executeSingle();
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
                .where("eid=?", id)
                .execute();
    }

    public void insertMediaLocalPath(String mediaLocalPath) {
        this.mediaLocalPath = mediaLocalPath;
        new Update(Episode.class)
                .set("media_local_path=?", mediaLocalPath)
                .where("eid=?", id)
                .execute();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(title);
        dest.writeString(description);
        dest.writeString(link.toString());
        dest.writeLong(postedAt.getTime());
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
        id = in.readString();
        title = in.readString();
        description = in.readString();
        link = Uri.parse(in.readString());
        postedAt = new Date(in.readLong());
        String uriString = in.readString();
        enclosure = (TextUtils.isEmpty(uriString) ? null : Uri.parse(uriString));
        duration = in.readString();
        showNotes = in.readString();
        isFavorited = (in.readInt() == 1);
        hasPlayed = (in.readInt() == 1);
        mediaLocalPath = in.readString();
    }
}
