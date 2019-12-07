package com.ly.blogsearch.domain;

public class Article {
    private String id;

    private String title;

    private String image;

    private String summary;

    private Integer viewCount;

    public Article() {
    }

    public Article(String id, String title, String image, String summary, Integer viewCount) {
        this.id = id;
        this.title = title;
        this.image = image;
        this.summary = summary;
        this.viewCount = viewCount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public Integer getViewCount() {
        return viewCount;
    }

    public void setViewCount(Integer viewCount) {
        this.viewCount = viewCount;
    }
}
