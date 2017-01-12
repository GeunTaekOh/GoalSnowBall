package com.taek_aaa.goalsnowball;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.LinkedList;

/**
 * Created by taek_aaa on 2017. 1. 11..
 */

public class DBManager extends SQLiteOpenHelper {

    public DBManager(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 새로운 Table 생성
        db.execSQL("CREATE TABLE database (_id INTEGER PRIMARY KEY AUTOINCREMENT, picturePath TEXT , userName TEXT, grade TEXT, cash INTEGER, todayGoalPercent INTEGER, weekGoalPercent INTEGER, monthGoalPercent INTEGER, todayGoal TEXT, weekGoal TEXT, monthGoal TEXT);");
    }

    /**
     * DB버전을 높여서 DB삭제하는 효과를 줌
     **/
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists database;");
        onCreate(db);
    }

    /**
     * DB에 데이터를 저장
     **/
    public void insert(double latitude, double longitude, String todoOrEvent, int category, int howLong, int num, String text, String time) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("INSERT INTO database VALUES(NULL, " + latitude + ", " + longitude + ", '" + todoOrEvent + "', " + category + ", " + howLong + ", " + num + ", '" + text + "', '" + time + "');");  //string넣을때는 '' 하고그안에""해야
        db.close();
    }

    /**
     * DB에 값을 LinkedList로 꺼냄
     **/
    public void getResult(LinkedList<DBData> sllDBData) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM database", null);

        while (cursor.moveToNext()) {
            double latitudecur = cursor.getDouble(cursor.getColumnIndex("latitude"));
            double longitudecur = cursor.getDouble(cursor.getColumnIndex("longitude"));
            String toDoOrEventcur = cursor.getString(cursor.getColumnIndex("TodoOrEvent"));
            int categorycur = cursor.getInt(cursor.getColumnIndex("category"));
            int howLongcur = cursor.getInt(cursor.getColumnIndex("HowLong"));
            String numcur = cursor.getString(cursor.getColumnIndex("num"));
            String textcur = cursor.getString(cursor.getColumnIndex("text"));
            String timecur = cursor.getString(cursor.getColumnIndex("time"));

            Log.i("tt", String.valueOf(latitudecur));
            Log.i("tt", String.valueOf(longitudecur));
            Log.i("tt", toDoOrEventcur);
            Log.i("tt", String.valueOf(categorycur));
            Log.i("tt", String.valueOf(howLongcur));
            Log.i("tt", numcur);
            Log.i("tt", textcur);
            Log.i("tt", timecur);

            DBData dbdata = new DBData();

            //dbdata.curlatitude = latitudecur;


            sllDBData.add(dbdata);
        }
        cursor.close();
        db.close();
    }



    /**
     * DB 선택한 부분의 마커 삭제
     **/
    public void delete(Double latitude, Double longitude) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM database WHERE latitude = " + latitude + " AND longitude = " + longitude);

        db.close();
    }

}