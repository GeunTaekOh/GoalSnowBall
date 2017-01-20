package com.taek_aaa.goalsnowball.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by taek_aaa on 2017. 1. 20..
 */

public class UserDBManager extends SQLiteOpenHelper {
    public UserDBManager(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE userInfo (_id INTEGER PRIMARY KEY AUTOINCREMENT, grade TEXT , name TEXT, gold INTEGER, picturePath TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("drop table if exists userInfo;");
        onCreate(db);
    }
}
