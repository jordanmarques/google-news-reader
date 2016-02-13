package com.jojo.googlenewsreader.articles;

import com.jojo.googlenewsreader.pojo.Article;

import java.util.List;

public class ArticleList {
    public List articles;

    public ArticleList() {
    }

    public ArticleList(List articles) {
        this.articles = articles;
    }

    public List<Article> getArticles() {
        return articles;
    }
    public void setArticles(List<Article> articles) {
        this.articles = articles;
    }
}
