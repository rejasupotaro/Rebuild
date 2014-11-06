package rejasupotaro.rebuild.dialogs;

import android.content.Context;

import rejasupotaro.rebuild.data.models.Episode;
import rejasupotaro.rebuild.events.BusProvider;
import rejasupotaro.rebuild.events.ClearEpisodeCacheEvent;
import rejasupotaro.rebuild.services.EpisodeDownloadService;
import rejasupotaro.rebuild.utils.StringUtils;
import uk.me.lewisdeane.ldialogs.CustomDialog;

public class EpisodePlayDialog {
    public interface OnSelectListener {
        public void playNow(Episode episode);
        public void startStreaming(Episode episode);
    }

    public static CustomDialog newInstance(final Context context, final Episode episode, final OnSelectListener listener) {
        if (episode.isDownloaded()) {
            return createPlayNowDialog(context, episode, listener);
        } else {
            return createStreamingDialog(context, episode, listener);
        }
    }

    private static CustomDialog createPlayNowDialog(final Context context, final Episode episode, final OnSelectListener listener) {
        CustomDialog.Builder builder = new CustomDialog.Builder(context, episode.getTitle(), "PLAY NOW");
        builder.content(StringUtils.removeHtmlTags(episode.getDescription()));
        builder.negativeText("CANCEL DOWNLOAD");
        CustomDialog dialog = builder.build();
        dialog.setClickListener(new CustomDialog.ClickListener() {
            @Override
            public void onConfirmClick() {
                listener.playNow(episode);
            }

            @Override
            public void onCancelClick() {
                episode.clearCache();
                episode.save();
                BusProvider.getInstance().post(new ClearEpisodeCacheEvent());
            }
        });
        return dialog;
    }

    private static CustomDialog createStreamingDialog(final Context context, final Episode episode, final OnSelectListener listener) {
        CustomDialog.Builder builder = new CustomDialog.Builder(context, episode.getTitle(), "STREAMING");
        builder.content(StringUtils.removeHtmlTags(episode.getDescription()));

        if (EpisodeDownloadService.isDownloading(episode)) {
            builder.negativeText("CANCEL DOWNLOAD");
        } else {
            builder.negativeText("DOWNLOAD");
        }

        CustomDialog dialog = builder.build();
        if (EpisodeDownloadService.isDownloading(episode)) {
            dialog.setClickListener(new CustomDialog.ClickListener() {
                @Override
                public void onConfirmClick() {
                    listener.startStreaming(episode);
                }

                @Override
                public void onCancelClick() {
                    EpisodeDownloadService.cancel(context, episode);
                }
            });
        } else {
            dialog.setClickListener(new CustomDialog.ClickListener() {
                @Override
                public void onConfirmClick() {
                    listener.startStreaming(episode);
                }

                @Override
                public void onCancelClick() {
                    EpisodeDownloadService.startDownload(context, episode);
                }
            });
        }
        return dialog;
    }
}
