package com.example.recyclerview.entity;

/**
 * Entity define the main data of the app
 */
public class News {
    private String title;
    private String summary;
    private String link;

    public News(String title, String summary, String link) {
        this.title = title;
        this.summary = summary;
        this.link = link;
    }

    public String getTitle() {
        return title;
    }

    public String getSummary() {
        return summary;
    }

    public String getLink() {
        return link;
    }

    @Override
    public boolean equals (Object obj) {
        if (obj.getClass() != News.class)
            return false;

        News otherNews = (News) obj;
        return title.equals(otherNews.title) && summary.equals(otherNews.summary)
                && link.equals(otherNews.link);
    }
}
