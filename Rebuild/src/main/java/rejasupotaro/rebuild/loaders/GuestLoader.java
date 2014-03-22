package rejasupotaro.rebuild.loaders;

import com.path.android.jobqueue.JobManager;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import rejasupotaro.rebuild.RebuildApplication;
import rejasupotaro.rebuild.api.TwitterApiClient;
import rejasupotaro.rebuild.jobs.UpdateGuestJob;
import rejasupotaro.rebuild.models.Guest;

public class GuestLoader extends AsyncTaskLoader<List<Guest>> {

    private JobManager jobManager;

    private List<String> guestNameList = new ArrayList<String>();

    private List<Guest> guestList = new ArrayList<Guest>();

    public GuestLoader(Context context, List<String> guestNameList) {
        super(context);
        this.guestNameList = guestNameList;
        jobManager = RebuildApplication.getInstance().getJobManager();
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
                jobManager.addJobInBackground(new UpdateGuestJob(guestName));
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

