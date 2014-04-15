package rejasupotaro.rebuild.helpers;

import android.content.Context;

import javax.inject.Inject;

import roboguice.activity.RoboFragmentActivity;

public class ActivityHelper {

    @Inject
    private Context context;

    public RoboFragmentActivity getActivity() {
        return (RoboFragmentActivity) context;
    }
}
