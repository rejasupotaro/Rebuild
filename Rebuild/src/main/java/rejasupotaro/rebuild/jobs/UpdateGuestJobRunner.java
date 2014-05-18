package rejasupotaro.rebuild.jobs;

import android.content.Context;
import android.text.TextUtils;

import java.util.HashMap;
import java.util.Map;

import nirai.Job;
import nirai.JobRunner;
import nirai.exception.NetworkOfflineException;
import rejasupotaro.rebuild.api.TwitterApiClient;
import rejasupotaro.rebuild.models.Guest;

public class UpdateGuestJobRunner implements JobRunner {

    private static final String ARGS_GUEST_NAME = "guest_name";

    public static Job createJob(String guestName) {
        return new Job(UpdateGuestJobRunner.class, createArgs(guestName));

    }

    private static Map<String, Object> createArgs(String guestName) {
        HashMap<String, Object> args = new HashMap<String, Object>();
        args.put(ARGS_GUEST_NAME, guestName);
        return args;
    }

    private String getGuestName(Map<String, Object> args) {
        if (args == null) {
            return "";
        }

        return (String) args.get(ARGS_GUEST_NAME);
    }

    @Override
    public void run(Context context, Map<String, Object> args) throws NetworkOfflineException {
        String guestName = getGuestName(args);
        if (TextUtils.isEmpty(guestName)) {
            return;
        }

        Guest guest = TwitterApiClient.getInstance().getUser(guestName);
        if (!Guest.isEmpty(guest)) {
            guest.save();
        }
    }
}

