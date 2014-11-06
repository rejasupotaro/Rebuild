package rejasupotaro.rebuild.dialogs;

import android.content.Context;

import rejasupotaro.rebuild.R;
import rejasupotaro.rebuild.data.models.Episode;
import rejasupotaro.rebuild.events.BusProvider;
import rejasupotaro.rebuild.events.ClearEpisodeCacheEvent;
import rejasupotaro.rebuild.services.EpisodeDownloadService;
import rejasupotaro.rebuild.utils.StringUtils;
import uk.me.lewisdeane.ldialogs.CustomDialog;

public class EpisodeDownloadDialog {
    public static CustomDialog newInstance(final Context context, final Episode episode) {
        if (episode.isDownloaded()) {
            return createClearCacheDialog(context, episode);
        } else {
            if (EpisodeDownloadService.isDownloading(episode)) {
                return createCancelDownload(context, episode);
            } else {
                return createDownloadDialog(context, episode);
            }
        }
    }

    private static CustomDialog createClearCacheDialog(final Context context, final Episode episode) {
        CustomDialog.Builder builder = new CustomDialog.Builder(context, episode.getTitle(), "CLEAR CACHE");
        builder.content(StringUtils.removeHtmlTags(episode.getDescription()));
        CustomDialog dialog = builder.build();
        dialog.setClickListener(new CustomDialog.ClickListener() {
            @Override
            public void onConfirmClick() {
                episode.clearCache();
                episode.save();
                BusProvider.getInstance().post(new ClearEpisodeCacheEvent());
            }

            @Override
            public void onCancelClick() {
                // do nothing
            }
        });
        return dialog;
    }

    private static CustomDialog createCancelDownload(final Context context, final Episode episode) {
        CustomDialog.Builder builder = new CustomDialog.Builder(context, episode.getTitle(), "CANCEL DOWNLOAD");
        builder.content(StringUtils.removeHtmlTags(episode.getDescription()));
        CustomDialog dialog = builder.build();
        dialog.setClickListener(new CustomDialog.ClickListener() {
            @Override
            public void onConfirmClick() {
                EpisodeDownloadService.cancel(context, episode);
            }

            @Override
            public void onCancelClick() {
                // do nothing
            }
        });
        return dialog;
    }

    private static CustomDialog createDownloadDialog(final Context context, final Episode episode) {
        CustomDialog.Builder builder = new CustomDialog.Builder(context, episode.getTitle(), "DOWNLOAD");
        builder.content(StringUtils.removeHtmlTags(episode.getDescription()));
        CustomDialog dialog = builder.build();
        dialog.setClickListener(new CustomDialog.ClickListener() {
            @Override
            public void onConfirmClick() {
                EpisodeDownloadService.startDownload(context, episode);
            }

            @Override
            public void onCancelClick() {
                // do nothing
            }
        });
        return dialog;
    }
}

