package com.jojo.googlenewsreader.pojo;

import android.text.Html;

import java.io.Serializable;

public class Article implements Serializable {

    private int id;
    private String title;
    private String content;
    private String imageUrl;
    private String url;
    private String publisher;
    private String date;
    private int deleted;

    public Article() {
    }

    public Article(String title, String content, String imageUrl, String url, String publisher, String date) {
        this.title = title;
        this.content = content;
        this.imageUrl = imageUrl;
        this.url = url;
        this.publisher = publisher;
        this.date = date;
    }

    public Article(int id, String title, String content, String imageUrl, String url, String publisher, String date, int deleted) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.imageUrl = imageUrl;
        this.url = url;
        this.publisher = publisher;
        this.date = date;
        this.deleted = deleted;
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

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getPublisher() {
        return publisher;
    }
    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }

    public int getDeleted() {
        return deleted;
    }
    public void setDeleted(int deleted) {
        this.deleted = deleted;
    }
}
