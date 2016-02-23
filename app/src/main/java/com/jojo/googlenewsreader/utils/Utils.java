package com.jojo.googlenewsreader.utils;

import com.jojo.googlenewsreader.pojo.Article;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class Utils {

    public static void sortByPublishedDate(List<Article> articleList) {
        Collections.sort(articleList, new Comparator<Article>() {
            public int compare(Article o1, Article o2) {
                return o2.getPublisheDate().compareTo(o1.getPublisheDate());
            }
        });
    }

    public static void populatePublishDateField(List<Article> articleList) throws ParseException {
        DateFormat format = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss", Locale.ENGLISH);
        for(Article loopArticle : articleList){
            loopArticle.setDate(loopArticle.getDate().replace(" -0800", ""));
            loopArticle.setPublisheDate(format.parse(loopArticle.getDate()));
        }
    }
}
