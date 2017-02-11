package com.taek_aaa.goalsnowball.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.taek_aaa.goalsnowball.data.CommonData.FROM_MONTH;
import static com.taek_aaa.goalsnowball.data.CommonData.FROM_TODAY;


/**
 * Created by taek_aaa on 2017. 1. 11..
 */


/**is success 가 0이면 저장 안함 1이면 성공 2이면 하는중 3이면 실패**/
public class DBManager extends SQLiteOpenHelper {

    public DBManager(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 새로운 Table 생성
        db.execSQL("CREATE TABLE database (_id INTEGER PRIMARY KEY AUTOINCREMENT, year INTEGER, month INTEGER, date INTEGER, weekOfYear INTEGER, whatDateType INTEGER , goal TEXT, type TEXT, amount INTEGER, unit TEXT, currentAmount INTEGER, bettingGold INTEGER, isSuccess INTEGER);");
        //db.execSQL("INSERT INTO database VALUES(NULL, " + 0 + ", " + 0 + ", " + 0 + ", " + 0 + ", " + 0 + ", '" + "" + "',  '" +"" + "', " + 0 + ", '" + "" + "', " + 0 + ", " + 0 + "," + 0 + " );");

    }

    /**
     * DB버전을 높여서 DB삭제하는 효과를 줌
     **/
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table database;");
        onCreate(db);
    }

    public void deleteAll(SQLiteDatabase db){
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

        db.close();
    }

    public void setCurrentAmount(int findWhatDateType, int setAmount) {
        SQLiteDatabase db = getReadableDatabase();
        CalendarDatas today = new CalendarDatas();
        int findYear = today.cYear;
        int findMonth = today.cMonth;
        int findDate = today.cdate;
        int findWeekOfyear = today.weekOfYear;

        if (findWhatDateType == FROM_TODAY) {
            String str = "UPDATE database SET currentAmount=" + setAmount + " WHERE year = " + findYear + " AND month = " + findMonth + " AND date = " + findDate + " AND whatDateType=" + findWhatDateType + ";";
            db.execSQL(str);
        } else if (findWhatDateType == FROM_MONTH) {
            String str = "UPDATE database SET currentAmount=" + setAmount + " WHERE year = " + findYear + " AND month = " + findMonth + " AND whatDateType=" + findWhatDateType + ";";
            db.execSQL(str);
        } else {
            String str = "UPDATE database SET currentAmount=" + setAmount + " WHERE year =" + findYear + " AND month = " + findMonth + " AND weekOfYear = " + findWeekOfyear + " AND whatDateType=" + findWhatDateType + ";";
            db.execSQL(str);
        }
        db.close();
    }


    public void setIsSuccess(int findWhatDateType, int status) {
        SQLiteDatabase db = getReadableDatabase();
        CalendarDatas today = new CalendarDatas();
        int findYear = today.cYear;
        int findMonth = today.cMonth;
        int findDate = today.cdate;
        int findWeekOfYear = today.weekOfYear;

        if (findWhatDateType == FROM_TODAY) {
            String str = "UPDATE database SET isSuccess=" + status + " WHERE year = " + findYear + " AND month = " + findMonth + " AND date = " + findDate + " AND whatDateType=" + findWhatDateType + ";";
            db.execSQL(str);
        } else if (findWhatDateType == FROM_MONTH) {
            String str = "UPDATE database SET isSuccess=" + status + " WHERE year = " + findYear + " AND month = " + findMonth + " AND whatDateType=" + findWhatDateType + ";";
            db.execSQL(str);
        } else {
            String str = "UPDATE database SET isSuccess=" + status + " WHERE year =" + findYear + " AND month = " + findMonth + " AND weekOfYear = " + findWeekOfYear + " AND whatDateType=" + findWhatDateType + ";";
            db.execSQL(str);
        }
        db.close();
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
        if (findWhatDateType == FROM_TODAY) {
            while (cursor.moveToNext()) {
                int dbYear = cursor.getInt(cursor.getColumnIndex("year"));
                int dbMonth = cursor.getInt(cursor.getColumnIndex("month"));
                int dbDate = cursor.getInt(cursor.getColumnIndex("date"));
                int dbWhatDateType = cursor.getInt(cursor.getColumnIndex("whatDateType"));
                if ((dbYear == findYear) && (dbMonth == findMonth) && (dbDate == findDate) && (dbWhatDateType == findWhatDateType)) {
                    result = cursor.getInt(cursor.getColumnIndex("currentAmount"));
                }
            }
        } else if (findWhatDateType == FROM_MONTH) {
            while (cursor.moveToNext()) {
                int dbYear = cursor.getInt(cursor.getColumnIndex("year"));
                int dbMonth = cursor.getInt(cursor.getColumnIndex("month"));
                int dbWhatDateType = cursor.getInt(cursor.getColumnIndex("whatDateType"));
                if ((dbYear == findYear) && (dbMonth == findMonth) && (dbWhatDateType == findWhatDateType)) {
                    result = cursor.getInt(cursor.getColumnIndex("currentAmount"));
                }
            }
        } else {
            while (cursor.moveToNext()) {
                int dbYear = cursor.getInt(cursor.getColumnIndex("year"));
                int dbWhatDateType = cursor.getInt(cursor.getColumnIndex("whatDateType"));
                int dbWeekOfYear = cursor.getInt(cursor.getColumnIndex("weekOfYear"));
                if ((dbYear == findYear) && (dbWeekOfYear == findWeekOfYear) && (dbWhatDateType == findWhatDateType)) {
                    result = cursor.getInt(cursor.getColumnIndex("currentAmount"));
                }
            }
        }

        db.close();
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
        if (findWhatDateType == FROM_TODAY) {
            while (cursor.moveToNext()) {
                int dbYear = cursor.getInt(cursor.getColumnIndex("year"));
                int dbMonth = cursor.getInt(cursor.getColumnIndex("month"));
                int dbDate = cursor.getInt(cursor.getColumnIndex("date"));
                int dbWhatDateType = cursor.getInt(cursor.getColumnIndex("whatDateType"));
                if ((dbYear == findYear) && (dbMonth == findMonth) && (dbDate == findDate) && (dbWhatDateType == findWhatDateType)) {
                    result = cursor.getInt(cursor.getColumnIndex("isSuccess"));
                }
            }
        } else if (findWhatDateType == FROM_MONTH) {
            while (cursor.moveToNext()) {
                int dbYear = cursor.getInt(cursor.getColumnIndex("year"));
                int dbMonth = cursor.getInt(cursor.getColumnIndex("month"));
                int dbWhatDateType = cursor.getInt(cursor.getColumnIndex("whatDateType"));
                if ((dbYear == findYear) && (dbMonth == findMonth) && (dbWhatDateType == findWhatDateType)) {
                    result = cursor.getInt(cursor.getColumnIndex("isSuccess"));
                }
            }
        } else {
            while (cursor.moveToNext()) {
                int dbYear = cursor.getInt(cursor.getColumnIndex("year"));
                int dbWhatDateType = cursor.getInt(cursor.getColumnIndex("whatDateType"));
                int dbWeekOfYear = cursor.getInt(cursor.getColumnIndex("weekOfYear"));
                if ((dbYear == findYear) && (dbWeekOfYear == findWeekOfYear) && (dbWhatDateType == findWhatDateType)) {
                    result = cursor.getInt(cursor.getColumnIndex("isSuccess"));
                }
            }
        }
        db.close();
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
        if (findWhatDateType == FROM_TODAY) {
            while (cursor.moveToNext()) {
                int dbYear = cursor.getInt(cursor.getColumnIndex("year"));
                int dbMonth = cursor.getInt(cursor.getColumnIndex("month"));
                int dbDate = cursor.getInt(cursor.getColumnIndex("date"));
                int dbWhatDateType = cursor.getInt(cursor.getColumnIndex("whatDateType"));
                if ((dbYear == findYear) && (dbMonth == findMonth) && (dbDate == findDate) && (dbWhatDateType == findWhatDateType)) {
                    result = cursor.getString(cursor.getColumnIndex("goal"));

                }
            }
        } else if (findWhatDateType == FROM_MONTH) {
            while (cursor.moveToNext()) {
                int dbYear = cursor.getInt(cursor.getColumnIndex("year"));
                int dbMonth = cursor.getInt(cursor.getColumnIndex("month"));
                int dbWhatDateType = cursor.getInt(cursor.getColumnIndex("whatDateType"));
                if ((dbYear == findYear) && (dbMonth == findMonth) && (dbWhatDateType == findWhatDateType)) {
                    result = cursor.getString(cursor.getColumnIndex("goal"));

                }
            }
        } else {
            while (cursor.moveToNext()) {
                int dbYear = cursor.getInt(cursor.getColumnIndex("year"));
                int dbWhatDateType = cursor.getInt(cursor.getColumnIndex("whatDateType"));
                int dbWeekOfYear = cursor.getInt(cursor.getColumnIndex("weekOfYear"));
                if ((dbYear == findYear) && (dbWeekOfYear == findWeekOfYear) && (dbWhatDateType == findWhatDateType)) {
                    result = cursor.getString(cursor.getColumnIndex("goal"));
                }
            }
        }
        db.close();
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
        if (findWhatDateType == FROM_TODAY) {
            while (cursor.moveToNext()) {
                int dbYear = cursor.getInt(cursor.getColumnIndex("year"));
                int dbMonth = cursor.getInt(cursor.getColumnIndex("month"));
                int dbDate = cursor.getInt(cursor.getColumnIndex("date"));
                int dbWhatDateType = cursor.getInt(cursor.getColumnIndex("whatDateType"));
                if ((dbYear == findYear) && (dbMonth == findMonth) && (dbDate == findDate) && (dbWhatDateType == findWhatDateType)) {
                    result = cursor.getInt(cursor.getColumnIndex("amount"));
                }

            }
        } else if (findWhatDateType == FROM_MONTH) {
            while (cursor.moveToNext()) {
                int dbYear = cursor.getInt(cursor.getColumnIndex("year"));
                int dbMonth = cursor.getInt(cursor.getColumnIndex("month"));
                int dbWhatDateType = cursor.getInt(cursor.getColumnIndex("whatDateType"));
                if ((dbYear == findYear) && (dbMonth == findMonth) && (dbWhatDateType == findWhatDateType)) {
                    result = cursor.getInt(cursor.getColumnIndex("amount"));
                }
            }
        } else {
            while (cursor.moveToNext()) {
                int dbYear = cursor.getInt(cursor.getColumnIndex("year"));
                int dbWhatDateType = cursor.getInt(cursor.getColumnIndex("whatDateType"));
                int dbWeekOfYear = cursor.getInt(cursor.getColumnIndex("weekOfYear"));
                if ((dbYear == findYear) && (dbWeekOfYear == findWeekOfYear) && (dbWhatDateType == findWhatDateType)) {
                    result = cursor.getInt(cursor.getColumnIndex("amount"));
                }
            }
        }
        db.close();
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
        if (findWhatDateType == FROM_TODAY) {
            while (cursor.moveToNext()) {
                int dbYear = cursor.getInt(cursor.getColumnIndex("year"));
                int dbMonth = cursor.getInt(cursor.getColumnIndex("month"));
                int dbDate = cursor.getInt(cursor.getColumnIndex("date"));
                int dbWhatDateType = cursor.getInt(cursor.getColumnIndex("whatDateType"));
                if ((dbYear == findYear) && (dbMonth == findMonth) && (dbDate == findDate) && (dbWhatDateType == findWhatDateType)) {
                    result = cursor.getInt(cursor.getColumnIndex("bettingGold"));

                }

            }
        } else if (findWhatDateType == FROM_MONTH) {
            while (cursor.moveToNext()) {
                int dbYear = cursor.getInt(cursor.getColumnIndex("year"));
                int dbMonth = cursor.getInt(cursor.getColumnIndex("month"));
                int dbWhatDateType = cursor.getInt(cursor.getColumnIndex("whatDateType"));
                if ((dbYear == findYear) && (dbMonth == findMonth) && (dbWhatDateType == findWhatDateType)) {
                    result = cursor.getInt(cursor.getColumnIndex("bettingGold"));
                }
            }
        } else {
            while (cursor.moveToNext()) {
                int dbYear = cursor.getInt(cursor.getColumnIndex("year"));
                int dbWhatDateType = cursor.getInt(cursor.getColumnIndex("whatDateType"));
                int dbWeekOfYear = cursor.getInt(cursor.getColumnIndex("weekOfYear"));
                if ((dbYear == findYear) && (dbWeekOfYear == findWeekOfYear) && (dbWhatDateType == findWhatDateType)) {
                    result = cursor.getInt(cursor.getColumnIndex("bettingGold"));
                }
            }
        }
        db.close();
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
        if (findWhatDateType == FROM_TODAY) {
            while (cursor.moveToNext()) {
                int dbYear = cursor.getInt(cursor.getColumnIndex("year"));
                int dbMonth = cursor.getInt(cursor.getColumnIndex("month"));
                int dbDate = cursor.getInt(cursor.getColumnIndex("date"));
                int dbWhatDateType = cursor.getInt(cursor.getColumnIndex("whatDateType"));
                if ((dbYear == findYear) && (dbMonth == findMonth) && (dbDate == findDate) && (dbWhatDateType == findWhatDateType)) {
                    result = true;
                    break;
                } else {
                    result = false;
                }
            }
        } else if (findWhatDateType == FROM_MONTH) {
            while (cursor.moveToNext()) {
                int dbYear = cursor.getInt(cursor.getColumnIndex("year"));
                int dbMonth = cursor.getInt(cursor.getColumnIndex("month"));
                int dbWhatDateType = cursor.getInt(cursor.getColumnIndex("whatDateType"));
                if ((dbYear == findYear) && (dbMonth == findMonth) && (dbWhatDateType == findWhatDateType)) {
                    result = true;
                    break;
                } else {
                    result = false;
                }
            }
        } else {
            while (cursor.moveToNext()) {
                int dbYear = cursor.getInt(cursor.getColumnIndex("year"));
                int dbWhatDateType = cursor.getInt(cursor.getColumnIndex("whatDateType"));
                int dbWeekOfYear = cursor.getInt(cursor.getColumnIndex("weekOfYear"));
                if ((dbYear == findYear) && (dbWeekOfYear == findWeekOfYear) && (dbWhatDateType == findWhatDateType)) {
                    result = true;
                    break;
                } else {
                    result = false;
                }
            }
        }
        db.close();
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
        if (findWhatDateType == FROM_TODAY) {
            while (cursor.moveToNext()) {
                int dbYear = cursor.getInt(cursor.getColumnIndex("year"));
                int dbMonth = cursor.getInt(cursor.getColumnIndex("month"));
                int dbDate = cursor.getInt(cursor.getColumnIndex("date"));
                int dbWhatDateType = cursor.getInt(cursor.getColumnIndex("whatDateType"));
                if ((dbYear == findYear) && (dbMonth == findMonth) && (dbDate == findDate) && (dbWhatDateType == findWhatDateType)) {
                    result = cursor.getString(cursor.getColumnIndex("type"));

                }

            }
        } else if (findWhatDateType == FROM_MONTH) {
            while (cursor.moveToNext()) {
                int dbYear = cursor.getInt(cursor.getColumnIndex("year"));
                int dbMonth = cursor.getInt(cursor.getColumnIndex("month"));
                int dbWhatDateType = cursor.getInt(cursor.getColumnIndex("whatDateType"));
                if ((dbYear == findYear) && (dbMonth == findMonth) && (dbWhatDateType == findWhatDateType)) {
                    result = cursor.getString(cursor.getColumnIndex("type"));
                }
            }
        } else {
            while (cursor.moveToNext()) {
                int dbYear = cursor.getInt(cursor.getColumnIndex("year"));
                int dbWhatDateType = cursor.getInt(cursor.getColumnIndex("whatDateType"));
                int dbWeekOfYear = cursor.getInt(cursor.getColumnIndex("weekOfYear"));
                if ((dbYear == findYear) && (dbWeekOfYear == findWeekOfYear) && (dbWhatDateType == findWhatDateType)) {
                    result = cursor.getString(cursor.getColumnIndex("type"));
                }
            }
        }
        db.close();
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
        if (findWhatDateType == FROM_TODAY) {
            while (cursor.moveToNext()) {
                int dbYear = cursor.getInt(cursor.getColumnIndex("year"));
                int dbMonth = cursor.getInt(cursor.getColumnIndex("month"));
                int dbDate = cursor.getInt(cursor.getColumnIndex("date"));
                int dbWhatDateType = cursor.getInt(cursor.getColumnIndex("whatDateType"));
                if ((dbYear == findYear) && (dbMonth == findMonth) && (dbDate == findDate) && (dbWhatDateType == findWhatDateType)) {
                    result = cursor.getString(cursor.getColumnIndex("unit"));

                }

            }
        } else if (findWhatDateType == FROM_MONTH) {
            while (cursor.moveToNext()) {
                int dbYear = cursor.getInt(cursor.getColumnIndex("year"));
                int dbMonth = cursor.getInt(cursor.getColumnIndex("month"));
                int dbWhatDateType = cursor.getInt(cursor.getColumnIndex("whatDateType"));
                if ((dbYear == findYear) && (dbMonth == findMonth) && (dbWhatDateType == findWhatDateType)) {
                    result = cursor.getString(cursor.getColumnIndex("unit"));
                }

            }
        } else {
            while (cursor.moveToNext()) {
                int dbYear = cursor.getInt(cursor.getColumnIndex("year"));
                int dbWhatDateType = cursor.getInt(cursor.getColumnIndex("whatDateType"));
                int dbWeekOfYear = cursor.getInt(cursor.getColumnIndex("weekOfYear"));
                if ((dbYear == findYear) && (dbWeekOfYear == findWeekOfYear) && (dbWhatDateType == findWhatDateType)) {
                    result = cursor.getString(cursor.getColumnIndex("unit"));
                }

            }
        }
        db.close();
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

        if (findWhatDateType == FROM_TODAY) {
            String str = "DELETE FROM database WHERE year = " + findYear + " AND month = " + findMonth + " AND date = " + findDate + " AND whatDateType=" + findWhatDateType + ";";
            db.execSQL(str);
        } else if (findWhatDateType == FROM_MONTH) {
            String str = "DELETE FROM database WHERE year = " + findYear + " AND month = " + findMonth + " AND whatDateType=" + findWhatDateType + ";";
            db.execSQL(str);
        } else {
            String str = "DELETE FROM database WHERE year = " + findYear + " AND month = " + findMonth + " AND weekOfYear = " + findWeekOfYear + " AND whatDateType=" + findWhatDateType + ";";
            db.execSQL(str);
        }


        db.close();
    }

    public ListViewData getPreviousListViewData(int position){
        ListViewData listViewData = new ListViewData();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM database", null);
        cursor.move(position);

        listViewData.lvSuccess = cursor.getInt(cursor.getColumnIndex("isSuccess"));
        listViewData.lvDateType = cursor.getInt(cursor.getColumnIndex("whatDateType"));

        int year = cursor.getInt(cursor.getColumnIndex("year"));
        int month = cursor.getInt(cursor.getColumnIndex("month"))+1;
        int date = cursor.getInt(cursor.getColumnIndex("date"));

        listViewData.lvDate = ""+year+"/"+""+month+"/"+""+date;
        listViewData.lvgoal = cursor.getString(cursor.getColumnIndex("goal"));
        listViewData.lvBettingGold=cursor.getInt(cursor.getColumnIndex("bettingGold"));

        db.close();
        cursor.close();
        return listViewData;
    }

    public int getLastPosition(){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM database", null);
        int a;
        cursor.moveToLast();
        a = cursor.getPosition()+1;
        db.close();
        cursor.close();
        return a;
    }

    public boolean isEmptyDB(){
        SQLiteDatabase db = getWritableDatabase();
        String count = "SELECT count(*) FROM database";
        Cursor mcursor = db.rawQuery(count, null);

        boolean result;
        mcursor.moveToFirst();
        int icount = mcursor.getInt(0);

        if(icount>0)
            result=false;
        else
            result=true;

        return result;
    }

}