package rejasupotaro.rebuild.test;

import org.junit.runners.model.InitializationError;
import org.robolectric.AndroidManifest;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.res.Fs;

import rejasupotaro.rebuild.RebuildApplication;

public class RobolectricGradleTestRunner extends RobolectricTestRunner {

    public RobolectricGradleTestRunner(Class<?> testClass) throws InitializationError {
        super(testClass);
    }

    @Override
    protected AndroidManifest getAppManifest(Config config) {
        String myAppPath = RebuildApplication.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        String filePath = myAppPath + "../../../src/main/AndroidManifest.xml";
        return createAppManifest(Fs.fileFromPath(filePath));
    }
}
