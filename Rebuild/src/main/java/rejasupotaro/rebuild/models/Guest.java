package rejasupotaro.rebuild.models;

import java.util.ArrayList;
import java.util.List;

import rejasupotaro.rebuild.utils.StringUtils;

public class Guest {

    private String name;

    public String getName() {
        return name;
    }

    public Guest(String name) {
        this.name = name;
    }

    public static List<Guest> fromDescription(String description) {
        List<Guest> guestList = new ArrayList<Guest>();
        List<String> guestNameList = StringUtils.getGuestNames(description);
        if (guestNameList == null || guestNameList.isEmpty()) {
            return guestList;
        }

        for (String guestName : guestNameList) {
            guestList.add(new Guest(guestName));
        }
        return guestList;
    }
}
