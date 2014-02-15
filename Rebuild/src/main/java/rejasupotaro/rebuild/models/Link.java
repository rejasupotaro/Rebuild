package rejasupotaro.rebuild.models;

import org.apache.commons.lang3.StringEscapeUtils;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

public class Link {

    private String mTitle;

    private String mUrl;

    public String getTitle() {
        return mTitle;
    }

    public String getUrl() {
        return mUrl;
    }

    public Link(String title, String url) {
        mTitle = title;
        mUrl = url;
    }

    public static class Parser {

        public static List<Link> toLinkList(String source) {
            List<Link> linkList = new ArrayList<Link>();
            String[] lines = substringDescription(source).split("<li>");
            for (String line : lines) {
                if (!line.startsWith("<a href=")) continue;
                Link link = new Link(getText(line), getHref(line));
                linkList.add(link);
            }
            return linkList;
        }

        public static String substringDescription(String source) {
            if (TextUtils.isEmpty(source)) return "";
            int startIndex = source.indexOf("<h3>");
            if (startIndex < 0) return "";
            return source.substring(startIndex, source.length());
        }

        public static String getHref(String source) {
            if (TextUtils.isEmpty(source)) return "";
            int startIndex = source.indexOf("href=\"");
            int endIndex = source.indexOf(">");
            if (startIndex < 0 || endIndex < 0) return "";
            return source.substring(startIndex + 6, endIndex - 1);
        }

        public static String getText(String source) {
            if (TextUtils.isEmpty(source)) return "";
            int startIndex = source.indexOf(">");
            int endIndex = source.indexOf("</");
            if (startIndex < 0 || endIndex < 0) return "";
            String text = source.substring(startIndex + 1, endIndex);
            return StringEscapeUtils.unescapeXml(text);
        }
    }
}

