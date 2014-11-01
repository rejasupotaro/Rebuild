package rejasupotaro.rebuild.data.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import twitter4j.User;

@Table(name = "guests")
public class Guest extends Model {
    @Column(name = "guest_id")
    private long guestId = -1;
    @Column(name = "name", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    private String name;
    @Column(name = "profile_image_url")
    private String profileImageUrl;
    @Column(name = "tweets_count")
    private int tweetsCount;
    @Column(name = "friends_count")
    private int friendsCount;
    @Column(name = "followers_count")
    private int followersCount;
    @Column(name = "description")
    private String description;
    @Column(name = "url")
    private String url;

    public long getGuestId() {
        return guestId;
    }

    public String getName() {
        return name;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public int getTweetsCount() {
        return tweetsCount;
    }

    public int getFriendsCount() {
        return friendsCount;
    }

    public int getFollowersCount() {
        return followersCount;
    }

    public String getDescription() {
        return description;
    }

    public String getUrl() {
        return url;
    }

    public static boolean isEmpty(Guest guest) {
        if (guest == null) {
            return true;
        }
        return (guest.guestId == -1);
    }

    public Guest() {
        super();
    }

    public Guest(long guestId, String name, String profileImageUrl, int tweetsCount, int friendsCount,
            int followersCount, String description, String url) {
        this.guestId = guestId;
        this.name = "@" + name;
        this.profileImageUrl = profileImageUrl;
        this.tweetsCount = tweetsCount;
        this.friendsCount = friendsCount;
        this.followersCount = followersCount;
        this.description = description;
        this.url = url;
    }

    public static Guest fromUser(User user) {
        return new Guest(
                user.getId(),
                user.getScreenName(),
                user.getProfileImageURL(),
                user.getStatusesCount(),
                user.getFriendsCount(),
                user.getFollowersCount(),
                user.getDescription(),
                user.getURL());
    }

    public static Guest findByName(String name) {
        if (!name.startsWith("@")) {
            name = "@" + name;
        }
        return new Select().from(Guest.class).where("name=?", name).executeSingle();
    }
}
