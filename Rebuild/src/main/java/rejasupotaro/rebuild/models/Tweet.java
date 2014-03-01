package rejasupotaro.rebuild.models;

import twitter4j.Status;

public class Tweet {

    private long id;

    private String userImageUrl;

    private String userName;

    private String text;

    public long getId() {
        return id;
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

    public Tweet(long id, String userImageUrl, String userName, String text) {
        this.id = id;
        this.userImageUrl = userImageUrl;
        this.userName = userName;
        this.text = text;
    }

    public static Tweet fromStatus(Status status) {
        return new Tweet(
                status.getId(),
                status.getUser().getProfileImageURL(),
                status.getUser().getName(),
                status.getText());
    }
}
