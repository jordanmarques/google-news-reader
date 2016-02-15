package com.jojo.googlenewsreader.dataBase.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jojo.googlenewsreader.dataBase.AppDataBase;
import com.jojo.googlenewsreader.dataBase.AppDatabaseContract;
import com.jojo.googlenewsreader.pojo.Article;
import com.jojo.googlenewsreader.pojo.Article_Tag;
import com.jojo.googlenewsreader.pojo.Tag;

import java.util.ArrayList;
import java.util.List;

public class ArticleTagDAO {

    private AppDataBase db;
    private SQLiteDatabase dbInstance;

    private final String[] PROJECTION_ARTICLE_TAG_TABLE = {
            AppDatabaseContract.AppDatabaseEntry.DATABASE_ARTICLE_TAG_COLUMN_ARTICLE_ID,
            AppDatabaseContract.AppDatabaseEntry.DATABASE_ARTICLE_TAG_COLUMN_TAG_ID};

    public ArticleTagDAO(Context context) {
        db = new AppDataBase(context);
        dbInstance = db.getWritableDatabase();
    }

    public List<Article_Tag> findAll(){
        List<Article_Tag> article_tag = new ArrayList<>();
        Cursor cursor = dbInstance.query(AppDatabaseContract.AppDatabaseEntry.DATABASE_ARTICLE_TAG_TABLE,
                new String[]{"*"},
                null,
                null,
                null,
                null,
                null);

        if(cursor.moveToFirst()){
            do{
                article_tag.add(new Article_Tag(cursor.getInt(cursor.getColumnIndex(AppDatabaseContract.AppDatabaseEntry.DATABASE_ARTICLE_TAG_COLUMN_ARTICLE_ID)),
                        cursor.getInt(cursor.getColumnIndex(AppDatabaseContract.AppDatabaseEntry.DATABASE_ARTICLE_TAG_COLUMN_TAG_ID))));
            }while(cursor.moveToNext());
        }
        cursor.close();
        return article_tag;
    }

    public void deleteArticleTagLink(Tag tag){
        String selection = AppDatabaseContract.AppDatabaseEntry.DATABASE_ARTICLE_TAG_COLUMN_TAG_ID + " LIKE ?";
        String[] selectionArgs = { String.valueOf(tag.getId()) };
        dbInstance.delete(AppDatabaseContract.AppDatabaseEntry.DATABASE_TAG_TABLE, selection, selectionArgs);
    }

    public void deleteArticleTagLink(Article article){
        String selection = AppDatabaseContract.AppDatabaseEntry.DATABASE_ARTICLE_TAG_COLUMN_ARTICLE_ID + " LIKE ?";
        String[] selectionArgs = { String.valueOf(article.getId()) };
        dbInstance.delete(AppDatabaseContract.AppDatabaseEntry.DATABASE_TAG_TABLE, selection, selectionArgs);
    }

    public long insertArticleTagLink(Article article, Tag tag){
        ContentValues values = new ContentValues();
        values.put(AppDatabaseContract.AppDatabaseEntry.DATABASE_ARTICLE_TAG_COLUMN_ARTICLE_ID, article.getId());
        values.put(AppDatabaseContract.AppDatabaseEntry.DATABASE_ARTICLE_TAG_COLUMN_TAG_ID, tag.getId());
        return dbInstance.insert(AppDatabaseContract.AppDatabaseEntry.DATABASE_ARTICLE_TAG_TABLE, null, values);
    }
}
