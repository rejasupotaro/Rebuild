package rejasupotaro.rebuild.models;

import twitter4j.User;

public class Guest {

    private long id;

    private String name;

    private String profileImageUrl;

    private int tweetsCount;

    private int friendsCount;

    private int followersCount;

    private String description;

    private String url;

    public long getId() {
        return id;
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

    public Guest() {
    }

    public Guest(long id, String name, String profileImageUrl, int tweetsCount, int friendsCount,
            int followersCount, String description, String url) {
        this.id = id;
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
}
