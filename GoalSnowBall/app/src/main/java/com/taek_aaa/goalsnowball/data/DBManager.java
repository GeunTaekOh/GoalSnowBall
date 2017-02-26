package com.taek_aaa.goalsnowball.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.taek_aaa.goalsnowball.data.CommonData.FROM_MONTH;
import static com.taek_aaa.goalsnowball.data.CommonData.FROM_TODAY;
import static com.taek_aaa.goalsnowball.data.CommonData.FROM_WEEK;


/**
 * Created by taek_aaa on 2017. 1. 11..
 */


/**
 * is success 가 0이면 저장 안함 1이면 성공 2이면 하는중 3이면 실패
 **/
public class DBManager extends SQLiteOpenHelper {


    public volatile static DBManager dbManagerInstance;
    private static final String DATABASE_NAME = "goaldb.db";
    private static final int DATABASE_VERSION = 1;

    public static synchronized DBManager getInstance(Context context) {
        if (dbManagerInstance == null) {
            dbManagerInstance = new DBManager(context.getApplicationContext());
        }
        return dbManagerInstance;
    }


    public DBManager(Context context) {//, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 새로운 Table 생성
        db.execSQL("CREATE TABLE database (_id INTEGER PRIMARY KEY AUTOINCREMENT, year INTEGER, month INTEGER, date INTEGER, weekOfYear INTEGER, whatDateType INTEGER , goal TEXT, type TEXT, amount INTEGER, unit TEXT, currentAmount INTEGER, bettingGold INTEGER, isSuccess INTEGER);");
    }

    /**
     * DB버전을 높여서 DB삭제하는 효과를 줌
     **/
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table database;");
        onCreate(db);
    }

    public void deleteAll(SQLiteDatabase db) {
        db.execSQL("drop table database;");
        onCreate(db);
    }

    /**
     * DB에 데이터를 저장
     **/
    public void insert(int whatDateType, String goal, String type, int amount, String unit, int currentAmount, int bettingGold, int isSuccess) {
        SQLiteDatabase db = getWritableDatabase();
        CalendarDatas today = new CalendarDatas();
        int year = today.cYear;
        int month = today.cMonth;
        int date = today.cdate;
        int weekOfYear = today.weekOfYear;
        db.execSQL("INSERT INTO database VALUES(NULL, " + year + ", " + month + ", " + date + ", " + weekOfYear + ", " + whatDateType + ", '" + goal + "','" + type + "', " + amount + ", '" + unit + "', " + currentAmount + ", " + bettingGold + "," + isSuccess + ");");  //string넣을때는 '' 하고그안에""해야

    }

    public void setCurrentAmount(int findWhatDateType, int setAmount) {
        SQLiteDatabase db = getReadableDatabase();
        CalendarDatas today = new CalendarDatas();
        int findYear = today.cYear;
        int findMonth = today.cMonth;
        int findDate = today.cdate;
        int findWeekOfyear = today.weekOfYear;
        String str = "";

        switch (findWhatDateType) {
            case FROM_TODAY:
                str = "UPDATE database SET currentAmount=" + setAmount + " WHERE year = " + findYear + " AND month = " + findMonth + " AND date = " + findDate + " AND whatDateType=" + findWhatDateType + ";";
                db.execSQL(str);
                break;
            case FROM_WEEK:
                str = "UPDATE database SET currentAmount=" + setAmount + " WHERE year =" + findYear + " AND month = " + findMonth + " AND weekOfYear = " + findWeekOfyear + " AND whatDateType=" + findWhatDateType + ";";
                db.execSQL(str);
                break;
            case FROM_MONTH:
                str = "UPDATE database SET currentAmount=" + setAmount + " WHERE year = " + findYear + " AND month = " + findMonth + " AND whatDateType=" + findWhatDateType + ";";
                db.execSQL(str);
                break;
        }
    }


    public void setIsSuccess(int findWhatDateType, int status) {
        SQLiteDatabase db = getReadableDatabase();
        CalendarDatas today = new CalendarDatas();
        int findYear = today.cYear;
        int findMonth = today.cMonth;
        int findDate = today.cdate;
        int findWeekOfYear = today.weekOfYear;
        String str = "";

        switch (findWhatDateType) {
            case FROM_TODAY:
                str = "UPDATE database SET isSuccess=" + status + " WHERE year = " + findYear + " AND month = " + findMonth + " AND date = " + findDate + " AND whatDateType=" + findWhatDateType + ";";
                db.execSQL(str);
                break;
            case FROM_WEEK:
                str = "UPDATE database SET isSuccess=" + status + " WHERE year =" + findYear + " AND month = " + findMonth + " AND weekOfYear = " + findWeekOfYear + " AND whatDateType=" + findWhatDateType + ";";
                db.execSQL(str);
                break;
            case FROM_MONTH:
                str = "UPDATE database SET isSuccess=" + status + " WHERE year = " + findYear + " AND month = " + findMonth + " AND whatDateType=" + findWhatDateType + ";";
                db.execSQL(str);
                break;
        }
    }

    public int getCurrentAmount(int findWhatDateType) {
        SQLiteDatabase db = getReadableDatabase();
        CalendarDatas today = new CalendarDatas();
        int findYear = today.cYear;
        int findMonth = today.cMonth;
        int findDate = today.cdate;
        int findWeekOfYear = today.weekOfYear;

        Cursor cursor = db.rawQuery("SELECT * FROM database", null);
        int result = 0;

        while (cursor.moveToNext()) {
            int dbYear = cursor.getInt(cursor.getColumnIndex("year"));
            int dbMonth = cursor.getInt(cursor.getColumnIndex("month"));
            int dbDate = cursor.getInt(cursor.getColumnIndex("date"));
            int dbWhatDateType = cursor.getInt(cursor.getColumnIndex("whatDateType"));
            int dbWeekOfYear = cursor.getInt(cursor.getColumnIndex("weekOfYear"));

            switch (findWhatDateType) {
                case FROM_TODAY:
                    if ((dbYear == findYear) && (dbMonth == findMonth) && (dbDate == findDate) && (dbWhatDateType == findWhatDateType)) {
                        result = cursor.getInt(cursor.getColumnIndex("currentAmount"));
                    }
                    break;
                case FROM_WEEK:
                    if ((dbYear == findYear) && (dbWeekOfYear == findWeekOfYear) && (dbWhatDateType == findWhatDateType)) {
                        result = cursor.getInt(cursor.getColumnIndex("currentAmount"));
                    }
                    break;
                case FROM_MONTH:
                    if ((dbYear == findYear) && (dbMonth == findMonth) && (dbWhatDateType == findWhatDateType)) {
                        result = cursor.getInt(cursor.getColumnIndex("currentAmount"));
                    }
                    break;
            }
        }
        cursor.close();
        return result;
    }


    public int getIsSuccess(int findWhatDateType) {
        SQLiteDatabase db = getReadableDatabase();
        CalendarDatas today = new CalendarDatas();
        int findYear = today.cYear;
        int findMonth = today.cMonth;
        int findDate = today.cdate;
        int findWeekOfYear = today.weekOfYear;

        Cursor cursor = db.rawQuery("SELECT * FROM database", null);
        int result = 0;

        while (cursor.moveToNext()) {

            int dbYear = cursor.getInt(cursor.getColumnIndex("year"));
            int dbMonth = cursor.getInt(cursor.getColumnIndex("month"));
            int dbDate = cursor.getInt(cursor.getColumnIndex("date"));
            int dbWhatDateType = cursor.getInt(cursor.getColumnIndex("whatDateType"));
            int dbWeekOfYear = cursor.getInt(cursor.getColumnIndex("weekOfYear"));


            switch (findWhatDateType) {
                case FROM_TODAY:
                    if ((dbYear == findYear) && (dbMonth == findMonth) && (dbDate == findDate) && (dbWhatDateType == findWhatDateType)) {
                        result = cursor.getInt(cursor.getColumnIndex("isSuccess"));
                    }
                    break;
                case FROM_WEEK:
                    if ((dbYear == findYear) && (dbWeekOfYear == findWeekOfYear) && (dbWhatDateType == findWhatDateType)) {
                        result = cursor.getInt(cursor.getColumnIndex("isSuccess"));
                    }
                    break;
                case FROM_MONTH:
                    if ((dbYear == findYear) && (dbMonth == findMonth) && (dbWhatDateType == findWhatDateType)) {
                        result = cursor.getInt(cursor.getColumnIndex("isSuccess"));
                    }
                    break;
            }
        }
        cursor.close();
        return result;
    }

    public String getGoal(int findWhatDateType) {
        SQLiteDatabase db = getReadableDatabase();
        CalendarDatas today = new CalendarDatas();
        int findYear = today.cYear;
        int findMonth = today.cMonth;
        int findDate = today.cdate;
        int findWeekOfYear = today.weekOfYear;

        Cursor cursor = db.rawQuery("SELECT * FROM database", null);
        String result = "";

        while (cursor.moveToNext()) {
            int dbYear = cursor.getInt(cursor.getColumnIndex("year"));
            int dbMonth = cursor.getInt(cursor.getColumnIndex("month"));
            int dbDate = cursor.getInt(cursor.getColumnIndex("date"));
            int dbWhatDateType = cursor.getInt(cursor.getColumnIndex("whatDateType"));
            int dbWeekOfYear = cursor.getInt(cursor.getColumnIndex("weekOfYear"));

            switch (findWhatDateType) {
                case FROM_TODAY:
                    if ((dbYear == findYear) && (dbMonth == findMonth) && (dbDate == findDate) && (dbWhatDateType == findWhatDateType)) {
                        result = cursor.getString(cursor.getColumnIndex("goal"));
                    }
                    break;
                case FROM_WEEK:
                    if ((dbYear == findYear) && (dbWeekOfYear == findWeekOfYear) && (dbWhatDateType == findWhatDateType)) {
                        result = cursor.getString(cursor.getColumnIndex("goal"));
                    }
                    break;
                case FROM_MONTH:
                    if ((dbYear == findYear) && (dbMonth == findMonth) && (dbWhatDateType == findWhatDateType)) {
                        result = cursor.getString(cursor.getColumnIndex("goal"));
                    }
                    break;
            }

        }
        cursor.close();
        return result;
    }

    public int getGoalAmount(int findWhatDateType) {
        SQLiteDatabase db = getReadableDatabase();
        CalendarDatas today = new CalendarDatas();
        int findYear = today.cYear;
        int findMonth = today.cMonth;
        int findDate = today.cdate;
        int findWeekOfYear = today.weekOfYear;

        Cursor cursor = db.rawQuery("SELECT * FROM database", null);
        int result = 0;


        while (cursor.moveToNext()) {
            int dbYear = cursor.getInt(cursor.getColumnIndex("year"));
            int dbMonth = cursor.getInt(cursor.getColumnIndex("month"));
            int dbDate = cursor.getInt(cursor.getColumnIndex("date"));
            int dbWhatDateType = cursor.getInt(cursor.getColumnIndex("whatDateType"));
            int dbWeekOfYear = cursor.getInt(cursor.getColumnIndex("weekOfYear"));

            switch (findWhatDateType) {
                case FROM_TODAY:
                    if ((dbYear == findYear) && (dbMonth == findMonth) && (dbDate == findDate) && (dbWhatDateType == findWhatDateType)) {
                        result = cursor.getInt(cursor.getColumnIndex("amount"));
                    }
                    break;
                case FROM_WEEK:
                    if ((dbYear == findYear) && (dbWeekOfYear == findWeekOfYear) && (dbWhatDateType == findWhatDateType)) {
                        result = cursor.getInt(cursor.getColumnIndex("amount"));
                    }
                    break;
                case FROM_MONTH:
                    if ((dbYear == findYear) && (dbMonth == findMonth) && (dbWhatDateType == findWhatDateType)) {
                        result = cursor.getInt(cursor.getColumnIndex("amount"));
                    }
                    break;
            }
        }
        cursor.close();
        return result;
    }

    public int getBettingGold(int findWhatDateType) {
        SQLiteDatabase db = getReadableDatabase();
        CalendarDatas today = new CalendarDatas();
        int findYear = today.cYear;
        int findMonth = today.cMonth;
        int findDate = today.cdate;
        int findWeekOfYear = today.weekOfYear;

        Cursor cursor = db.rawQuery("SELECT * FROM database", null);
        int result = 0;

        while (cursor.moveToNext()) {
            int dbYear = cursor.getInt(cursor.getColumnIndex("year"));
            int dbMonth = cursor.getInt(cursor.getColumnIndex("month"));
            int dbDate = cursor.getInt(cursor.getColumnIndex("date"));
            int dbWhatDateType = cursor.getInt(cursor.getColumnIndex("whatDateType"));
            int dbWeekOfYear = cursor.getInt(cursor.getColumnIndex("weekOfYear"));

            switch (findWhatDateType) {
                case FROM_TODAY:
                    if ((dbYear == findYear) && (dbMonth == findMonth) && (dbDate == findDate) && (dbWhatDateType == findWhatDateType)) {
                        result = cursor.getInt(cursor.getColumnIndex("bettingGold"));

                    }
                    break;
                case FROM_WEEK:
                    if ((dbYear == findYear) && (dbWeekOfYear == findWeekOfYear) && (dbWhatDateType == findWhatDateType)) {
                        result = cursor.getInt(cursor.getColumnIndex("bettingGold"));
                    }
                    break;
                case FROM_MONTH:
                    if ((dbYear == findYear) && (dbMonth == findMonth) && (dbWhatDateType == findWhatDateType)) {
                        result = cursor.getInt(cursor.getColumnIndex("bettingGold"));
                    }
                    break;
            }

        }
        cursor.close();
        return result;
    }

    public boolean hasGoal(int findWhatDateType) {
        SQLiteDatabase db = getReadableDatabase();
        CalendarDatas today = new CalendarDatas();
        int findYear = today.cYear;
        int findMonth = today.cMonth;
        int findDate = today.cdate;
        int findWeekOfYear = today.weekOfYear;

        Cursor cursor = db.rawQuery("SELECT * FROM database", null);
        boolean result = false;


        while (cursor.moveToNext()) {
            int dbYear = cursor.getInt(cursor.getColumnIndex("year"));
            int dbMonth = cursor.getInt(cursor.getColumnIndex("month"));
            int dbDate = cursor.getInt(cursor.getColumnIndex("date"));
            int dbWhatDateType = cursor.getInt(cursor.getColumnIndex("whatDateType"));
            int dbWeekOfYear = cursor.getInt(cursor.getColumnIndex("weekOfYear"));

            switch (findWhatDateType) {
                case FROM_TODAY:
                    if ((dbYear == findYear) && (dbMonth == findMonth) && (dbDate == findDate) && (dbWhatDateType == findWhatDateType)) {
                        result = true;
                        break;
                    } else {
                        result = false;
                    }
                    break;
                case FROM_WEEK:
                    if ((dbYear == findYear) && (dbWeekOfYear == findWeekOfYear) && (dbWhatDateType == findWhatDateType)) {
                        result = true;
                        break;
                    } else {
                        result = false;
                    }
                    break;
                case FROM_MONTH:
                    if ((dbYear == findYear) && (dbMonth == findMonth) && (dbWhatDateType == findWhatDateType)) {
                        result = true;
                        break;
                    } else {
                        result = false;
                    }
                    break;
            }

        }
        cursor.close();
        return result;
    }


    public String getType(int findWhatDateType) {
        SQLiteDatabase db = getReadableDatabase();
        CalendarDatas today = new CalendarDatas();
        int findYear = today.cYear;
        int findMonth = today.cMonth;
        int findDate = today.cdate;
        int findWeekOfYear = today.weekOfYear;

        Cursor cursor = db.rawQuery("SELECT * FROM database", null);
        String result = "";

        while (cursor.moveToNext()) {
            int dbYear = cursor.getInt(cursor.getColumnIndex("year"));
            int dbMonth = cursor.getInt(cursor.getColumnIndex("month"));
            int dbDate = cursor.getInt(cursor.getColumnIndex("date"));
            int dbWhatDateType = cursor.getInt(cursor.getColumnIndex("whatDateType"));
            int dbWeekOfYear = cursor.getInt(cursor.getColumnIndex("weekOfYear"));

            switch (findWhatDateType) {
                case FROM_TODAY:
                    if ((dbYear == findYear) && (dbMonth == findMonth) && (dbDate == findDate) && (dbWhatDateType == findWhatDateType)) {
                        result = cursor.getString(cursor.getColumnIndex("type"));

                    }
                    break;
                case FROM_WEEK:
                    if ((dbYear == findYear) && (dbWeekOfYear == findWeekOfYear) && (dbWhatDateType == findWhatDateType)) {
                        result = cursor.getString(cursor.getColumnIndex("type"));
                    }
                    break;
                case FROM_MONTH:
                    if ((dbYear == findYear) && (dbMonth == findMonth) && (dbWhatDateType == findWhatDateType)) {
                        result = cursor.getString(cursor.getColumnIndex("type"));
                    }
                    break;
            }

        }
        cursor.close();
        return result;
    }

    public String getUnit(int findWhatDateType) {
        SQLiteDatabase db = getReadableDatabase();
        CalendarDatas today = new CalendarDatas();
        int findYear = today.cYear;
        int findMonth = today.cMonth;
        int findDate = today.cdate;
        int findWeekOfYear = today.weekOfYear;

        Cursor cursor = db.rawQuery("SELECT * FROM database", null);
        String result = "";


        while (cursor.moveToNext()) {
            int dbYear = cursor.getInt(cursor.getColumnIndex("year"));
            int dbMonth = cursor.getInt(cursor.getColumnIndex("month"));
            int dbDate = cursor.getInt(cursor.getColumnIndex("date"));
            int dbWhatDateType = cursor.getInt(cursor.getColumnIndex("whatDateType"));
            int dbWeekOfYear = cursor.getInt(cursor.getColumnIndex("weekOfYear"));

            switch (findWhatDateType) {
                case FROM_TODAY:
                    if ((dbYear == findYear) && (dbMonth == findMonth) && (dbDate == findDate) && (dbWhatDateType == findWhatDateType)) {
                        result = cursor.getString(cursor.getColumnIndex("unit"));

                    }
                    break;
                case FROM_WEEK:
                    if ((dbYear == findYear) && (dbWeekOfYear == findWeekOfYear) && (dbWhatDateType == findWhatDateType)) {
                        result = cursor.getString(cursor.getColumnIndex("unit"));
                    }
                    break;
                case FROM_MONTH:
                    if ((dbYear == findYear) && (dbMonth == findMonth) && (dbWhatDateType == findWhatDateType)) {
                        result = cursor.getString(cursor.getColumnIndex("unit"));
                    }
            }


        }
        cursor.close();
        return result;
    }

    public void delete(int findWhatDateType) {
        SQLiteDatabase db = getReadableDatabase();
        CalendarDatas today = new CalendarDatas();
        int findYear = today.cYear;
        int findMonth = today.cMonth;
        int findDate = today.cdate;
        int findWeekOfYear = today.weekOfYear;
        String str = "";

        switch (findWhatDateType) {
            case FROM_TODAY:
                str = "DELETE FROM database WHERE year = " + findYear + " AND month = " + findMonth + " AND date = " + findDate + " AND whatDateType=" + findWhatDateType + ";";
                db.execSQL(str);
                break;
            case FROM_WEEK:
                str = "DELETE FROM database WHERE year = " + findYear + " AND month = " + findMonth + " AND weekOfYear = " + findWeekOfYear + " AND whatDateType=" + findWhatDateType + ";";
                db.execSQL(str);
                break;
            case FROM_MONTH:
                str = "DELETE FROM database WHERE year = " + findYear + " AND month = " + findMonth + " AND whatDateType=" + findWhatDateType + ";";
                db.execSQL(str);
                break;

        }
    }

    public ListViewData getPreviousListViewData(int position) {
        ListViewData listViewData = new ListViewData();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM database", null);
        cursor.move(position);

        listViewData.lvSuccess = cursor.getInt(cursor.getColumnIndex("isSuccess"));
        listViewData.lvDateType = cursor.getInt(cursor.getColumnIndex("whatDateType"));

        int year = cursor.getInt(cursor.getColumnIndex("year"));
        int month = cursor.getInt(cursor.getColumnIndex("month")) + 1;
        int date = cursor.getInt(cursor.getColumnIndex("date"));

        listViewData.lvDate = "" + year + "/" + "" + month + "/" + "" + date;
        listViewData.lvgoal = cursor.getString(cursor.getColumnIndex("goal"));
        listViewData.lvBettingGold = cursor.getInt(cursor.getColumnIndex("bettingGold"));
        listViewData.lvCurrentAmount = cursor.getInt(cursor.getColumnIndex("currentAmount"));
        listViewData.lvGoalAmount = cursor.getInt(cursor.getColumnIndex("amount"));

        cursor.close();
        return listViewData;
    }

    public int getLastPosition() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM database", null);
        int a;
        cursor.moveToLast();
        a = cursor.getPosition() + 1;

        cursor.close();
        return a;
    }

    public boolean isEmptyDB() {
        SQLiteDatabase db = getWritableDatabase();
        String count = "SELECT count(*) FROM database";
        Cursor cursor = db.rawQuery(count, null);

        boolean result;
        cursor.moveToFirst();
        int icount = cursor.getInt(0);

        if (icount > 0)
            result = false;
        else
            result = true;

        cursor.close();
        return result;
    }

    public Boolean isNotWorkFailToday() {
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM database", null);
        Boolean isNotWork = false;
        int count = 0;
        cursor.moveToLast();
        while (cursor.moveToPrevious()) {
            int dataType = cursor.getInt(cursor.getColumnIndex("whatDateType"));
            int successType = cursor.getInt(cursor.getColumnIndex("isSuccess"));
            if (dataType == FROM_TODAY && successType == 2) {
                count++;
                if (count > 1) {
                    isNotWork = true;
                    break;
                }
            }
        }
        cursor.close();
        return isNotWork;
    }

    public int getFirstTodayDoingIndex() {
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM database", null);
        int firstTodayPosition = 0;
        cursor.moveToLast();
        while (cursor.moveToPrevious()) {
            int dataType = cursor.getInt(cursor.getColumnIndex("whatDateType"));
            int successType = cursor.getInt(cursor.getColumnIndex("isSuccess"));
            if (dataType == FROM_TODAY && successType == 2) {
                firstTodayPosition = cursor.getPosition();
                break;
            }
        }
        cursor.close();
        return firstTodayPosition;
    }

    public void setDBFailToday(int pos) {
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM database", null);
        String str = "UPDATE database SET isSuccess=" + 3 + " WHERE _id<" + pos + " AND whatDateType=" + FROM_TODAY + " AND isSuccess=" + 2 + ";";
        db.execSQL(str);
        cursor.close();
    }
}