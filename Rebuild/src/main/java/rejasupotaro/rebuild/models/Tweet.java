package rejasupotaro.rebuild.models;

import twitter4j.Status;

public class Tweet {

    private String userImageUrl;

    private String text;

    public String getUserImageUrl() {
        return userImageUrl;
    }

    public String getText() {
        return text;
    }

    public Tweet(String userImageUrl, String text) {
        this.userImageUrl = userImageUrl;
        this.text = text;
    }

    public static Tweet fromStatus(Status status) {
        return new Tweet(
                status.getUser().getMiniProfileImageURL(),
                status.getText());
    }
}
