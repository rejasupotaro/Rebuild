package rejasupotaro.rebuild.api;

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

    public void search(String keyword) {
    }
}
