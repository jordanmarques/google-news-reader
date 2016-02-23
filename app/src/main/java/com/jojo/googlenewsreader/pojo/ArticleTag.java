package com.jojo.googlenewsreader.pojo;

import java.io.Serializable;

public class ArticleTag implements Serializable {

    private long articleId;
    private long tagId;

    public ArticleTag(long article_id, long tag_id) {
        this.articleId = article_id;
        this.tagId = tag_id;
    }

    public ArticleTag() {
    }

    public long getArticleId() {
        return articleId;
    }
    public void setArticleId(long articleId) {
        this.articleId = articleId;
    }

    public long getTagId() {
        return tagId;
    }
    public void setTagId(long tagId) {
        this.tagId = tagId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ArticleTag that = (ArticleTag) o;

        if (articleId != that.articleId) return false;
        return tagId == that.tagId;

    }
    @Override
    public int hashCode() {
        int result = (int) (articleId ^ (articleId >>> 32));
        result = 31 * result + (int) (tagId ^ (tagId >>> 32));
        return result;
    }
}
