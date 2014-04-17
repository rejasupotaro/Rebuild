package rejasupotaro.rebuild.dialogs;

import com.google.inject.Inject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import rejasupotaro.rebuild.fragments.EpisodePlayDialogHelper;
import rejasupotaro.rebuild.models.Episode;
import rejasupotaro.rebuild.utils.StringUtils;
import roboguice.fragment.RoboDialogFragment;

public class ChooseEpisodePlayFormatDialog extends RoboDialogFragment {

    @Inject
    private EpisodePlayDialogHelper episodePlayDialogHelper;

    private static final String ARGS_EPISODE = "args_episode";

    public static ChooseEpisodePlayFormatDialog newInstance(Episode episode) {
        ChooseEpisodePlayFormatDialog dialog = new ChooseEpisodePlayFormatDialog();
        Bundle args = new Bundle();
        args.putParcelable(ARGS_EPISODE, episode);
        dialog.setArguments(args);
        return dialog;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle args = getArguments();
        final Episode episode = args.getParcelable(ARGS_EPISODE);
        String title = episode.getTitle();
        String description = StringUtils.removeHtmlTags(episode.getDescription());
        boolean isDownloaded = episode.isDownloaded();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(title);
        builder.setMessage(description);
        if (isDownloaded) {
            builder.setPositiveButton("PLAY NOW", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    episodePlayDialogHelper.playNow(episode);
                }
            });
            builder.setNegativeButton("CLEAR CACHE", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    episodePlayDialogHelper.clearCache(episode);
                }
            });
        } else {
            builder.setPositiveButton("STREAM", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    episodePlayDialogHelper.startStreaming(episode);
                }
            });
            builder.setNegativeButton("DOWNLOAD", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    episodePlayDialogHelper.startDownload(episode);
                }
            });
        }

        Dialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(true);

        return dialog;
    }
}
