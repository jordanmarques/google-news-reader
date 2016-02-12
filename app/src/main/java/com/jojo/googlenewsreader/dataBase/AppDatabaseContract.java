package com.jojo.googlenewsreader.dataBase;


import android.provider.BaseColumns;

public class AppDatabaseContract {

    public AppDatabaseContract() {}

    public static abstract class AppDatabaseEntry implements BaseColumns{

        public static final String DATABASE_NAME = "Google-news-reader-DB.db";

        public static final String DATABASE_ARTICLE_TABLE = "Article";

        public static final String DATABASE_COLUMN_ID = "id";
        public static final String DATABASE_COLUMN_TITLE = "title";
        public static final String DATABASE_COLUMN_CONTENT = "content";
        public static final String DATABASE_COLUMN_IMAGE_URL = "imageUrl";
        public static final String DATABASE_COLUMN_URL = "url";
        public static final String DATABASE_COLUMN_DELETED = "deleted";
        public static final String DATABASE_COLUMN_PUBLISHER = "publisher";
        public static final String DATABASE_COLUMN_DATE = "publication_date";

        public static final int DATABASE_VERSION = 3;

        public static final String INIT_DB_TABLES = "CREATE TABLE IF NOT EXISTS " + DATABASE_ARTICLE_TABLE + "(" +
                DATABASE_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"+
                DATABASE_COLUMN_TITLE + " VARCHAR," +
                DATABASE_COLUMN_CONTENT + " VARCHAR," +
                DATABASE_COLUMN_IMAGE_URL + " VARCHAR," +
                DATABASE_COLUMN_URL + " VARCHAR," +
                DATABASE_COLUMN_PUBLISHER + " VARCHAR," +
                DATABASE_COLUMN_DATE + " VARCHAR," +
                DATABASE_COLUMN_DELETED + " BOOLEAN);";

        public static final String DELETE_DB_TABLES = "DROP TABLE IF EXISTS " + DATABASE_ARTICLE_TABLE;
    }
}
