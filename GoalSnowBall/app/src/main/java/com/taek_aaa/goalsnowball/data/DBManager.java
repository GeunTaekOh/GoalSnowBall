package com.taek_aaa.goalsnowball.data;

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
        db.execSQL("CREATE TABLE database (_id INTEGER PRIMARY KEY AUTOINCREMENT, year INTEGER, month INTEGER, date INTEGER, whatDateType INTEGER , goal TEXT, type TEXT, amount INTEGER, unit TEXT, currentAmount INTEGER, bettingGold INTEGER, isSuccess INTEGER);");
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
    public void insert(int year, int month, int date, int whatDateType, String goal, String type, int amount, String unit, int currentAmount, int bettingGold, int isSuccess) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("INSERT INTO database VALUES(NULL, " + year + ", " + month + ", " + date + ", " + whatDateType + ", '" + goal + "','" + type + "', " + amount + ", '" + unit + "', " + currentAmount + ", " + bettingGold + "," + isSuccess + ");");  //string넣을때는 '' 하고그안에""해야

        db.close();
    }

    public void setGoal(int findYear, int findMonth, int findDate, int findWhatDateType, String setGoalstr) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM database", null);

        while (cursor.moveToNext()) {
            int dbYear = cursor.getInt(cursor.getColumnIndex("year"));
            int dbMonth = cursor.getInt(cursor.getColumnIndex("month"));
            int dbDate = cursor.getInt(cursor.getColumnIndex("date"));
            int dbWhatDateType = cursor.getInt(cursor.getColumnIndex("whatDateType"));
            if ((dbYear == findYear) && (dbMonth == findMonth) && (dbDate == findDate) && (dbWhatDateType == findWhatDateType)) {
                String str = "UPDATE database SET goal='" + setGoalstr + "';";
                db.execSQL(str);
                break;
            }
        }
        cursor.close();
        db.close();
    }

    public void setCurrentAmount(int findYear, int findMonth, int findDate, int findWhatDateType, int setAmount) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM database", null);

        while (cursor.moveToNext()) {
            int dbYear = cursor.getInt(cursor.getColumnIndex("year"));
            int dbMonth = cursor.getInt(cursor.getColumnIndex("month"));
            int dbDate = cursor.getInt(cursor.getColumnIndex("date"));
            int dbWhatDateType = cursor.getInt(cursor.getColumnIndex("whatDateType"));
            if ((dbYear == findYear) && (dbMonth == findMonth) && (dbDate == findDate) && (dbWhatDateType == findWhatDateType)) {
                String str = "UPDATE database SET currentAmount=" + setAmount + ";";
                db.execSQL(str);
                break;
            }
        }
        cursor.close();
        db.close();
    }

    public int getCurrentAmount(int findYear, int findMonth, int findDate, int findWhatDateType) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM database", null);
        int result = 0;
        while (cursor.moveToNext()) {
            int dbYear = cursor.getInt(cursor.getColumnIndex("year"));
            int dbMonth = cursor.getInt(cursor.getColumnIndex("month"));
            int dbDate = cursor.getInt(cursor.getColumnIndex("date"));
            int dbWhatDateType = cursor.getInt(cursor.getColumnIndex("whatDateType"));
            if ((dbYear == findYear) && (dbMonth == findMonth) && (dbDate == findDate) && (dbWhatDateType == findWhatDateType)) {
                result = cursor.getInt(cursor.getColumnIndex("currentAmount"));
                break;
            }

        }
        return result;
    }

    public String getGrade() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM userInfo", null);
        String grade = "";
        while (cursor.moveToNext()) {
            grade = cursor.getString(cursor.getColumnIndex("grade"));
            Log.e("qwe", String.valueOf(grade));
        }
        return grade;
    }

    public void setName(String str) {
        SQLiteDatabase db = getReadableDatabase();
        String sql = "UPDATE userInfo SET name='" + str + "';";
        db.execSQL(sql);
        db.close();

    }


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

   /*
    public void delete(Double latitude, Double longitude) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM database WHERE latitude = " + latitude + " AND longitude = " + longitude);

        db.close();
    }*/

}