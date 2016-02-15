package com.jojo.googlenewsreader.pojo;

import java.io.Serializable;

public class Article_Tag implements Serializable {

    private long article_id;
    private long tag_id;

    public Article_Tag(long article_id, long tag_id) {
        this.article_id = article_id;
        this.tag_id = tag_id;
    }

    public Article_Tag() {
    }

    public long getArticle_id() {
        return article_id;
    }
    public void setArticle_id(long article_id) {
        this.article_id = article_id;
    }

    public long getTag_id() {
        return tag_id;
    }
    public void setTag_id(long tag_id) {
        this.tag_id = tag_id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Article_Tag that = (Article_Tag) o;

        if (article_id != that.article_id) return false;
        return tag_id == that.tag_id;

    }
    @Override
    public int hashCode() {
        int result = (int) (article_id ^ (article_id >>> 32));
        result = 31 * result + (int) (tag_id ^ (tag_id >>> 32));
        return result;
    }
}
