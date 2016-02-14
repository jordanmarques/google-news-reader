package com.jojo.googlenewsreader.dataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.jojo.googlenewsreader.dataBase.AppDatabaseContract.AppDatabaseEntry;

public class AppDataBase extends SQLiteOpenHelper{

    public AppDataBase(Context context) {
        super(context, AppDatabaseEntry.DATABASE_NAME, null, AppDatabaseEntry.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(AppDatabaseEntry.CREATE_ARTICLE_TABLE);
        db.execSQL(AppDatabaseEntry.CREATE_TAG_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(AppDatabaseEntry.DELETE_DB_TABLES);
        onCreate(db);
    }
}
