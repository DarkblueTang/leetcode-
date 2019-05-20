package com.leetcodeapp1.jdbc;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "leetcode.db";
    private static final int version = 1;
    public static final String USER_TABLE = "user";

    public DBHelper(Context context) {
        super(context, DB_NAME, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table if not exists " + USER_TABLE +
                " (username text primary key, password text)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "DROP TABLE IF EXISTS " + USER_TABLE;
        db.execSQL(sql);
        onCreate(db);
    }
}
