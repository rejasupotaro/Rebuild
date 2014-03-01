package rejasupotaro.rebuild.views;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import rejasupotaro.rebuild.R;
import rejasupotaro.rebuild.loaders.GuestLoader;
import rejasupotaro.rebuild.models.Guest;

public class GuestListView extends LinearLayout {

    private static final int REQUEST_GUEST_LIST = 1;

    private TextView nameTextView;

    public GuestListView(Context context) {
        super(context);
    }

    public GuestListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setup(List<String> guestNameList) {
        View view = View.inflate(getContext(), R.layout.list_item_guest, null);
        findViews(view);
        addView(view);
        requestGuestList(guestNameList);
    }

    public void findViews(View view) {
        nameTextView = (TextView) view.findViewById(R.id.guest_name_text);
    }

    public void setupGuestList(List<Guest> guestList) {
        for (Guest guest : guestList) {
            Log.e("debugging", guest.getName());
        }
    }

    public void requestGuestList(final List<String> guestNameList) {
        getActivity().getLoaderManager().restartLoader(REQUEST_GUEST_LIST, null,
                new LoaderManager.LoaderCallbacks<List<Guest>>() {
                    @Override
                    public Loader<List<Guest>> onCreateLoader(int i, Bundle bundle) {
                        return new GuestLoader(getContext(), guestNameList);
                    }

                    @Override
                    public void onLoadFinished(Loader<List<Guest>> listLoader,
                            List<Guest> guestList) {
                        setupGuestList(guestList);
                    }

                    @Override
                    public void onLoaderReset(Loader<List<Guest>> listLoader) {
                        // nothing to do
                    }
                });
    }

    public FragmentActivity getActivity() {
        Context context = getContext();
        if (context instanceof FragmentActivity) {
            return (FragmentActivity) context;
        } else {
            throw new IllegalStateException("Parent activity is not FragmentActivity");
        }
    }
}
