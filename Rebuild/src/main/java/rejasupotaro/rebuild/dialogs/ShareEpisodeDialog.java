package rejasupotaro.rebuild.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import rejasupotaro.rebuild.R;
import rejasupotaro.rebuild.utils.IntentUtils;

public class ShareEpisodeDialog extends DialogFragment {
    private static final String ARGS_SHARE_TEXT = "args_share_text";

    public static ShareEpisodeDialog newInstance(String share_text) {
        ShareEpisodeDialog dialog = new ShareEpisodeDialog();
        Bundle args = new Bundle();
        args.putString(ARGS_SHARE_TEXT, share_text);
        dialog.setArguments(args);
        return dialog;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstnceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        Bundle args = getArguments();

        View root = View.inflate(getActivity(), R.layout.dialog_share_episode, null);
        setupViews(root, args);
        builder.setView(root);

        Dialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(true);

        return dialog;
    }

    private void setupViews(View root, Bundle args) {
        final EditText shareText = (EditText) root.findViewById(R.id.share_text);
        shareText.setText(args.getString(ARGS_SHARE_TEXT));
        View negativeButton = root.findViewById(R.id.negative_button);
        negativeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        View positiveButton = root.findViewById(R.id.positive_button);
        positiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentUtils.sendPostIntent(getActivity(), shareText.getText().toString());
            }
        });
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }
}
