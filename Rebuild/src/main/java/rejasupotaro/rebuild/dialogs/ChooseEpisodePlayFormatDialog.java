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

    private static final String ARGS_TITLE = "args_title";

    private static final String ARGS_DESCRIPTION = "args_description";

    private static final String ARGS_IS_DONWLOADED = "args_is_downloaded";

    public static ChooseEpisodePlayFormatDialog newInstance(Episode episode, boolean isDownloaded) {
        ChooseEpisodePlayFormatDialog dialog = new ChooseEpisodePlayFormatDialog();
        Bundle args = new Bundle();
        args.putString(ARGS_TITLE, episode.getTitle());
        args.putString(ARGS_DESCRIPTION, StringUtils.removeHtmlTags(episode.getDescription()));
        args.putBoolean(ARGS_IS_DONWLOADED, isDownloaded);
        dialog.setArguments(args);
        return dialog;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle args = getArguments();
        String title = args.getString(ARGS_TITLE);
        String description = args.getString(ARGS_DESCRIPTION);
        Log.e("debugging", description);
        boolean isDownloaded = args.getBoolean(ARGS_IS_DONWLOADED);

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
