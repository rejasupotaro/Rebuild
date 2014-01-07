package rejasupotaro.rebuild.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import rejasupotaro.rebuild.R;
import roboguice.activity.RoboFragmentActivity;

public class ReportActivity extends RoboFragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showReportDialog();
    }

    public void showReportDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.report_dialog_title);
        builder.create();
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                System.exit(0);
            }
        });
        builder.setPositiveButton(R.string.report_dialog_positive_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                System.exit(0);
            }
        });
        builder.setMessage(R.string.report_dialog_body);
        builder.show();
    }
}
