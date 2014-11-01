package rejasupotaro.rebuild.jobs;

import android.os.AsyncTask;
import android.text.TextUtils;

import rejasupotaro.rebuild.api.TwitterApiClient;
import rejasupotaro.rebuild.data.models.Guest;

public class UpdateGuestTask extends AsyncTask<Void, Void, Void> {
    private String guestName;

    public UpdateGuestTask(String guestName) {
        this.guestName = guestName;
    }

    @Override
    protected Void doInBackground(Void... params) {
        if (TextUtils.isEmpty(guestName)) {
            return null;
        }

        Guest guest = TwitterApiClient.getInstance().getUser(guestName);
        if (!Guest.isEmpty(guest)) {
            guest.save();
        }

        return null;
    }
}
