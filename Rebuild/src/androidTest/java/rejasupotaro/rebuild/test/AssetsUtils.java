package rejasupotaro.rebuild.test;

import android.app.Instrumentation;
import android.content.res.AssetManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class AssetsUtils {

    public static String read(Instrumentation instrumentation, String fileName) {
        String data = "";

        try {
            BufferedReader bufferedReader = null;
            try {
                AssetManager assetManager = instrumentation.getContext().getAssets();
                InputStream inputStream = assetManager.open(fileName);
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line);
                }

                data = stringBuilder.toString();
            } finally {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return data;
    }
}

