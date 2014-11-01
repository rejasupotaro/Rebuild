package rejasupotaro.rebuild.data.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.Date;

import rejasupotaro.rebuild.utils.DateUtils;
import twitter4j.Status;

@Table(name = "tweets")
public class Tweet extends Model {
    @Column(name = "tweet_id", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    private long tweetId;
    @Column(name = "craeted_at")
    private Date createdAt;
    @Column(name = "user_image_url")
    private String userImageUrl;
    @Column(name = "user_name")
    private String userName;
    @Column(name = "text")
    private String text;
    @Column(name = "favorite_count")
    private int favoriteCount;
    @Column(name = "retweet_count")
    private int retweetCount;

    public long getTweetId() {
        return tweetId;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public String getUserImageUrl() {
        return userImageUrl;
    }

    public String getUserName() {
        return userName;
    }

    public String getText() {
        return text;
    }

    public int getFavoriteCount() {
        return favoriteCount;
    }

    public int getRetweetCount() {
        return retweetCount;
    }

    public String getElapsedTimeText() {
        return DateUtils.createdAtToElapsedTimeText(createdAt);
    }

    public String getTweetTimeText() {
        return DateUtils.formatTweetTime(createdAt);
    }

    public Tweet() {
        super();
    }

    public Tweet(long tweetId, Date createdAt, String userImageUrl, String userName, String text,
            int favoriteCount, int retweetCount) {
        this.tweetId = tweetId;
        this.createdAt = createdAt;
        this.userImageUrl = userImageUrl;
        this.userName = "@" + userName;
        this.text = text;
        this.favoriteCount = favoriteCount;
        this.retweetCount = retweetCount;
    }

    public static Tweet fromStatus(Status status) {
        return new Tweet(
                status.getId(),
                status.getCreatedAt(),
                status.getUser().getProfileImageURL(),
                status.getUser().getScreenName(),
                status.getText(),
                status.getFavoriteCount(),
                status.getRetweetCount());
    }

    public static Tweet findById(long tweetId) {
        return new Select().from(Tweet.class).where("tweet_id=?", tweetId).executeSingle();
    }
}
