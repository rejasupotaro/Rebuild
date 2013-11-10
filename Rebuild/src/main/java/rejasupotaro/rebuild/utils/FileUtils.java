package rejasupotaro.rebuild.utils;

import java.io.Closeable;
import java.io.IOException;

public final class FileUtils {

    private FileUtils() {}

    public static void close(Closeable resource) {
        if (resource == null) return;

        try {
            resource.close();
        } catch (IOException e) {
            // ignore
        }
    }
}
