package rejasupotaro.rebuild.data.loaders;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import rejasupotaro.rebuild.api.TwitterApiClient;
import rejasupotaro.rebuild.jobs.UpdateGuestTask;
import rejasupotaro.rebuild.data.models.Guest;

public class GuestLoader extends AsyncTaskLoader<List<Guest>> {
    private List<String> guestNameList = new ArrayList<String>();
    private List<Guest> guestList = new ArrayList<Guest>();

    public GuestLoader(Context context, List<String> guestNameList) {
        super(context);
        this.guestNameList = guestNameList;
    }

    @Override
    protected void onStartLoading() {
        if (!guestList.isEmpty()) {
            deliverResult(guestList);
        } else if (takeContentChanged() || guestList.isEmpty()) {
            forceLoad();
        }
    }

    @Override
    public List<Guest> loadInBackground() {
        List<Guest> guestList = new ArrayList<Guest>();
        for (String guestName : guestNameList) {
            Guest guest = Guest.findByName(guestName);
            if (!Guest.isEmpty(guest)) {
                guestList.add(guest);
                new UpdateGuestTask(guestName).execute();
                continue;
            }

            guest = TwitterApiClient.getInstance().getUser(guestName);
            if (!Guest.isEmpty(guest)) {
                guestList.add(guest);
                guest.save();
            }
        }
        return guestList;
    }

    @Override
    public void deliverResult(List<Guest> data) {
        if (isReset()) {
            if (!guestList.isEmpty()) {
                guestList.clear();
            }
            return;
        }

        guestList = data;

        if (isStarted()) {
            super.deliverResult(data);
        }
    }
}

