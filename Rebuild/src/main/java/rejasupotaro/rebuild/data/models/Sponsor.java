package rejasupotaro.rebuild.data.models;

import org.apache.commons.lang3.StringEscapeUtils;

import android.text.TextUtils;

public class Sponsor {
    private static final Sponsor NULL_SPONSOR = new Sponsor("", "");

    private String text;
    private String url;

    public String getText() {
        return text;
    }

    public String getUrl() {
        return url;
    }

    public Sponsor(String text, String url) {
        this.text = text;
        this.url = url;
    }

    public boolean isNull() {
        return (TextUtils.isEmpty(text) && TextUtils.isEmpty(url));
    }

    public static Sponsor fromSource(String source) {
        return Parser.toSponsor(source);

    }

    public static class Parser {

        public static Sponsor toSponsor(String source) {
            if (TextUtils.isEmpty(source)) {
                return NULL_SPONSOR;
            }

            try {
                String sponsorText = substringSponsor(source);
                String text = getText(sponsorText);
                String url = getUrl(sponsorText);
                return new Sponsor(text, url);
            } catch (Exception e) {
                // considering case that description format is modified
                return NULL_SPONSOR;
            }
        }

        public static String substringSponsor(String source) {
            if (TextUtils.isEmpty(source)) {
                return "";
            }

            String pattern = "<p>スポンサー: ";
            int startIndex = source.indexOf(pattern);
            if (startIndex < 0) {
                return "";
            }

            source = source.substring(startIndex, source.length());
            startIndex = source.indexOf("</a></p>");
            if (startIndex < 0) {
                return "";
            }

            return source.substring(pattern.length(), startIndex);
        }

        public static String getText(String source) {
            if (TextUtils.isEmpty(source)) {
                return "";
            }

            int startIndex = source.indexOf(">");
            if (startIndex < 0) {
                return "";
            }

            String text = source.substring(startIndex + 1, source.length());
            return StringEscapeUtils.unescapeXml(text);
        }

        public static String getUrl(String source) {
            if (TextUtils.isEmpty(source)) {
                return "";
            }

            int startIndex = source.indexOf("href=\"");
            int endIndex = source.indexOf(">");
            if (startIndex < 0 || endIndex < 0) {
                return "";
            }

            return source.substring(startIndex + 6, endIndex - 1);
        }
    }
}
