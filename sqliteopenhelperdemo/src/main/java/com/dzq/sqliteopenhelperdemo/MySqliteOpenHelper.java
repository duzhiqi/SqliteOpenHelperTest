package com.dzq.sqliteopenhelperdemo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Description：
 * Created by duzhiqi on 2016/11/4.
 */

public class MySqliteOpenHelper extends SQLiteOpenHelper {

    private static final String TABLE = "test";

    public MySqliteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql = "create table if not exists " + TABLE + "(_id integer primary key autoincrement," +
                "age integer, firstName text, lastName text);";
        sqLiteDatabase.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int j) {
        String sql = "drop table if exists " + TABLE;
        sqLiteDatabase.execSQL(sql);
        onCreate(sqLiteDatabase);
    }
}
