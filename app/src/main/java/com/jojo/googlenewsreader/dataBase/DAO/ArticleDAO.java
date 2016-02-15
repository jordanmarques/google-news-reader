package com.jojo.googlenewsreader.dataBase.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jojo.googlenewsreader.dataBase.AppDataBase;
import com.jojo.googlenewsreader.dataBase.AppDatabaseContract;
import com.jojo.googlenewsreader.dataBase.AppDatabaseContract.AppDatabaseEntry;
import com.jojo.googlenewsreader.pojo.Article;
import com.jojo.googlenewsreader.pojo.Tag;

import java.util.ArrayList;
import java.util.List;

public class ArticleDAO {

    private AppDataBase db;
    private SQLiteDatabase dbInstance;

    private final String[] PROJECTION_ARTICLE_TABLE = {
            AppDatabaseEntry.DATABASE_ARTICLE_COLUMN_ID,
            AppDatabaseEntry.DATABASE_ARTICLE_COLUMN_TITLE,
            AppDatabaseEntry.DATABASE_ARTICLE_COLUMN_CONTENT,
            AppDatabaseEntry.DATABASE_ARTICLE_COLUMN_IMAGE_URL,
            AppDatabaseEntry.DATABASE_ARTICLE_COLUMN_URL,
            AppDatabaseEntry.DATABASE_ARTICLE_COLUMN_PUBLISHER,
            AppDatabaseEntry.DATABASE_ARTICLE_COLUMN_DATE,
            AppDatabaseEntry.DATABASE_ARTICLE_COLUMN_DELETED};

    public ArticleDAO(Context context) {
        db = new AppDataBase(context);
        dbInstance = db.getWritableDatabase();
    }

    public long insertArticle(Article article){
        ContentValues values = new ContentValues();

        values.put(AppDatabaseEntry.DATABASE_ARTICLE_COLUMN_TITLE, article.getTitle());
        values.put(AppDatabaseEntry.DATABASE_ARTICLE_COLUMN_CONTENT, article.getContent());
        values.put(AppDatabaseEntry.DATABASE_ARTICLE_COLUMN_IMAGE_URL, article.getImageUrl());
        values.put(AppDatabaseEntry.DATABASE_ARTICLE_COLUMN_URL, article.getUrl());
        values.put(AppDatabaseEntry.DATABASE_ARTICLE_COLUMN_PUBLISHER, article.getPublisher());
        values.put(AppDatabaseEntry.DATABASE_ARTICLE_COLUMN_DATE, article.getDate());
        values.put(AppDatabaseEntry.DATABASE_ARTICLE_COLUMN_DELETED, 0);

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
                Article article = new Article(cursor.getInt(cursor.getColumnIndex(AppDatabaseEntry.DATABASE_ARTICLE_COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndex(AppDatabaseEntry.DATABASE_ARTICLE_COLUMN_TITLE)),
                        cursor.getString(cursor.getColumnIndex(AppDatabaseEntry.DATABASE_ARTICLE_COLUMN_CONTENT)),
                        cursor.getString(cursor.getColumnIndex(AppDatabaseEntry.DATABASE_ARTICLE_COLUMN_IMAGE_URL)),
                        cursor.getString(cursor.getColumnIndex(AppDatabaseEntry.DATABASE_ARTICLE_COLUMN_URL)),
                        cursor.getString(cursor.getColumnIndex(AppDatabaseEntry.DATABASE_ARTICLE_COLUMN_PUBLISHER)),
                        cursor.getString(cursor.getColumnIndex(AppDatabaseEntry.DATABASE_ARTICLE_COLUMN_DATE)),
                        cursor.getInt(cursor.getColumnIndex(AppDatabaseEntry.DATABASE_ARTICLE_COLUMN_DELETED)));
                article.setTagList(findArticlesTag(article));
                articles.add(article);
            }while(cursor.moveToNext());
        }
        cursor.close();
        return articles;
    }

    public Article findByTitleStrict(Article article){
        String[] title = {String.valueOf(article.getTitle())};

        Cursor cursor = dbInstance.query(
                AppDatabaseEntry.DATABASE_ARTICLE_TABLE,
                PROJECTION_ARTICLE_TABLE,
                AppDatabaseEntry.DATABASE_ARTICLE_COLUMN_TITLE + "=?",
                title,
                null,
                null,
                null);

        if(cursor.moveToFirst()){
            article = new Article(cursor.getInt(cursor.getColumnIndex(AppDatabaseEntry.DATABASE_ARTICLE_COLUMN_ID)),
                    cursor.getString(cursor.getColumnIndex(AppDatabaseEntry.DATABASE_ARTICLE_COLUMN_TITLE)),
                    cursor.getString(cursor.getColumnIndex(AppDatabaseEntry.DATABASE_ARTICLE_COLUMN_CONTENT)),
                    cursor.getString(cursor.getColumnIndex(AppDatabaseEntry.DATABASE_ARTICLE_COLUMN_IMAGE_URL)),
                    cursor.getString(cursor.getColumnIndex(AppDatabaseEntry.DATABASE_ARTICLE_COLUMN_URL)),
                    cursor.getString(cursor.getColumnIndex(AppDatabaseEntry.DATABASE_ARTICLE_COLUMN_PUBLISHER)),
                    cursor.getString(cursor.getColumnIndex(AppDatabaseEntry.DATABASE_ARTICLE_COLUMN_DATE)),
                    cursor.getInt(cursor.getColumnIndex(AppDatabaseEntry.DATABASE_ARTICLE_COLUMN_DELETED)));
            article.setTagList(findArticlesTag(article));
        }
        cursor.close();

        return article;

    }

    public List<Article> findByTitle(String query) {
        List<Article> articles = new ArrayList<>();
        query = "%" + query + "%";
        String[] title = {query};

        Cursor cursor = dbInstance.rawQuery("SELECT " + AppDatabaseEntry.DATABASE_ARTICLE_COLUMN_ID + "," +
                AppDatabaseEntry.DATABASE_ARTICLE_COLUMN_TITLE + "," +
                AppDatabaseEntry.DATABASE_ARTICLE_COLUMN_CONTENT + "," +
                AppDatabaseEntry.DATABASE_ARTICLE_COLUMN_IMAGE_URL + "," +
                AppDatabaseEntry.DATABASE_ARTICLE_COLUMN_URL + "," +
                AppDatabaseEntry.DATABASE_ARTICLE_COLUMN_PUBLISHER + "," +
                AppDatabaseEntry.DATABASE_ARTICLE_COLUMN_DATE + "," +
                AppDatabaseEntry.DATABASE_ARTICLE_COLUMN_DELETED +
                " FROM " + AppDatabaseEntry.DATABASE_ARTICLE_TABLE +
                " WHERE " + AppDatabaseEntry.DATABASE_ARTICLE_COLUMN_TITLE + " LIKE ?", title);

        if(cursor.moveToFirst()){
            do{
                Article article = new Article(cursor.getInt(cursor.getColumnIndex(AppDatabaseEntry.DATABASE_ARTICLE_COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndex(AppDatabaseEntry.DATABASE_ARTICLE_COLUMN_TITLE)),
                        cursor.getString(cursor.getColumnIndex(AppDatabaseEntry.DATABASE_ARTICLE_COLUMN_CONTENT)),
                        cursor.getString(cursor.getColumnIndex(AppDatabaseEntry.DATABASE_ARTICLE_COLUMN_IMAGE_URL)),
                        cursor.getString(cursor.getColumnIndex(AppDatabaseEntry.DATABASE_ARTICLE_COLUMN_URL)),
                        cursor.getString(cursor.getColumnIndex(AppDatabaseEntry.DATABASE_ARTICLE_COLUMN_PUBLISHER)),
                        cursor.getString(cursor.getColumnIndex(AppDatabaseEntry.DATABASE_ARTICLE_COLUMN_DATE)),
                        cursor.getInt(cursor.getColumnIndex(AppDatabaseEntry.DATABASE_ARTICLE_COLUMN_DELETED)));
                article.setTagList(findArticlesTag(article));
                articles.add(article);
            }while(cursor.moveToNext());
        }
        cursor.close();
        return articles;
    }

    public Article findById(long id){

        Article article = new Article();
        Cursor cursor = dbInstance.query(AppDatabaseEntry.DATABASE_ARTICLE_TABLE,
                PROJECTION_ARTICLE_TABLE,
                AppDatabaseEntry.DATABASE_ARTICLE_COLUMN_ID + "=?",
                new String[]{String.valueOf(id)},
                null,
                null,
                null);

        if(cursor.moveToFirst()){
            article = new Article(cursor.getInt(cursor.getColumnIndex(AppDatabaseEntry.DATABASE_ARTICLE_COLUMN_ID)),
                    cursor.getString(cursor.getColumnIndex(AppDatabaseEntry.DATABASE_ARTICLE_COLUMN_TITLE)),
                    cursor.getString(cursor.getColumnIndex(AppDatabaseEntry.DATABASE_ARTICLE_COLUMN_CONTENT)),
                    cursor.getString(cursor.getColumnIndex(AppDatabaseEntry.DATABASE_ARTICLE_COLUMN_IMAGE_URL)),
                    cursor.getString(cursor.getColumnIndex(AppDatabaseEntry.DATABASE_ARTICLE_COLUMN_URL)),
                    cursor.getString(cursor.getColumnIndex(AppDatabaseEntry.DATABASE_ARTICLE_COLUMN_PUBLISHER)),
                    cursor.getString(cursor.getColumnIndex(AppDatabaseEntry.DATABASE_ARTICLE_COLUMN_DATE)),
                    cursor.getInt(cursor.getColumnIndex(AppDatabaseEntry.DATABASE_ARTICLE_COLUMN_DELETED)));
            article.setTagList(findArticlesTag(article));
        }
        cursor.close();

        return article;
    }

    public List<Article> findByTag(String query){
        List<Article> articles = new ArrayList<>();
        String joinQuery = "SELECT " + " a." +AppDatabaseEntry.DATABASE_ARTICLE_COLUMN_ID + ", " +
                "a." + AppDatabaseEntry.DATABASE_ARTICLE_COLUMN_TITLE + ", " +
                "a." +AppDatabaseEntry.DATABASE_ARTICLE_COLUMN_CONTENT + ", " +
                "a." +AppDatabaseEntry.DATABASE_ARTICLE_COLUMN_IMAGE_URL + ", " +
                "a." +AppDatabaseEntry.DATABASE_ARTICLE_COLUMN_URL + ", " +
                "a." +AppDatabaseEntry.DATABASE_ARTICLE_COLUMN_PUBLISHER + ", " +
                "a." +AppDatabaseEntry.DATABASE_ARTICLE_COLUMN_DATE + ", " +
                "a." +AppDatabaseEntry.DATABASE_ARTICLE_COLUMN_DELETED +
        " FROM " + AppDatabaseEntry.DATABASE_ARTICLE_TABLE + " a" +
        " INNER JOIN " + AppDatabaseEntry.DATABASE_ARTICLE_TAG_TABLE + " at ON a." + AppDatabaseEntry.DATABASE_ARTICLE_COLUMN_ID + " = at." + AppDatabaseEntry.DATABASE_ARTICLE_TAG_COLUMN_ARTICLE_ID +
        " INNER JOIN " + AppDatabaseEntry.DATABASE_TAG_TABLE + " t ON at." + AppDatabaseEntry.DATABASE_ARTICLE_TAG_COLUMN_TAG_ID + " = t." + AppDatabaseEntry.DATABASE_TAG_COLUMN_ID +
        " WHERE t." + AppDatabaseEntry.DATABASE_TAG_COLUMN_LABEL + "=?";

        Cursor cursor = dbInstance.rawQuery(joinQuery, new String[]{String.valueOf(query)});

        if(cursor.moveToFirst()){
            do{
                Article article = new Article(cursor.getInt(cursor.getColumnIndex(AppDatabaseEntry.DATABASE_ARTICLE_COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndex(AppDatabaseEntry.DATABASE_ARTICLE_COLUMN_TITLE)),
                        cursor.getString(cursor.getColumnIndex(AppDatabaseEntry.DATABASE_ARTICLE_COLUMN_CONTENT)),
                        cursor.getString(cursor.getColumnIndex(AppDatabaseEntry.DATABASE_ARTICLE_COLUMN_IMAGE_URL)),
                        cursor.getString(cursor.getColumnIndex(AppDatabaseEntry.DATABASE_ARTICLE_COLUMN_URL)),
                        cursor.getString(cursor.getColumnIndex(AppDatabaseEntry.DATABASE_ARTICLE_COLUMN_PUBLISHER)),
                        cursor.getString(cursor.getColumnIndex(AppDatabaseEntry.DATABASE_ARTICLE_COLUMN_DATE)),
                        cursor.getInt(cursor.getColumnIndex(AppDatabaseEntry.DATABASE_ARTICLE_COLUMN_DELETED)));
                article.setTagList(findArticlesTag(article));
                articles.add(article);
            }while(cursor.moveToNext());
        }
        cursor.close();
        return articles;

    }

    public void deleteArticle(Article article){

        String selection = AppDatabaseEntry.DATABASE_ARTICLE_COLUMN_ID + " LIKE ?";
        String[] selectionArgs = { String.valueOf(article.getId()) };
        dbInstance.delete(AppDatabaseEntry.DATABASE_ARTICLE_TABLE, selection, selectionArgs);
    }

    public List<Tag> findArticlesTag(Article article){

        List<Tag> articles = new ArrayList<>();
        String joinQuery = "SELECT " + " t." +AppDatabaseEntry.DATABASE_TAG_COLUMN_ID + ", " +
                "t." + AppDatabaseEntry.DATABASE_TAG_COLUMN_LABEL + " " +
                " FROM " + AppDatabaseEntry.DATABASE_TAG_TABLE + " t" +
                " INNER JOIN " + AppDatabaseEntry.DATABASE_ARTICLE_TAG_TABLE + " at ON t." + AppDatabaseEntry.DATABASE_TAG_COLUMN_ID + " = at." + AppDatabaseEntry.DATABASE_ARTICLE_TAG_COLUMN_TAG_ID +
                " INNER JOIN " + AppDatabaseEntry.DATABASE_ARTICLE_TABLE + " a ON at." + AppDatabaseEntry.DATABASE_ARTICLE_TAG_COLUMN_ARTICLE_ID + " = a." + AppDatabaseEntry.DATABASE_ARTICLE_COLUMN_ID +
                " WHERE a." + AppDatabaseEntry.DATABASE_ARTICLE_COLUMN_ID + "=?";

        Cursor cursor = dbInstance.rawQuery(joinQuery, new String[]{String.valueOf(article.getId())});
        if(cursor.moveToFirst()){
            do{
                articles.add(new Tag(cursor.getInt(cursor.getColumnIndex(AppDatabaseEntry.DATABASE_TAG_COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndex(AppDatabaseEntry.DATABASE_TAG_COLUMN_LABEL))));
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
                AppDatabaseEntry.DATABASE_ARTICLE_COLUMN_TITLE + "=?",
                title,
                null,
                null,
                null);

        return cursor.moveToFirst();
    }
}
