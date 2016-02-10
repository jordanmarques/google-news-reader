package com.jojo.googlenewsreader.articles;

import com.jojo.googlenewsreader.pojo.Article;

import java.util.List;

public class ArticleList {
    public String[] listTitles;
    public List articles;

    public ArticleList() {
    }

    public ArticleList(String[] listTitles, List articles) {
        this.listTitles = listTitles;
        this.articles = articles;
    }

    public String[] getListTitles() {
        return listTitles;
    }

    public void setListTitles(String[] listTitles) {
        this.listTitles = listTitles;
    }

    public List<Article> getArticles() {
        return articles;
    }

    public void setArticles(List<Article> articles) {
        this.articles = articles;
    }
}
