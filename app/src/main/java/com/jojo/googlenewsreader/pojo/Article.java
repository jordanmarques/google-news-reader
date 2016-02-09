package com.jojo.googlenewsreader.pojo;

import android.text.Html;

import java.io.Serializable;

public class Article implements Serializable {

    private String title;
    private String content;
    private String imageUrl;
    private String url;

    public Article() {
    }

    public Article(String title, String content, String imageUrl, String url) {
        this.title = title;
        this.content = content;
        this.imageUrl = imageUrl;
        this.url = url;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = Html.fromHtml(title).toString();
    }

    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = Html.fromHtml(content).toString();
    }

    public String getImageUrl() {
        return imageUrl;
    }
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
}
