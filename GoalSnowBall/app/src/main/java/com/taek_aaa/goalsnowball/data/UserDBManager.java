package com.taek_aaa.goalsnowball.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by taek_aaa on 2017. 1. 20..
 */

//db 이름 userInfo
public class UserDBManager extends SQLiteOpenHelper {
    public UserDBManager(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE userInfo (_id INTEGER PRIMARY KEY AUTOINCREMENT, grade TEXT , name TEXT, gold INTEGER, picturePath TEXT);");
        db.execSQL("INSERT INTO userInfo VALUES(NULL, '" + "브론즈" + "', '" + "userName" + "', " + 10 + ", '" + "null" + "');");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("drop table if exists userInfo;");
        onCreate(db);
    }

    public void insert(String grade, String name, int gold, String picturePath) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("INSERT INTO userInfo VALUES(NULL, '" + grade + "', '" + name + "', " + gold + ", '" + picturePath + "');");
        db.close();
    }

    public void setGrade(String str) {
        SQLiteDatabase db = getReadableDatabase();
        String sql =  "UPDATE userInfo SET grade='"+str+"';";
        db.execSQL(sql);
        db.close();

    }
    public void setName(String str){
        SQLiteDatabase db = getReadableDatabase();
        String sql =  "UPDATE userInfo SET name='"+str+"';";
        db.execSQL(sql);
        db.close();

    }
    public void setGold(int gd){
        SQLiteDatabase db = getReadableDatabase();
        String sql =  "UPDATE userInfo SET gold="+gd;
        db.execSQL(sql);
        db.close();
    }
    public void setPicturePath(String str){
        SQLiteDatabase db = getReadableDatabase();
        String sql =  "UPDATE userInfo SET picturePath='"+str+"';";
        db.execSQL(sql);
        db.close();
    }

    public String getGrade() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM userInfo", null);
        String grade="";
        while (cursor.moveToNext()) {
            grade = cursor.getString(cursor.getColumnIndex("grade"));
            Log.e("qwe", String.valueOf(grade));
        }
        return grade;
    }
    public String getName() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM userInfo", null);
        String name="";
        while (cursor.moveToNext()) {
            name = cursor.getString(cursor.getColumnIndex("name"));
            Log.e("qwe", String.valueOf(name));
        }
        return name;
    }
    public int getGold() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM userInfo", null);
        int gold=0;
        while (cursor.moveToNext()) {
            gold = cursor.getInt(cursor.getColumnIndex("gold"));
            Log.e("qwe", "gold : "+String.valueOf(gold));

        }
        return gold;
    }
    public String getPicturePath() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM userInfo", null);
        String Path="";
        while (cursor.moveToNext()) {
            Path = cursor.getString(cursor.getColumnIndex("picturePath"));
            Log.e("qwe", String.valueOf(Path));
        }
        return Path;
    }




}
