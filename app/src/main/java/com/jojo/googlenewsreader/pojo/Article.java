package com.jojo.googlenewsreader.pojo;

import android.text.Html;

import java.io.Serializable;

public class Article implements Serializable {

    private int id;
    private String title;
    private String content;
    private String imageUrl;
    private String url;
    private int deleted;

    public Article() {
    }

    public Article(String title, String content, String imageUrl, String url) {
        this.title = title;
        this.content = content;
        this.imageUrl = imageUrl;
        this.url = url;
    }

    public Article(int id, String title, String content, String imageUrl, String url, int deleted) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.imageUrl = imageUrl;
        this.url = url;
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

    public int getDeleted() {
        return deleted;
    }
    public void setDeleted(int deleted) {
        this.deleted = deleted;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Article article = (Article) o;

        if (id != article.id) return false;
        if (deleted != article.deleted) return false;
        if (title != null ? !title.equals(article.title) : article.title != null) return false;
        if (content != null ? !content.equals(article.content) : article.content != null)
            return false;
        if (imageUrl != null ? !imageUrl.equals(article.imageUrl) : article.imageUrl != null)
            return false;
        return !(url != null ? !url.equals(article.url) : article.url != null);

    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (content != null ? content.hashCode() : 0);
        result = 31 * result + (imageUrl != null ? imageUrl.hashCode() : 0);
        result = 31 * result + (url != null ? url.hashCode() : 0);
        result = 31 * result + deleted;
        return result;
    }
}
