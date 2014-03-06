package rejasupotaro.rebuild.fragments;

import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import rejasupotaro.rebuild.R;
import roboguice.fragment.RoboFragment;
import roboguice.inject.InjectView;

public class EpisodeTranscriptFragment extends RoboFragment {

    @InjectView(R.id.transcript_text)
    private TextView transcriptTextView;

    public static EpisodeTranscriptFragment newInstance() {
        return new EpisodeTranscriptFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_episode_transcript, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        transcriptTextView.setText(Html.fromHtml(
                "<b>naan:</b> 毎週日曜朝ってことになったの。<br>"
                + "<b>miyagawa:</b> (笑)<br>"
                + "<b>naan:</b> そういうわけではないの？<br>"
                + "<b>miyagawa:</b> そういうわけでは…スケジュールがある程度決まってたほうが聞きやすいかもね。<br>"
                + "<b>naan:</b> ま、確かにね。日曜朝のパソコン番組。<br>"
                + "<b>miyagawa:</b> パソコン番組。それまったくわかんないと思うけど。<br>"
                + "<b>naan:</b> (笑)パソコンサンデーね。<br>"
                + "<b>miyagawa:</b> (笑) 僕ですら分かんないしさ。<br>"
                + "<b>naan:</b> (笑)そっか。<br>"));
    }
}
