package rejasupotaro.rebuild.listener;

import android.widget.SeekBar;

import rejasupotaro.rebuild.media.PodcastPlayer;

public class OnPlayerSeekListener implements SeekBar.OnSeekBarChangeListener {
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        if (!PodcastPlayer.getInstance().isPlaying()) {
            return;
        }
        PodcastPlayer.getInstance().seekTo(seekBar.getProgress());
    }
}
