package rejasupotaro.rebuild.data.models;

public class AboutItem {
    public static class AboutItemHeader extends AboutItem {
        private String title;

        public String getTitle() {
            return title;
        }

        public AboutItemHeader(String title) {
            this.title = title;
        }
    }

    public static class AboutItemContent extends AboutItem {
        private String imageUrl;
        private String text;
        private String link;

        public String getImageUrl() {
            return imageUrl;
        }

        public String getText() {
            return text;
        }

        public String getLink() {
            return link;
        }

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

