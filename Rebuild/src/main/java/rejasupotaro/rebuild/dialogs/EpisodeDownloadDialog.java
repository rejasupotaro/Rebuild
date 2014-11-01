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

public class EpisodeDownloadDialog extends DialogFragment {
    private static final String ARGS_EPISODE = "args_episode";

    private EpisodePlayDialogHelper episodePlayDialogHelper = new EpisodePlayDialogHelper();
    private TextView titleTextView;
    private TextView messageTextView;
    private TextView button;

    public static EpisodeDownloadDialog newInstance(Episode episode) {
        EpisodeDownloadDialog dialog = new EpisodeDownloadDialog();
        Bundle args = new Bundle();
        args.putParcelable(ARGS_EPISODE, episode);
        dialog.setArguments(args);
        return dialog;
    }

    private void findViews(View rootView) {
        titleTextView = (TextView) rootView.findViewById(R.id.title_text);
        messageTextView = (TextView) rootView.findViewById(R.id.message_text);
        button = (TextView) rootView.findViewById(R.id.button);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle args = getArguments();
        final Episode episode = args.getParcelable(ARGS_EPISODE);
        String title = episode.getTitle();
        String description = StringUtils.removeHtmlTags(episode.getDescription());
        boolean isDownloaded = episode.isDownloaded();

        View rootView = View.inflate(getActivity(), R.layout.dialog_episode_download, null);
        findViews(rootView);

        titleTextView.setText(title);
        messageTextView.setText(description);
        if (isDownloaded) {
            setClearCacheEvent(button, episode);
        } else {
            if (EpisodeDownloadService.isDownloading(episode)) {
                setCancelDownloadEvent(button, episode);
            } else {
                setDownloadEvent(button, episode);
            }
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(rootView);

        Dialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(true);

        return dialog;
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

