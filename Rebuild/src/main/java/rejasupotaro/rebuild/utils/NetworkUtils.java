package rejasupotaro.rebuild.utils;

import org.apache.http.protocol.HTTP;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.telephony.TelephonyManager;

import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public final class NetworkUtils {

    public static String getUserAgent(Context context) {
        String packageName = context.getPackageName();
        int appVersion = getAppVersion(context, packageName);
        int osVersion = Build.VERSION.SDK_INT;
        String modelName = Build.MODEL;
        String carrier = getCarrier(context);

        StringBuilder sb = new StringBuilder();
        sb.append(packageName).append("/").append(appVersion).append("; ");
        sb.append("Android/").append(osVersion).append("; ");
        sb.append(modelName).append("; ");
        sb.append(carrier).append(";");
        return sb.toString();
    }

    private static int getAppVersion(Context context, String packageName) {
        try {
            PackageInfo packageInfo =
                    context.getPackageManager().getPackageInfo(packageName,
                            PackageManager.GET_META_DATA);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            throw new IllegalStateException(
                    "An error occurred while PackageManager#getPackageInfo");
        }
    }

    public static String getCarrier(Context context) {
        TelephonyManager telephonyManager =
                (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (telephonyManager == null) return "";

        String carrier = telephonyManager.getNetworkOperatorName();

        return optimizeCarrierName(carrier);
    }

    public static String optimizeCarrierName(String carrier) {
        if (carrier == null) return "";

        if (carrier.equals("ソフトバンクモバイル")) carrier = "SOFTBANK";

        try {
            return URLEncoder.encode(carrier, HTTP.UTF_8).toUpperCase();
        } catch (UnsupportedEncodingException e) {
            return "";
        }
    }

    public static void setUserAgent(Context context, HttpURLConnection urlConnection) {
        urlConnection.setRequestProperty(HTTP.USER_AGENT, getUserAgent(context));
    }

    public static Map<String, String> createUserAgentHeader(Context context) {
        Map<String, String> headers = new HashMap<String, String>();;
        headers.put(HTTP.USER_AGENT, getUserAgent(context));
        return headers;
    }

    private NetworkUtils() {}
}
