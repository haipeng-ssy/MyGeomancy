package com.haipeng.geomancy.UserData;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class UserContentDB {

    static final String DATABASE_NAME = "UserDB.db";
    static final int DATABASE_VERSION = 1;

    static final String TABLE_USER = "table_user";

    static final String COLUMN_USERNAME = "username";
    static final String COLUMN_USERSEX = "usersex";
    static final String COLUMN_USERPROVICE = "userprovince";
    static final String COLUMN_USERCITY = "usercity";
    static final String COLUMN_USERCOUNTRY = "usercountry";
    static final String COLUMN_USERBIR = "userbir";
    static final String COLUMN_USERBIRLUAR = "userbirluar";
    static final String COLUMN_USERXIA = "userxia";
    static final String COLUMN_USERTIME = "usertime";
    static final String COLUMN_HADPAID = "usertime";
    static final String COLUMN_HADSERVICE = "usertime";

    //    static final String PRIMARYSTR  = "INTEGER PRIMARY KEY AUTOINCREMENT";
    UserDBHelper DBHelper;
    SQLiteDatabase write_db;
    final Context mContext;
    SQLiteDatabase read_db;

    static final String sql_user = "CREATE TABLE " + TABLE_USER + "("
            + COLUMN_USERNAME + " text"
            + COLUMN_USERSEX + " text"
            + COLUMN_USERPROVICE + " text"
            + COLUMN_USERCITY + " text"
            + COLUMN_USERCOUNTRY + " text"
            + COLUMN_USERBIR + " text"
            + COLUMN_USERBIRLUAR + " text"
            + COLUMN_USERXIA + " text"
            + COLUMN_USERTIME + " text"
            + COLUMN_HADPAID + " text"
            + COLUMN_HADSERVICE + " text"
            + ");";

    static public boolean hasInsert;

    public UserContentDB(Context context) {
        mContext = context;
        DBHelper = new UserDBHelper(mContext);
        read_db = DBHelper.getReadableDatabase();
        write_db = DBHelper.getWritableDatabase();

    }


    public static class UserDBHelper extends SQLiteOpenHelper {
        public UserDBHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            // TODO Auto-generated constructor stub
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            // TODO Auto-generated method stub
            db.execSQL(sql_user);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // TODO Auto-generated method stub
            onCreate(db);
        }
    }

    public void insertUserInfoByStr(String columnName,String columnStr) {

        try {
            ContentValues values = new ContentValues();

            write_db.beginTransaction();
            values.put(columnName, columnStr);
            write_db.insert(TABLE_USER, null, values);
            write_db.setTransactionSuccessful();
            write_db.endTransaction();

        } catch (Exception e) {
            e.printStackTrace();
            Log.d("tag", "sql insert province exception!");
        } finally {
//			db.close();
        }

    }
    public String getUserInfoByStr(String columnStr) {
        if (read_db == null) {
            read_db = DBHelper.getReadableDatabase();
        }
        String str = "";
        String sql = "select count(*) from " + TABLE_USER + " where id = " + columnStr + ";";
        Cursor cr = read_db.rawQuery(sql, null);
        if (cr != null)
            str = cr.getString(0);
        if (read_db != null) {
            read_db.close();
            read_db = null;
        }
        if (cr != null)
            cr.close();
        return str;
    }
    public void deleteTableUser(){
        String sql = "delete * from "+TABLE_USER+";";
        write_db.execSQL(sql);
    }
}