package com.wdil.whendidilast;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class CounterSQLiteHelper extends SQLiteOpenHelper{

	 private static final String DROP_COMMAND = "DROP TABLE IF EXISTS ";
	private static final int DATABASE_VERSION = 1;
	 private static final String DATABASE_NAME = "wdil.db";
	 
	 public static final String WDIL_COUNTER_TABLE_NAME = "counters";
	 public static final String COLUMN_ID = "_id";
	 public static final String COLUMN_NAME = "name";
	 
	 public static final String WDIL_DATE_TABLE_NAME = "dates";
	 public static final String COLUMN_DATE = "date";
	 public static final String COLUMN_COUNTER_ID = "counter_id";
	 
	 public static final String WDIL_COUNTER_TABLE_CREATE =
	                "CREATE TABLE IF NOT EXISTS " + WDIL_COUNTER_TABLE_NAME + " (" +
	                COLUMN_ID + " integer primary key autoincrement, " +
	                COLUMN_NAME + " TEXT NOT NULL);";
	 
	 public static final String WDIL_DATE_TABLE_CREATE = 
			 "CREATE TABLE IF NOT EXISTS " + WDIL_DATE_TABLE_NAME + " (" +
			 COLUMN_DATE + " integer NOT NULL, " + 
			 COLUMN_COUNTER_ID + " integer NOT NULL,"	+	 
			 "FOREIGN KEY (" + COLUMN_COUNTER_ID + ") REFERENCES " + WDIL_COUNTER_TABLE_NAME + "("	 + COLUMN_ID + ")" +
			 " ON DELETE CASCADE);";

	
	public CounterSQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(WDIL_COUNTER_TABLE_CREATE);
		db.execSQL(WDIL_DATE_TABLE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		//TODO actually do table migration
		//Currently just destroys the table
	    db.execSQL(DROP_COMMAND + WDIL_COUNTER_TABLE_NAME);
	    db.execSQL(DROP_COMMAND + WDIL_DATE_TABLE_NAME);
	    onCreate(db);
		
	}

}
