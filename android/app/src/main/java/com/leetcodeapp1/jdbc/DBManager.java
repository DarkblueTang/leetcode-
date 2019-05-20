package com.leetcodeapp1.jdbc;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.leetcodeapp1.entity.User;

public class DBManager {
    private DBHelper dbHelper;

    public DBManager(Context context) {
        dbHelper = new DBHelper(context);
    }

    public void saveUser(User user) {
        SQLiteDatabase database = null;
        try {
            database = dbHelper.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put("username", user.getUsername());
            contentValues.put("password", user.getPassword());

            database.insert(DBHelper.USER_TABLE, null, contentValues);
        } catch (Exception e) {

        } finally {
            if (database != null)
                database.close();
        }
    }

    public int getAllUser() {
        SQLiteDatabase db = null;
        try {
            db = dbHelper.getReadableDatabase();
            String sql = "select * from " + DBHelper.USER_TABLE;
            Cursor cursor = db.rawQuery(sql, new String[]{});
            return cursor.getCount();
        } catch (Exception e) {

        } finally {
            if (db != null)
                db.close();
        }

        return 0;
    }

    public void clearUserDB() {
        SQLiteDatabase db = null;
        try {
            db = dbHelper.getWritableDatabase();
            String sql = "delete from " + DBHelper.USER_TABLE;
            db.execSQL(sql);
        } catch (Exception e) {
        } finally {
            if (db != null)
                db.close();
        }
    }

    public User getFirstUser() {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        User user = null;
        try {
            db = dbHelper.getReadableDatabase();
            String sql = "select * from " + DBHelper.USER_TABLE;

            cursor = db.rawQuery(sql, new String[]{});

            if (cursor.moveToNext())
                user = new User(cursor.getString(0), cursor.getString(1));
            cursor.close();
        } catch (Exception e) {
        } finally {
            if (cursor != null)
                cursor.close();
            if (db != null)
                db.close();
        }

        return user;
    }
}
