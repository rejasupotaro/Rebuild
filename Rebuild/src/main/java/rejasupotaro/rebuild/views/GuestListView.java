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
import rejasupotaro.rebuild.utils.IntentUtils;
import rejasupotaro.rebuild.utils.PicassoHelper;
import rx.functions.Action1;

public class GuestListView extends LinearLayout {
    private OnContextExecutor onContextExecutor = new OnContextExecutor();

    public GuestListView(Context context) {
        super(context);
    }

    public GuestListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setup(List<String> guestNames) {
        if (guestNames == null || guestNames.isEmpty()) {
            return;
        }
        setOrientation(VERTICAL);
        requestGuestList(guestNames);
    }

    public void setupGuestList(final List<Guest> guests) {
        if (guests == null || guests.isEmpty()) {
            return;
        }

        onContextExecutor.execute(getContext(), new Runnable() {
            @Override
            public void run() {
                SectionHeaderView sectionHeaderView = new SectionHeaderView(getContext());
                sectionHeaderView.setText("Guests");
                addView(sectionHeaderView);

                for (Guest guest : guests) {
                    if (Guest.isEmpty(guest)) {
                        continue;
                    }

                    LayoutParams params =
                            new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
                    addView(createGuestView(guest), params);
                }
            }
        });
    }

    public View createGuestView(final Guest guest) {
        View view = View.inflate(getContext(), R.layout.list_item_guest, null);

        View twitterProfileButton = view.findViewById(R.id.twitter_profile_button);
        twitterProfileButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentUtils.openTwitterProfile(getContext(), guest.getName());
            }
        });

        TextView guestNameText = (TextView) view.findViewById(R.id.guest_name_text);
        guestNameText.setText(guest.getName());

        ImageView profileImageView = (ImageView) view.findViewById(R.id.profile_image);
        PicassoHelper.loadAndCircleTransform(getContext(), profileImageView,
                guest.getProfileImageUrl());

        TextView descriptionTextView = (TextView) view.findViewById(R.id.description_text);
        descriptionTextView.setText(guest.getDescription());

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
