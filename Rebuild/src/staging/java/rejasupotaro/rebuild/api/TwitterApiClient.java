package rejasupotaro.rebuild.api;

import java.util.ArrayList;
import java.util.List;

import twitter4j.TweetEntity;
import twitter4j.Twitter;

public class TwitterApiClient {

    private static TwitterApiClient INSTANCE;

    private Twitter twitter;

    public static synchronized TwitterApiClient getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new TwitterApiClient();
        }
        return INSTANCE;
    }

    private TwitterApiClient() {
    }

    public void getUser(int userId) {
    }

    public List<TweetEntity> search(String keyword) {
        return new ArrayList<TweetEntity>();
    }
}
