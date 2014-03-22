package rejasupotaro.rebuild.jobs;

import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.Params;

import rejasupotaro.rebuild.api.TwitterApiClient;
import rejasupotaro.rebuild.models.Guest;

public class UpdateGuestJob extends Job {

    private String guestName;

    public UpdateGuestJob(String guestName) {
        super(new Params(Priority.LOW).requireNetwork().groupBy("update-guest"));
        this.guestName = guestName;
    }

    @Override
    public void onAdded() {
    }

    @Override
    public void onRun() throws Throwable {
        Guest guest = TwitterApiClient.getInstance().getUser(guestName);
        if (!Guest.isEmpty(guest)) {
            guest.save();
        }
    }

    @Override
    protected void onCancel() {
    }

    @Override
    protected boolean shouldReRunOnThrowable(Throwable throwable) {
        return false;
    }
}
