package rejasupotaro.rebuild.models;

import lombok.Data;

public class AboutItem {

    @Data
    public static class AboutItemHeader extends AboutItem {

        private String title;

        public AboutItemHeader(String title) {
            this.title = title;
        }
    }

    @Data
    public static class AboutItemContent extends AboutItem {

        private String imageUrl;

        private String text;

        private String link;

        public AboutItemContent(String imageUrl, String text, String link) {
            this.text = text;
            this.imageUrl = imageUrl;
            this.link = link;
        }

        public AboutItemContent(Developer developer) {
            this(developer.getImageUrl(), developer.getName(), developer.getLink());
        }
    }
}

