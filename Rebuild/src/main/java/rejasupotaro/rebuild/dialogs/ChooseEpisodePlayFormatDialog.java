package rejasupotaro.rebuild.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;

import rejasupotaro.rebuild.models.Episode;
import rejasupotaro.rebuild.utils.StringUtils;
import roboguice.fragment.RoboDialogFragment;

public class ChooseEpisodePlayFormatDialog extends RoboDialogFragment {

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
        Episode episode = args.getParcelable(ARGS_EPISODE);
        String title = episode.getTitle();
        String description = StringUtils.removeHtmlTags(episode.getDescription());
        boolean isDownloaded = episode.isDownloaded();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(title)
                .setMessage(description)
                .setPositiveButton("STREAM", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                })
                .setNegativeButton("DOWNLOAD", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });

        Dialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(true);

        return dialog;
    }
}
