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
import rejasupotaro.rebuild.utils.IntentUtils;
import rejasupotaro.rebuild.utils.PicassoHelper;

public class GuestListView extends LinearLayout {

    private static final int REQUEST_GUEST_LIST = 1;

    private OnContextExecutor onContextExecutor = new OnContextExecutor();

    private TextView nameTextView;

    public GuestListView(Context context) {
        super(context);
    }

    public GuestListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setup(List<String> guestNameList) {
        setOrientation(VERTICAL);
        guestNameList.add(0, "miyagawa");
        requestGuestList(guestNameList);
    }

    public void setupGuestList(final List<Guest> guestList) {
        if (guestList == null || guestList.isEmpty()) {
            return;
        }

        onContextExecutor.execute(getContext(), new Runnable() {
            @Override
            public void run() {
                SectionHeaderView sectionHeaderView = new SectionHeaderView(getContext());
                sectionHeaderView.setText("Casts");
                addView(sectionHeaderView);

                for (Guest guest : guestList) {
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
        PicassoHelper.load(getContext(), profileImageView, guest.getProfileImageUrl());

        TextView tweetsCountText = (TextView) view.findViewById(R.id.tweets_count_text);
        tweetsCountText.setText(guest.getTweetsCount() + " tweets");

        TextView friendsCountText = (TextView) view.findViewById(R.id.friends_count_text);
        friendsCountText.setText("following " + guest.getFriendsCount());

        TextView followersCountText = (TextView) view.findViewById(R.id.followers_count_text);
        followersCountText.setText(guest.getFollowersCount() + " followers");

        TextView descriptionTextView = (TextView) view.findViewById(R.id.description_text);
        descriptionTextView.setText(guest.getDescription());

        TextView urlTextView = (TextView) view.findViewById(R.id.url_text);
        urlTextView.setText(guest.getUrl());

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
