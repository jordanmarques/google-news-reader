package com.jojo.googlenewsreader.dataBase.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jojo.googlenewsreader.dataBase.AppDataBase;
import com.jojo.googlenewsreader.dataBase.AppDatabaseContract.AppDatabaseEntry;
import com.jojo.googlenewsreader.pojo.Article;
import com.jojo.googlenewsreader.pojo.ArticleTag;
import com.jojo.googlenewsreader.pojo.Tag;

import java.util.ArrayList;
import java.util.List;

public class ArticleTagDAO {

    private AppDataBase db;
    private SQLiteDatabase dbInstance;

    private final String[] PROJECTION_ARTICLE_TAG_TABLE = {
            AppDatabaseEntry.DATABASE_ARTICLE_TAG_COLUMN_ARTICLE_ID,
            AppDatabaseEntry.DATABASE_ARTICLE_TAG_COLUMN_TAG_ID};

    public ArticleTagDAO(Context context) {
        db = new AppDataBase(context);
        dbInstance = db.getWritableDatabase();
    }

    public List<ArticleTag> findAll(){
        List<ArticleTag> article_tag = new ArrayList<>();
        Cursor cursor = dbInstance.query(AppDatabaseEntry.DATABASE_ARTICLE_TAG_TABLE,
                new String[]{"*"},
                null,
                null,
                null,
                null,
                null);

        if(cursor.moveToFirst()){
            do{
                article_tag.add(new ArticleTag(cursor.getInt(cursor.getColumnIndex(AppDatabaseEntry.DATABASE_ARTICLE_TAG_COLUMN_ARTICLE_ID)),
                        cursor.getInt(cursor.getColumnIndex(AppDatabaseEntry.DATABASE_ARTICLE_TAG_COLUMN_TAG_ID))));
            }while(cursor.moveToNext());
        }
        cursor.close();
        return article_tag;
    }

    public void deleteArticleTagLink(Tag tag){
        String selection = AppDatabaseEntry.DATABASE_ARTICLE_TAG_COLUMN_TAG_ID + " LIKE ?";
        String[] selectionArgs = { String.valueOf(tag.getId()) };
        dbInstance.delete(AppDatabaseEntry.DATABASE_TAG_TABLE, selection, selectionArgs);
    }

    public void deleteArticleTagLink(Article article, Tag tag){
        String deleteLink = "DELETE FROM " + AppDatabaseEntry.DATABASE_ARTICLE_TAG_TABLE+ " WHERE " + AppDatabaseEntry.DATABASE_ARTICLE_TAG_COLUMN_ARTICLE_ID + "=" + String.valueOf(article.getId()) + " AND " +
                AppDatabaseEntry.DATABASE_ARTICLE_TAG_COLUMN_TAG_ID + "=" + String.valueOf(tag.getId()) + ";";
        dbInstance.execSQL(deleteLink);

    }

    public void deleteArticleTagLink(Article article){
        String selection = AppDatabaseEntry.DATABASE_ARTICLE_TAG_COLUMN_ARTICLE_ID + " LIKE ?";
        String[] selectionArgs = { String.valueOf(article.getId()) };
        dbInstance.delete(AppDatabaseEntry.DATABASE_ARTICLE_TAG_TABLE, selection, selectionArgs);
    }

    public long insertArticleTagLink(Article article, Tag tag){
        ContentValues values = new ContentValues();
        values.put(AppDatabaseEntry.DATABASE_ARTICLE_TAG_COLUMN_ARTICLE_ID, article.getId());
        values.put(AppDatabaseEntry.DATABASE_ARTICLE_TAG_COLUMN_TAG_ID, tag.getId());
        return dbInstance.insert(AppDatabaseEntry.DATABASE_ARTICLE_TAG_TABLE, null, values);
    }
}
