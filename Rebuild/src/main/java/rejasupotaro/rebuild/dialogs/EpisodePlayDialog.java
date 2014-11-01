package rejasupotaro.rebuild.dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import rejasupotaro.rebuild.data.models.Episode;
import rejasupotaro.rebuild.events.BusProvider;
import rejasupotaro.rebuild.events.ClearEpisodeCacheEvent;
import rejasupotaro.rebuild.services.EpisodeDownloadService;
import rejasupotaro.rebuild.utils.StringUtils;

public class EpisodePlayDialog {
    public interface OnSelectListener {
        public void playNow(Episode episode);
        public void startStreaming(Episode episode);
    }

    public static AlertDialog newInstance(final Context context, final Episode episode, final OnSelectListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(episode.getTitle());
        builder.setMessage(StringUtils.removeHtmlTags(episode.getDescription()));

        if (episode.isDownloaded()) {
            builder.setPositiveButton("PLAY NOW", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    listener.playNow(episode);
                }
            });
            builder.setNegativeButton("CLEAR CACHE", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    episode.clearCache();
                    episode.save();
                    BusProvider.getInstance().post(new ClearEpisodeCacheEvent());
                }
            });
        } else {
            builder.setPositiveButton("STREAMING", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    listener.startStreaming(episode);
                }
            });
            if (EpisodeDownloadService.isDownloading(episode)) {
                builder.setNegativeButton("CANCEL DOWNLOAD", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        EpisodeDownloadService.cancel(context, episode);
                    }
                });
            } else {
                builder.setNegativeButton("DOWNLOAD", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        EpisodeDownloadService.startDownload(context, episode);
                    }
                });
            }
        }

        builder.setCancelable(true);

        return builder.create();
    }
}
