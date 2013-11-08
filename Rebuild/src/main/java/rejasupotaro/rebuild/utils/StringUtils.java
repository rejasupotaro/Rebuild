package rejasupotaro.rebuild.utils;

import android.text.TextUtils;

public final class StringUtils {

    private StringUtils() {
    }

    public static String removeNewLines(String source) {
        return source.replaceAll("\n", "");
    }

    public static String buildTwitterLinkText(String source) {
        if (TextUtils.isEmpty(source)) return "";
        if (source.indexOf("@") < 0) return source;

        String[] splitedSources = source.split("@");

        String twitterLinkText = "";
        twitterLinkText += splitedSources[0];
        for (int i = 1; i < splitedSources.length; i++) {
            String twitterName = getTwitterName(splitedSources[i]);
            if (TextUtils.isEmpty(twitterName)) {
                twitterLinkText += splitedSources[i];
            } else {
                twitterLinkText += appendHref("@" + twitterName, twitterName);
                twitterLinkText += splitedSources[i].substring(twitterName.length(), splitedSources[i].length());
            }
        }
        return twitterLinkText;
    }

    private static String appendHref(String text, String url) {
        return "<a href=\"https://twitter.com/" + url + "\">" + text + "</a>";
    }

    private static String getTwitterName(String source) {
        int endIndex = source.indexOf(")");
        if (endIndex < 0) return "";
        return source.substring(0, endIndex);
    }
}
