package rejasupotaro.rebuild.data.models;

public enum Developer {
    REJASUPOTARO(
            "https://pbs.twimg.com/profile_images/424554842367852544/jRoDtV1R.jpeg",
            "rejasupotaro",
            "http://twitter.com/rejasupotaro"),
    HOTCHEMI(
            "https://pbs.twimg.com/profile_images/424955294616023040/aco9m_GJ.png",
            "hotchemi",
            "http://twitter.com/hotchemi"),
    MOOTOH(
            "https://avatars.githubusercontent.com/u/2490",
            "mootoh",
            "https://github.com/mootoh"),
    HAK(
            "https://avatars.githubusercontent.com/u/98031",
            "hak",
            "https://github.com/hak");

    private String imageUrl;
    private String name;
    private String link;

    public String getImageUrl() {
        return imageUrl;
    }

    public String getName() {
        return name;
    }

    public String getLink() {
        return link;
    }

    private Developer(String imageUrl, String name, String link) {
        this.imageUrl = imageUrl;
        this.name = name;
        this.link = link;
    }
}
