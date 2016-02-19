package com.jojo.googlenewsreader.pojo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class ArticleForSerialization extends Article implements Serializable {

    private int id;
    private String title;
    private String content;
    private String imageUrl;
    private String url;
    private String publisher;
    private String date;
    private int deleted;
    private List<Tag> tagList;
    private Date publisheDate;

    public ArticleForSerialization(Article article) {
        id = article.getId();
        title = article.getTitle();
        content = article.getContent();
        imageUrl = article.getImageUrl();
        url = article.getUrl();
        publisher = article.getPublisher();
        date = article.getDate();
        deleted = article.getDeleted();
        this.tagList = article.getTagList();
        this.publisheDate = article.getPublisheDate();
    }

    @Override
    public int getId() {
        return id;
    }
    @Override
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String getTitle() {
        return title;
    }
    @Override
    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String getContent() {
        return content;
    }
    @Override
    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String getImageUrl() {
        return imageUrl;
    }
    @Override
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public String getUrl() {
        return url;
    }
    @Override
    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String getPublisher() {
        return publisher;
    }
    @Override
    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    @Override
    public String getDate() {
        return date;
    }
    @Override
    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public int getDeleted() {
        return deleted;
    }
    @Override
    public void setDeleted(int deleted) {
        this.deleted = deleted;
    }

    @Override
    public List<Tag> getTagList() {
        return tagList;
    }
    @Override
    public void setTagList(List<Tag> tagList) {
        this.tagList = tagList;
    }

    @Override
    public Date getPublisheDate() {
        return publisheDate;
    }
    @Override
    public void setPublisheDate(Date publisheDate) {
        this.publisheDate = publisheDate;
    }
}
