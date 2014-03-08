package rejasupotaro.rebuild.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

public final class PackageUtils {

    public static String getVersionName(Context context) {
        PackageInfo packageInfo = getPackageInfo(context);
        if (packageInfo == null) {
            return "";
        }

        return packageInfo.versionName;
    }

    public static PackageInfo getPackageInfo(Context context) {
        PackageInfo packageInfo = null;
        try {
            packageInfo = context.getPackageManager()
                    .getPackageInfo(getPackageName(context), PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            // nothing to do
        }
        return packageInfo;
    }

    public static String getPackageName(Context context) {
        return context.getPackageName();
    }

    private PackageUtils() {
    }
}
