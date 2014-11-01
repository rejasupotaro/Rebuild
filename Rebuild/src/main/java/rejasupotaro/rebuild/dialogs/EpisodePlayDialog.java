package rejasupotaro.rebuild.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import rejasupotaro.rebuild.R;
import rejasupotaro.rebuild.fragments.EpisodePlayDialogHelper;
import rejasupotaro.rebuild.data.models.Episode;
import rejasupotaro.rebuild.services.EpisodeDownloadService;
import rejasupotaro.rebuild.utils.StringUtils;

public class EpisodePlayDialog extends DialogFragment {
    private static final String ARGS_EPISODE = "args_episode";

    private EpisodePlayDialogHelper episodePlayDialogHelper = new EpisodePlayDialogHelper();
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
            setPlayNowEvent(positiveButton, episode);
            setClearCacheEvent(negativeButton, episode);
        } else {
            setStreamEvent(positiveButton, episode);
            if (EpisodeDownloadService.isDownloading(episode)) {
                setCancelDownloadEvent(negativeButton, episode);
            } else {
                setDownloadEvent(negativeButton, episode);
            }
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(rootView);

        Dialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(true);

        return dialog;
    }

    private void setPlayNowEvent(TextView view, final Episode episode) {
        view.setText("PLAY NOW");
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                episodePlayDialogHelper.playNow(episode);
                dismiss();
            }
        });
    }

    private void setClearCacheEvent(TextView view, final Episode episode) {
        view.setText("CLEAR CACHE");
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                episodePlayDialogHelper.clearCache(episode);
                dismiss();
            }
        });
    }

    private void setStreamEvent(TextView view, final Episode episode) {
        view.setText("STREAM");
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                episodePlayDialogHelper.startStreaming(episode);
                dismiss();
            }
        });
    }

    private void setCancelDownloadEvent(TextView view, final Episode episode) {
        view.setText("CANCEL DOWNLOAD");
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EpisodeDownloadService.cancel(getActivity(), episode);
                dismiss();
            }
        });
    }

    private void setDownloadEvent(TextView view, final Episode episode) {
        view.setText("DOWNLOAD");
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                episodePlayDialogHelper.startDownload(getActivity(), episode);
                dismiss();
            }
        });
    }
}
