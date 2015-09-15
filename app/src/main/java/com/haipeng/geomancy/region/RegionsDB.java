package com.haipeng.geomancy.region;

import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class RegionsDB {

	static final String DATABASE_NAME = "ReDB.db";
	static final int DATABASE_VERSION = 1;

	static final String TABLE_PROVINCE = "table_province";
	static final String TABLE_CITY = "table_city";
	static final String TABLE_COUNTY = "table_county";

	static final String COLUMN_PROVINCE = "province";
	static final String COLUMN_CITY = "city";
	static final String COLUMN_COUNTY = "county";
//    static final String PRIMARYSTR  = "INTEGER PRIMARY KEY AUTOINCREMENT";
	RegionsDBHelper DBHelper;
	SQLiteDatabase write_db;
	final Context mContext;
	SQLiteDatabase read_db;
	
	static final String sql_province = "CREATE TABLE " + TABLE_PROVINCE + "("
			+ COLUMN_PROVINCE + " text" 
			+ ");";

	static final String sql_city = "CREATE TABLE " + TABLE_CITY + "("

			+ COLUMN_CITY + " text" +","
			+ COLUMN_PROVINCE + " text" 
			+");";

	static final String sql_county = "CREATE TABLE " + TABLE_COUNTY + "("
			+ COLUMN_COUNTY + " text"+"," 
			+ COLUMN_CITY + " text" 
			+ ");";
    
	static public boolean hasInsert ;
    SharedPreferences sp ;
	public RegionsDB(Context context) {
		mContext = context;
		sp = mContext.getSharedPreferences("hasNotInser", mContext.MODE_APPEND);
		hasInsert = sp.getBoolean("hasInsert", false);
		DBHelper = new RegionsDBHelper(mContext);
		read_db = DBHelper.getReadableDatabase();

		if (!hasInsert) {
			write_db = DBHelper.getWritableDatabase();
			RegionData rd = new RegionData();
			insertTableProvince(rd.provinces, write_db);
			insertTableCity(rd.getTableCity(), write_db);
			insertTableCounty(rd.getTableCounty(), write_db);
//            if(write_db!=null) {
//                write_db.close();
//                write_db = null;
//            }
            SharedPreferences.Editor editor = sp.edit();
			editor.putBoolean("hasInsert", true);
		    editor.commit();
		}
	}

	public int getCountTable(String tableName,SQLiteDatabase read_db)
	{
        if(read_db==null)
        {
            read_db = DBHelper.getReadableDatabase();
        }
		String sql = "select count(*) from "+tableName;
		Cursor cr  = read_db.rawQuery(sql, null);

		int count = cr.getCount();
        if(read_db!=null) {
            read_db.close();
            read_db = null;
        }
            if(cr!=null)
            cr.close();
        return count;
	}

	public static class RegionsDBHelper extends SQLiteOpenHelper {
		public RegionsDBHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			// TODO Auto-generated method stub
		
			db.execSQL(sql_province);
			db.execSQL(sql_city);
			db.execSQL(sql_county);

		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub
             onCreate(db);
		}
	}

	public void insertTableProvince(String provinces[],SQLiteDatabase db) {
		
		// new Thread() {
		// //开一个线程给它防止堵塞
		// @Override
		// public void run() {
		try {
			ContentValues values = new ContentValues();
			db.beginTransaction();
			for (int i = 0; i < provinces.length; i++) {
				values.put(COLUMN_PROVINCE, provinces[i]);
				db.insert(TABLE_PROVINCE, null, values);
			}
			db.setTransactionSuccessful();
			db.endTransaction();

		} catch (Exception e) {
			e.printStackTrace();
			Log.d("tag", "sql insert province exception!");
		} finally {
//			db.close();
		}
		// }

		// }.start();

	}

	public void insertTableCity(final List<TableCity> tCity,final SQLiteDatabase db) {
	
		
				try {
					ContentValues values = new ContentValues();
					db.beginTransaction();
					for (int i = 0; i < tCity.size(); i++) {
						TableCity tc = new TableCity();
						tc = tCity.get(i);
						values.put(COLUMN_CITY, tc.getCity());
						values.put(COLUMN_PROVINCE, tc.getProvince());
						db.insert(TABLE_CITY, null, values);
					}
					db.setTransactionSuccessful();
					db.endTransaction();

				} catch (Exception e) {
					e.printStackTrace();
					Log.d("tag", "sql insert city exception!");
				} finally {
//					db.close();
				}


	}

	public void insertTableCounty(final List<TableCounty> tCounty,final SQLiteDatabase db) {

//		new Thread() {
//			// 开一个线程给它防止堵塞
//			@Override
//			public void run() {
				try {
					ContentValues values = new ContentValues();
					db.beginTransaction();
					for (int i = 0; i < tCounty.size(); i++) {
						TableCounty tc = new TableCounty();
						tc = tCounty.get(i);
						values.put(COLUMN_COUNTY, tc.getCounty());
						values.put(COLUMN_CITY, tc.getCity());
						db.insert(TABLE_COUNTY, null, values);

					}
					db.setTransactionSuccessful();
					db.endTransaction();

				} catch (Exception e) {
					e.printStackTrace();
					Log.d("tag", "sql insert county exception!");
				} finally {
//					db.close();
				}
//			}
//
//		}.start();

	}

	public String[] queryProvince() {
		String str[] = null;
		
		try {
            if(read_db==null)
            {
                read_db = DBHelper.getReadableDatabase();
            }
			Cursor c = read_db.rawQuery("SELECT * FROM " + TABLE_PROVINCE, null);
			str = new String[c.getCount()];
			int count = 0;
			if (c != null) {
				c.moveToFirst();
				do {
					str[count] = c.getString(c.getColumnIndex(COLUMN_PROVINCE));
					count++;
				} while (c.moveToNext());
			}

		} catch (Exception e) {
			e.printStackTrace();
			Log.d("tag", "sql query province exception!");
		}
if(read_db!=null) {
    read_db.close();
    read_db = null;
}
		return str;
	}

	public String[] queryCity(String province ) {
		String str[] = null;
	
		try {
            if(read_db==null)
            {
                read_db = DBHelper.getReadableDatabase();
            }
			Cursor c = read_db.rawQuery("SELECT "+COLUMN_CITY+" FROM " + TABLE_CITY
					+ " WHERE " +COLUMN_PROVINCE+" = '" + province+"'", null);
			str = new String[c.getCount()];
			int count = 0;
			if (c != null) {
				c.moveToFirst();
				do {
					str[count] = c.getString(c.getColumnIndex(COLUMN_CITY));
					count++;
				} while (c.moveToNext());
			}

		} catch (Exception e) {
			e.printStackTrace();
			Log.d("tag", "sql query province exception!");
		}
        if(read_db!=null) {
            read_db.close();
            read_db = null;
        }
            return str;
	}

	public String[] queryCounty(String city) {
		String str[] = null;
		try {
            if(read_db==null)
            {
                read_db = DBHelper.getReadableDatabase();
            }
			Cursor c = read_db.rawQuery("SELECT "+COLUMN_COUNTY+" FROM " + TABLE_COUNTY
					+ " WHERE "+COLUMN_CITY+" = '" + city+"'", null);
			str = new String[c.getCount()];
			int count = 0;
			if (c != null) {
				c.moveToFirst();
				do {
					str[count] = c.getString(c.getColumnIndex(COLUMN_COUNTY));
					count++;
				} while (c.moveToNext());
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.d("tag", "sql query province exception!");
		}

        if(read_db!=null) {
            read_db.close();
            read_db = null;
        }
            return str;
	}

}
