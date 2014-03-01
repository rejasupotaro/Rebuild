package rejasupotaro.rebuild.models;

import twitter4j.User;

public class Guest {

    private String name;

    public String getName() {
        return name;
    }

    public Guest() {
    }

    public Guest(String name) {
        this.name = name;
    }

    public static Guest fromUser(User user) {
        return new Guest(
                user.getScreenName());
    }
}
