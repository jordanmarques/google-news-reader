package com.jojo.googlenewsreader.articles;

import com.jojo.googlenewsreader.pojo.Article;

import org.json.JSONArray;

import java.util.List;

public class ListArticles {
    public String[] listTitles;
    public List articles;

    public ListArticles() {
        this.listTitles = new String[]{"Chargements des news..."};
    }

    public ListArticles(String[] listTitles, List articles) {
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
