package rejasupotaro.rebuild.media;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import org.apache.http.util.ByteArrayBuffer;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

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

    public static String saveMediaToFile(Context context, InputStream inputStream, Episode episode) {
        BufferedInputStream bufferedInputStream = null;
        try {
            bufferedInputStream = new BufferedInputStream(inputStream);
            ByteArrayBuffer byteArrayBuffer = new ByteArrayBuffer(50);
            int read = 0;
            int bufferSize = 512;
            byte[] buffer = new byte[bufferSize];
            while (true) {
                read = bufferedInputStream.read(buffer);
                if (read == -1) {
                    break;
                }
                byteArrayBuffer.append(buffer, 0, read);
            }

            return saveMediaToFile(context, byteArrayBuffer.toByteArray(), episode);
        } catch (IOException e) {
            Log.e(TAG, "An error occurred while saveing sound", e);
            return null;
        } finally {
            FileUtils.close(bufferedInputStream);
        }
    }

    public static File createExternalStoragePrivateFile(Context context, Episode episode) {
        String fileName = String.valueOf(episode.getEpisodeId()) + ".mp3";
        return new File(context.getExternalFilesDir(null), fileName);
    }

    public static boolean exists(String filePath) {
        if (TextUtils.isEmpty(filePath)) return false;

        File file = new File(filePath);
        return file.exists();
    }
}
