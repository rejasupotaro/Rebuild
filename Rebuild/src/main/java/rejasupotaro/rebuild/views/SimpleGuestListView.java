package rejasupotaro.rebuild.views;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import rejasupotaro.rebuild.R;
import rejasupotaro.rebuild.data.models.Guest;
import rejasupotaro.rebuild.data.services.GuestService;
import rejasupotaro.rebuild.tools.OnContextExecutor;
import rejasupotaro.rebuild.utils.PicassoHelper;
import rx.functions.Action1;

public class SimpleGuestListView extends LinearLayout {
    private OnContextExecutor onContextExecutor = new OnContextExecutor();

    public SimpleGuestListView(Context context) {
        super(context);
    }

    public SimpleGuestListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setup(List<String> guestNames) {
        if (guestNames == null || guestNames.isEmpty()) {
            return;
        }
        setOrientation(HORIZONTAL);
        requestGuestList(guestNames);
    }

    public void setupGuestList(final List<Guest> guests) {
        if (guests == null || guests.isEmpty()) {
            return;
        }

        onContextExecutor.execute(getContext(), new Runnable() {
            @Override
            public void run() {
                removeAllViews();
                for (Guest guest : guests) {
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

    public void requestGuestList(final List<String> guestNames) {
        new GuestService().getList(guestNames)
                .subscribe(new Action1<ArrayList<Guest>>() {
                    @Override
                    public void call(ArrayList<Guest> guests) {
                        setupGuestList(guests);
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

