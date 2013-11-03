package rejasupotaro.rebuild.models;

public class Episode {

    private String mTitle;

    private String mPostedAt;

    private String mDescription;

    private String mEnclosure;

    private String mShowNotes;

    private int mFavoritedCount;

    private int mCommentedCount;

    public String getTitle() {
        return mTitle;
    }

    public String getDescription() {
        return mDescription;
    }

    public String getPostedAt() {
        return mPostedAt;
    }

    public String getEnclosure() {
        return mEnclosure;
    }

    public String getShowNotes() {
        return mShowNotes;
    }

    public int getFavoritedCount() {
        return mFavoritedCount;
    }

    public int getCommentedCount() {
        return mCommentedCount;
    }

    private Episode(String title, String description, String postedAt, String enclosure,
                    String showNotes, int favoritedCount, int commentedCount) {
        mTitle = title;
        mDescription = description;
        mPostedAt = postedAt;
        mEnclosure = enclosure;
        mShowNotes = showNotes;
        mFavoritedCount = favoritedCount;
        mCommentedCount = commentedCount;
    }

    public static Episode newDummyInstance() {
        Episode episode = new Episode(
                "24: Go, Mavericks, LinkedIn Intro (typester)",
                "Daisuke Muraseさん (@typester) をゲストに迎えて、Go, OS X Mavericks, Safari Notifications, LinkedIn Intro, Tweetbot などについて話しました。",
                "Thu, 31 Oct 2013 00:00:00 -0700",
                "http://tracking.feedpress.it/link/1949/5437/podcast-ep24.mp3",
                "",
                3,
                3);
        return episode;
    }
}
