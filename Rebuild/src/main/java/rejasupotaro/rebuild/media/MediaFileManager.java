package rejasupotaro.rebuild.media;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import rejasupotaro.rebuild.models.Episode;
import rejasupotaro.rebuild.utils.FileUtils;

public class MediaFileManager {

    private static final String TAG = MediaFileManager.class.getSimpleName();

    public static String saveMediaToFile(Context context, byte[] bytes, Episode episode) {
        String mediaLocalPath = null;

        FileOutputStream outputStream = null;
        try {
            File outputFile = createExternalStoragePrivateFile(context, episode);
            outputStream = new FileOutputStream(outputFile);
            outputStream.write(bytes);

            mediaLocalPath = outputFile.getPath();
        } catch (FileNotFoundException e) {
            Log.e(TAG, "An error occurred while saveing sound", e);
        } catch (IOException e) {
            Log.e(TAG, "An error occurred while saveing sound", e);
        } finally {
            if (outputStream != null) {
                FileUtils.close(outputStream);
            }
        }

        return mediaLocalPath;
    }

    public static File createExternalStoragePrivateFile(Context context, Episode episode) {
        String fileName = String.valueOf(episode.getEpisodeId()) + ".mp3";
        return new File(context.getExternalFilesDir(null), fileName);
    }
}
