package com.jojo.googlenewsreader.dataBase.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jojo.googlenewsreader.dataBase.AppDataBase;
import com.jojo.googlenewsreader.dataBase.AppDatabaseContract.AppDatabaseEntry;
import com.jojo.googlenewsreader.pojo.Article;

import java.util.ArrayList;
import java.util.List;

public class ArticleDAO {

    private AppDataBase db;
    private SQLiteDatabase dbInstance;

    private final String[] PROJECTION_ARTICLE_TABLE = {
            AppDatabaseEntry.DATABASE_COLUMN_ID,
            AppDatabaseEntry.DATABASE_COLUMN_TITLE,
            AppDatabaseEntry.DATABASE_COLUMN_CONTENT,
            AppDatabaseEntry.DATABASE_COLUMN_IMAGE_URL,
            AppDatabaseEntry.DATABASE_COLUMN_URL,
            AppDatabaseEntry.DATABASE_COLUMN_PUBLISHER,
            AppDatabaseEntry.DATABASE_COLUMN_DATE,
            AppDatabaseEntry.DATABASE_COLUMN_DELETED};

    public ArticleDAO(Context context) {
        db = new AppDataBase(context);
        dbInstance = db.getWritableDatabase();
    }

    public long insertArticle(Article article){
        ContentValues values = new ContentValues();

        values.put(AppDatabaseEntry.DATABASE_COLUMN_TITLE, article.getTitle());
        values.put(AppDatabaseEntry.DATABASE_COLUMN_CONTENT, article.getContent());
        values.put(AppDatabaseEntry.DATABASE_COLUMN_IMAGE_URL, article.getImageUrl());
        values.put(AppDatabaseEntry.DATABASE_COLUMN_URL, article.getUrl());
        values.put(AppDatabaseEntry.DATABASE_COLUMN_PUBLISHER, article.getPublisher());
        values.put(AppDatabaseEntry.DATABASE_COLUMN_DATE, article.getDate());
        values.put(AppDatabaseEntry.DATABASE_COLUMN_DELETED, 0);

        return dbInstance.insert(AppDatabaseEntry.DATABASE_ARTICLE_TABLE, null, values);
    }

    public List<Article> findAllArticles(){
        List<Article> articles = new ArrayList<>();
        Cursor cursor = dbInstance.query(AppDatabaseEntry.DATABASE_ARTICLE_TABLE,
                PROJECTION_ARTICLE_TABLE,
                null,
                null,
                null,
                null,
                null);

        if(cursor.moveToFirst()){
            do{
                articles.add(new Article(cursor.getInt(cursor.getColumnIndex(AppDatabaseEntry.DATABASE_COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndex(AppDatabaseEntry.DATABASE_COLUMN_TITLE)),
                        cursor.getString(cursor.getColumnIndex(AppDatabaseEntry.DATABASE_COLUMN_CONTENT)),
                        cursor.getString(cursor.getColumnIndex(AppDatabaseEntry.DATABASE_COLUMN_IMAGE_URL)),
                        cursor.getString(cursor.getColumnIndex(AppDatabaseEntry.DATABASE_COLUMN_URL)),
                        cursor.getString(cursor.getColumnIndex(AppDatabaseEntry.DATABASE_COLUMN_PUBLISHER)),
                        cursor.getString(cursor.getColumnIndex(AppDatabaseEntry.DATABASE_COLUMN_DATE)),
                        cursor.getInt(cursor.getColumnIndex(AppDatabaseEntry.DATABASE_COLUMN_DELETED))));
            }while(cursor.moveToNext());
        }
        cursor.close();
        return articles;
    }

    public Boolean isArticleInDB(Article article){

        String[] title = {String.valueOf(article.getTitle())};

        Cursor cursor = dbInstance.query(
                AppDatabaseEntry.DATABASE_ARTICLE_TABLE,
                PROJECTION_ARTICLE_TABLE,
                AppDatabaseEntry.DATABASE_COLUMN_TITLE + "=?",
                title,
                null,
                null,
                null);

        return cursor.moveToFirst();
    }

    public List<Article> findByTitle(String query) {
        List<Article> articles = new ArrayList<>();
        query = "%" + query + "%";
        String[] title = {query};

        Cursor cursor = dbInstance.rawQuery( "SELECT " + AppDatabaseEntry.DATABASE_COLUMN_ID + "," +
                        AppDatabaseEntry.DATABASE_COLUMN_TITLE + "," +
                        AppDatabaseEntry.DATABASE_COLUMN_CONTENT + "," +
                        AppDatabaseEntry.DATABASE_COLUMN_IMAGE_URL + "," +
                        AppDatabaseEntry.DATABASE_COLUMN_URL + "," +
                        AppDatabaseEntry.DATABASE_COLUMN_PUBLISHER +","+
                        AppDatabaseEntry.DATABASE_COLUMN_DATE + "," +
                        AppDatabaseEntry.DATABASE_COLUMN_DELETED +
                        " FROM " + AppDatabaseEntry.DATABASE_ARTICLE_TABLE +
                        " WHERE " + AppDatabaseEntry.DATABASE_COLUMN_TITLE + " LIKE ?", title);

        if(cursor.moveToFirst()){
            do{
                articles.add(new Article(cursor.getInt(cursor.getColumnIndex(AppDatabaseEntry.DATABASE_COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndex(AppDatabaseEntry.DATABASE_COLUMN_TITLE)),
                        cursor.getString(cursor.getColumnIndex(AppDatabaseEntry.DATABASE_COLUMN_CONTENT)),
                        cursor.getString(cursor.getColumnIndex(AppDatabaseEntry.DATABASE_COLUMN_IMAGE_URL)),
                        cursor.getString(cursor.getColumnIndex(AppDatabaseEntry.DATABASE_COLUMN_URL)),
                        cursor.getString(cursor.getColumnIndex(AppDatabaseEntry.DATABASE_COLUMN_PUBLISHER)),
                        cursor.getString(cursor.getColumnIndex(AppDatabaseEntry.DATABASE_COLUMN_DATE)),
                        cursor.getInt(cursor.getColumnIndex(AppDatabaseEntry.DATABASE_COLUMN_DELETED))));
            }while(cursor.moveToNext());
        }
        cursor.close();
        return articles;
    }
}
