package rejasupotaro.rebuild.views;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import rejasupotaro.rebuild.R;
import rejasupotaro.rebuild.loaders.GuestLoader;
import rejasupotaro.rebuild.models.Guest;
import rejasupotaro.rebuild.tools.OnContextExecutor;
import rejasupotaro.rebuild.utils.PicassoHelper;

public class SimpleGuestListView extends LinearLayout {

    private static final int REQUEST_GUEST_LIST = 1;

    private OnContextExecutor onContextExecutor = new OnContextExecutor();

    public SimpleGuestListView(Context context) {
        super(context);
    }

    public SimpleGuestListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setup(List<String> guestNameList) {
        setOrientation(HORIZONTAL);
        requestGuestList(guestNameList);
    }

    public void setupGuestList(final List<Guest> guestList) {
        if (guestList == null || guestList.isEmpty()) {
            return;
        }

        onContextExecutor.execute(getContext(), new Runnable() {
            @Override
            public void run() {
                for (Guest guest : guestList) {
                    if (Guest.isEmpty(guest)) {
                        continue;
                    }

                    LayoutParams params = new LayoutParams(
                            LayoutParams.MATCH_PARENT,
                            LayoutParams.WRAP_CONTENT,
                            1);
                    params.setMargins(0,
                            getResources().getDimensionPixelSize(R.dimen.spacing_small), 0, 0);
                    addView(createGuestView(guest), params);
                }
            }
        });
    }

    public View createGuestView(final Guest guest) {
        View view = View.inflate(getContext(), R.layout.list_item_simple_guest, null);

        TextView guestNameText = (TextView) view.findViewById(R.id.guest_name_text);
        guestNameText.setText(guest.getName());

        ImageView profileImageView = (ImageView) view.findViewById(R.id.profile_image);
        PicassoHelper.loadAndCircleTransform(getContext(), profileImageView,
                guest.getProfileImageUrl());

        return view;
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
                }
        );
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

