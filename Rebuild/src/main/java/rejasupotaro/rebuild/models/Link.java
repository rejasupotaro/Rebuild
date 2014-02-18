package rejasupotaro.rebuild.models;

import org.apache.commons.lang3.StringEscapeUtils;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

public class Link implements Parcelable {

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

    public Link(Parcel in) {
        mTitle = in.readString();
        mUrl = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Link> CREATOR = new Creator<Link>() {
        public Link createFromParcel(Parcel in) {
            return new Link(in);
        }

        public Link[] newArray(int size) {
            return new Link[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mTitle);
        dest.writeString(mUrl);
    }

    public static class Parser {

        public static List<Link> toLinkList(String source) {
            List<Link> linkList = new ArrayList<Link>();
            if (TextUtils.isEmpty(source)) {
                return linkList;
            }

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

