package com.jojo.googlenewsreader.dataBase.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jojo.googlenewsreader.dataBase.AppDataBase;
import com.jojo.googlenewsreader.dataBase.AppDatabaseContract;
import com.jojo.googlenewsreader.pojo.Tag;

import java.util.ArrayList;
import java.util.List;

public class TagDAO {

    private AppDataBase db;
    private SQLiteDatabase dbInstance;

    private final String[] PROJECTION_TAG_TABLE = {
            AppDatabaseContract.AppDatabaseEntry.DATABASE_TAG_COLUMN_ID,
            AppDatabaseContract.AppDatabaseEntry.DATABASE_TAG_COLUMN_LABEL};

    public TagDAO(Context context) {
        db = new AppDataBase(context);
        dbInstance = db.getWritableDatabase();
    }

    public List<Tag> findAllTags(){
        List<Tag> articles = new ArrayList<>();
        Cursor cursor = dbInstance.query(AppDatabaseContract.AppDatabaseEntry.DATABASE_TAG_TABLE,
                PROJECTION_TAG_TABLE,
                null,
                null,
                null,
                null,
                null);

        if(cursor.moveToFirst()){
            do{
                articles.add(new Tag(cursor.getInt(cursor.getColumnIndex(AppDatabaseContract.AppDatabaseEntry.DATABASE_TAG_COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndex(AppDatabaseContract.AppDatabaseEntry.DATABASE_TAG_COLUMN_LABEL))));
            }while(cursor.moveToNext());
        }
        cursor.close();
        return articles;
    }

    public Tag findById(long id){

        Tag tag = new Tag();
        Cursor cursor = dbInstance.query(AppDatabaseContract.AppDatabaseEntry.DATABASE_TAG_TABLE,
                PROJECTION_TAG_TABLE,
                AppDatabaseContract.AppDatabaseEntry.DATABASE_TAG_COLUMN_ID + "=?",
                new String[]{String.valueOf(id)},
                null,
                null,
                null);

        if(cursor.moveToFirst()){
            tag = new Tag(cursor.getInt(cursor.getColumnIndex(AppDatabaseContract.AppDatabaseEntry.DATABASE_TAG_COLUMN_ID)),
                    cursor.getString(cursor.getColumnIndex(AppDatabaseContract.AppDatabaseEntry.DATABASE_TAG_COLUMN_LABEL)));
        }
        cursor.close();

        return tag;
    }

    public void deleteTag(Tag tag){
        String selection = AppDatabaseContract.AppDatabaseEntry.DATABASE_TAG_COLUMN_ID + " LIKE ?";
        String[] selectionArgs = { String.valueOf(tag.getId()) };
        dbInstance.delete(AppDatabaseContract.AppDatabaseEntry.DATABASE_TAG_TABLE, selection, selectionArgs);
    }

    public long insertTag(Tag tag){
        ContentValues values = new ContentValues();

        values.put(AppDatabaseContract.AppDatabaseEntry.DATABASE_TAG_COLUMN_LABEL, tag.getLabel());

        return dbInstance.insert(AppDatabaseContract.AppDatabaseEntry.DATABASE_TAG_TABLE, null, values);
    }
}
