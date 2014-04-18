package rejasupotaro.rebuild.dialogs;

import com.google.inject.Inject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import rejasupotaro.rebuild.R;
import rejasupotaro.rebuild.fragments.EpisodePlayDialogHelper;
import rejasupotaro.rebuild.models.Episode;
import rejasupotaro.rebuild.services.EpisodeDownloadService;
import rejasupotaro.rebuild.utils.StringUtils;
import roboguice.fragment.RoboDialogFragment;

public class EpisodePlayDialog extends RoboDialogFragment {

    private static final String ARGS_EPISODE = "args_episode";

    @Inject
    private EpisodePlayDialogHelper episodePlayDialogHelper;

    private TextView titleTextView;

    private TextView messageTextView;

    private TextView positiveButton;

    private TextView negativeButton;

    public static EpisodePlayDialog newInstance(Episode episode) {
        EpisodePlayDialog dialog = new EpisodePlayDialog();
        Bundle args = new Bundle();
        args.putParcelable(ARGS_EPISODE, episode);
        dialog.setArguments(args);
        return dialog;
    }

    private void findViews(View rootView) {
        titleTextView = (TextView) rootView.findViewById(R.id.title_text);
        messageTextView = (TextView) rootView.findViewById(R.id.message_text);
        positiveButton = (TextView) rootView.findViewById(R.id.positive_button);
        negativeButton = (TextView) rootView.findViewById(R.id.negative_button);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle args = getArguments();
        final Episode episode = args.getParcelable(ARGS_EPISODE);
        String title = episode.getTitle();
        String description = StringUtils.removeHtmlTags(episode.getDescription());
        boolean isDownloaded = episode.isDownloaded();

        View rootView = View.inflate(getActivity(), R.layout.dialog_episode_play, null);
        findViews(rootView);

        titleTextView.setText(title);
        messageTextView.setText(description);
        if (isDownloaded) {
            positiveButton.setText("PLAY NOW");
            positiveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    episodePlayDialogHelper.playNow(episode);
                    dismiss();
                }
            });
            negativeButton.setText("CLEAR CACHE");
            negativeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    episodePlayDialogHelper.clearCache(episode);
                    dismiss();
                }
            });
        } else {
            positiveButton.setText("STREAM");
            positiveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    episodePlayDialogHelper.startStreaming(episode);
                    dismiss();
                }
            });
            if (EpisodeDownloadService.isDownloading(episode)) {
                negativeButton.setText("CANCEL DOWNLOAD");
                negativeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        EpisodeDownloadService.cancel(getActivity(), episode);
                        dismiss();
                    }
                });
            } else {
                negativeButton.setText("DOWNLOAD");
                negativeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        episodePlayDialogHelper.startDownload(episode);
                        dismiss();
                    }
                });
            }
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(rootView);

        Dialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(true);

        return dialog;
    }
}
