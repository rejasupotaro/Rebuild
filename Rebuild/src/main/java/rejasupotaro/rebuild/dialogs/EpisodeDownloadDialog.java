package rejasupotaro.rebuild.dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import rejasupotaro.rebuild.data.models.Episode;
import rejasupotaro.rebuild.events.BusProvider;
import rejasupotaro.rebuild.events.ClearEpisodeCacheEvent;
import rejasupotaro.rebuild.services.EpisodeDownloadService;
import rejasupotaro.rebuild.utils.StringUtils;

public class EpisodeDownloadDialog {
    public static AlertDialog newInstance(final Context context, final Episode episode) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(episode.getTitle());
        builder.setMessage(StringUtils.removeHtmlTags(episode.getDescription()));

        if (episode.isDownloaded()) {
            builder.setPositiveButton("CLEAR CACHE", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    episode.clearCache();
                    episode.save();
                    BusProvider.getInstance().post(new ClearEpisodeCacheEvent());
                }
            });
        } else {
            if (EpisodeDownloadService.isDownloading(episode)) {
                builder.setPositiveButton("CANCEL DOWNLOAD", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        EpisodeDownloadService.cancel(context, episode);
                    }
                });
            } else {
                builder.setPositiveButton("DOWNLOAD", new DialogInterface.OnClickListener() {
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

