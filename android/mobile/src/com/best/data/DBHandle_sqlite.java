////////////////////////////////////////////////
//
// License: GPLv3
//
//

package com.best.data;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;


public class DBHandle_sqlite {

	
	// Database fields
	private static SQLiteDatabase database;
	private static BEST_DB dbHelper;
	public DBHandle_sqlite(Context context) {
		dbHelper = new BEST_DB(context);
	}

	// public void open() throws SQLException {
		// database = dbHelper.getWritableDatabase();
	// }
	
	private static void openForRead() throws SQLException {
		close();
		database = dbHelper.getReadableDatabase();
	}

	public static void close() {
		if( database != null  && database.isOpen() )
			database.close();
	}

	public String[][] execSelectQuery(String query, int columns) {

		Cursor cursor = null;
		List<String[]> result = new ArrayList<String[]>();
		try{
			openForRead();
			//database.open();
			System.out.println("DBHANDLE QUERY: " +query);
			cursor = database.rawQuery( query, null );
		
			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				String[] _row = new String[columns];
				for(int i = 0; i < columns; i++)
				{
					_row[i] = cursor.getString(i);
				}
				result.add(_row);
				cursor.moveToNext();
			}
		}catch( Exception e ){	System.out.println("DBHANDLE EXCEP: " +e);e.printStackTrace();}
		finally{ if( cursor != null )cursor.close(); close(); }
        System.out.println("result.size() "+result.size());
		return  (result.toArray( new String[ result.size() ][])) ;
	
	}
	
}