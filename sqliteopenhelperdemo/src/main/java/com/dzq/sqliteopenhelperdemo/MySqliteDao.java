package com.dzq.sqliteopenhelperdemo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description：
 * Created by duzhiqi on 2016/11/4.
 */

public class MySqliteDao {
    private static final String TABLE = "test";
    private final String DB_NAME = "test.db";
    private MySqliteOpenHelper helper;

    public MySqliteDao(Context context) {
        this.helper = new MySqliteOpenHelper(context, DB_NAME, null, 1);
    }

    /**
     *
     * @param ages
     * @return
     */
    public List<User> queryByAge(String[] ages) {
        SQLiteDatabase db = helper.getReadableDatabase();
        List<User> list = null;
        User user;
        if (db.isOpen()) {
            list = new ArrayList<>();
            String sql = "select * from " + TABLE + " where age in (";
            for (String age : ages) {
                sql = sql + "\'" + age + "\'" + ", ";
            }
            sql = sql + "\'" + ages[0] + "\'" + ")";
            Cursor cursor = db.rawQuery(sql, null);
            while (cursor.moveToNext()) {
                user = new User();
                user.setAge(cursor.getInt(1));
                user.setFirstName(cursor.getString(2));
                user.setLastName(cursor.getString(3));
                list.add(user);
            }
            cursor.close();
        }

        return list;
    }

    public List<User> query(HashMap<String, String> params) {
        SQLiteDatabase db = helper.getReadableDatabase();
        List<User> list = null;
        if (db.isOpen()) {
            list = new ArrayList<>();
            String sql = "select * from " + TABLE + " where ";
            for (String key : params.keySet()) {
                sql += key + " = " + "\'" + params.get(key) + "\'" + " and ";
            }
            int index = sql.lastIndexOf("and");
            sql = sql.substring(0, index);
            User user;
            Cursor cursor = db.rawQuery(sql, null);
            while (cursor.moveToNext()) {
                user = new User();
                user.setAge(cursor.getInt(1));
                user.setFirstName(cursor.getString(2));
                user.setLastName(cursor.getString(3));
                list.add(user);
            }
            cursor.close();
        }
        return list;
    }

    public void insert(List<User> users) {
        SQLiteDatabase db = helper.getWritableDatabase();

        if (db.isOpen()) {
            for (User user : users) {
                ContentValues values = new ContentValues();
                values.put("age", user.getAge());
                values.put("firstName", user.getFirstName());
                values.put("lastName", user.getLastName());
                db.insert(TABLE, null, values);
            }

        }
    }

    public void insert(User user) {
        SQLiteDatabase db = helper.getWritableDatabase();
        if (db.isOpen()) {
            ContentValues values = new ContentValues();
            values.put("age", user.getAge());
            values.put("firstName", user.getFirstName());
            values.put("lastName", user.getLastName());
            db.insert(TABLE, null, values);
        }
    }

    private class Params {
        String key;
        String[] values;
    }

    public List<User> query(List<Params> paramsList) {
        List<User> result = null;
        SQLiteDatabase db = helper.getReadableDatabase();
        if (db.isOpen()) {
            result = new ArrayList<>();
            String sql = " select * from " + TABLE + " where ";
            for (Params params : paramsList) {
                sql += params.key + " in (" ;
                for (String value : params.values) {
                    sql += "\'" + value + "\', ";
                }
                sql += "\'" + params.values[0] + "\'" + ") and ";
            }
            int index = sql.lastIndexOf("and");
            sql = sql.substring(0, index);
            Cursor cursor = db.rawQuery(sql, null);
            User user;
            while (cursor.moveToNext()){
                user = new User();
                user.setAge(cursor.getInt(1));
                user.setFirstName(cursor.getString(2));
                user.setLastName(cursor.getString(3));
                result.add(user);
            }
            cursor.close();
        }
        return result;
    }
}
