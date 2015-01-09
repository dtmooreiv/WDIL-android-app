package com.wdil.whendidilast;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class CounterDataSource {

	private CounterSQLiteHelper dbHelper;
	private SQLiteDatabase database;
	private String[] counterColumns = { CounterSQLiteHelper.COLUMN_ID,
			CounterSQLiteHelper.COLUMN_NAME };
	private String[] dateColumns = {CounterSQLiteHelper.COLUMN_DATE,
			CounterSQLiteHelper.COLUMN_COUNTER_ID};

	public CounterDataSource(Context context) {
		dbHelper = new CounterSQLiteHelper(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	public Counter createCounter(String name) {
		ContentValues values = new ContentValues();
		values.put(CounterSQLiteHelper.COLUMN_NAME, name);
		long insertId = database.insert(CounterSQLiteHelper.WDIL_COUNTER_TABLE_NAME,
				null, values);
		Cursor cursor = database.query(CounterSQLiteHelper.WDIL_COUNTER_TABLE_NAME,
				counterColumns, CounterSQLiteHelper.COLUMN_ID + " = " + insertId,
				null, null, null, null);
		cursor.moveToFirst();
		Counter newCounter = cursorToCounter(cursor);
		cursor.close();
		return newCounter;
	}
	
	public void addDateToCounter(Counter counter, DateTime date) {
		ContentValues cv = new ContentValues();
		cv.put(CounterSQLiteHelper.COLUMN_DATE, date.getMillis()/1000);
		cv.put(CounterSQLiteHelper.COLUMN_COUNTER_ID, counter.getId());
		long rowId = database.insert(CounterSQLiteHelper.WDIL_DATE_TABLE_NAME, null, cv);
		counter.addDate(date);
		Log.d("Counter data source", "Just inserted: " + date.getMillis()/1000 + " for id:" + counter.getId());
	}

    public void deleteDateFromCounter(Counter counter, DateTime date) {
        Long id = counter.getId();
        long dateLong = date.getMillis()/1000;
        String whereClause = CounterSQLiteHelper.COLUMN_COUNTER_ID + " =? AND " + CounterSQLiteHelper.COLUMN_DATE + " =? ";
        int res = database.delete(CounterSQLiteHelper.WDIL_DATE_TABLE_NAME, whereClause, new String[]{id + "", dateLong + ""});
        counter.deleteCalendar(date);
        Log.d("Counter Data Source", "Deleting date: " + date.toString() + " from Counter: " + counter.toString());

    }

	public void deleteCounter(Counter counter) {
		long id = counter.getId();
		Log.d("Counter Data Source", "Deleting counter: " + counter.getName() + " with id: " + counter.getId());
		database.delete(CounterSQLiteHelper.WDIL_COUNTER_TABLE_NAME, CounterSQLiteHelper.COLUMN_ID
	        + " = " + id, null);
	}
	
	public List<Counter> getAllCounters() {
		List<Counter> counters = new ArrayList<Counter>();

	    Cursor cursor = database.query(CounterSQLiteHelper.WDIL_COUNTER_TABLE_NAME,
	        counterColumns, null, null, null, null, null);

	    cursor.moveToFirst();
	    while (!cursor.isAfterLast()) {
	      Counter counter = cursorToCounter(cursor);
	      
	      Cursor dateCursor = database.query(CounterSQLiteHelper.WDIL_DATE_TABLE_NAME, dateColumns, CounterSQLiteHelper.COLUMN_COUNTER_ID + " = " + counter.getId(), null, null, null, null);
	      dateCursor.moveToFirst();
	      while(!dateCursor.isAfterLast()) {
	    	  DateTime date = cursorToDateTime(dateCursor);
	    	  counter.addDate(date);
	    	  dateCursor.moveToNext();
	      }
	      dateCursor.close();
	      
	      counters.add(counter);
	      cursor.moveToNext();
	    }
	    // make sure to close the cursor
	    cursor.close();
	    return counters;
	}
	
	private Counter cursorToCounter(Cursor cursor) {
		Counter counter = new Counter(cursor.getString(1));
		counter.setId(cursor.getLong(0));
		return counter;
	}
	
	private DateTime cursorToDateTime(Cursor cursor) {
		long seconds = cursor.getInt(0);
		return new DateTime(seconds * 1000);
	}
}
