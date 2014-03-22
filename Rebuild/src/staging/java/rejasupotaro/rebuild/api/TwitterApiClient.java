package rejasupotaro.rebuild.api;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import rejasupotaro.rebuild.models.Guest;
import rejasupotaro.rebuild.models.Tweet;
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

    public Guest getUser(String userName) {
        return new Guest(235544534,
                "rejasupotaro",
                "http://pbs.twimg.com/profile_images/424554842367852544/jRoDtV1R_normal.jpeg",
                12897,
                298,
                999,
                "顧客に価値を届け続けるオアダイ",
                "http://t.co/IxfgYdk6nK");
    }

    public List<Tweet> findTweetById(List<Long> ids) {
        List<Tweet> tweetList = new ArrayList<Tweet>();
        for (long id : ids) {
            Tweet tweet = findTweetById(id);
            if (tweet != null) {
                tweetList.add(tweet);
            }
        }
        return tweetList;
    }

    public Tweet findTweetById(long id) {
        return newDummyTweet();
    }

    public List<Tweet> search(String keyword, boolean shouldClearQuery) {
        List<Tweet> tweetList = new ArrayList<Tweet>();
        for (int i = 0; i < 20; i++) {
            tweetList.add(newDummyTweet());
        }
        return tweetList;
    }

    private Tweet newDummyTweet() {
        return new Tweet(
                362409221523910657L,
                new Date(),
                "https://pbs.twimg.com/profile_images/378800000217831113/d9a348d7ff1d6e089dfec2ead82a1bf4.png",
                "rebuildfm",
                "Announcing Rebuild, new Podcast by @miyagawa. http://rebuild.fm/",
                10,
                10);
    }
}
