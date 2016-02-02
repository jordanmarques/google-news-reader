package com.jojo.googlenewsreader.articles;

import org.json.JSONArray;

/**
 * Created by Arnaud Flaesch on 31/01/2016.
 */
public class ListArticles {
    public String[] listTitles;
    public JSONArray articles = new JSONArray();

    public ListArticles() {
        this.listTitles = new String[]{"Chargements des news..."};
    }

    public ListArticles(String[] listTitles, JSONArray articles) {
        this.listTitles = listTitles;
        this.articles = articles;
    }
}
