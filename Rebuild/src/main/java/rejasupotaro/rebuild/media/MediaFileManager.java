package rejasupotaro.rebuild.media;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import rejasupotaro.rebuild.data.models.Episode;
import rejasupotaro.rebuild.utils.FileUtils;

public class MediaFileManager {
    private static final String TAG = MediaFileManager.class.getSimpleName();
    private static final int BUFFER_SIZE = 23 * 1024;

    public static String saveMediaToFile(Context context, InputStream inputStream,
            Episode episode) {
        BufferedInputStream bufferedInputStream = null;
        FileOutputStream fileOutputStream = null;
        try {
            File destFile = createExternalStoragePrivateFile(context, episode);

            bufferedInputStream
                    = new BufferedInputStream(inputStream, BUFFER_SIZE);
            fileOutputStream = new FileOutputStream(destFile);

            int actual;
            byte[] buffer = new byte[BUFFER_SIZE];
            while ((actual = bufferedInputStream.read(buffer, 0, BUFFER_SIZE)) > 0) {
                fileOutputStream.write(buffer, 0, actual);
            }

            return destFile.getPath();
        } catch (IOException e) {
            Log.e(TAG, "An error occurred while saving media", e);
            return null;
        } finally {
            FileUtils.close(fileOutputStream);
            FileUtils.close(bufferedInputStream);
        }
    }

    public static File createExternalStoragePrivateFile(Context context, Episode episode) {
        String fileName = String.valueOf(episode.getEpisodeId()) + ".mp3";
        return new File(context.getExternalFilesDir(null), fileName);
    }

    public static boolean exists(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return false;
        }

        File file = new File(filePath);
        return file.exists();
    }
}
