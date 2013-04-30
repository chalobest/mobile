////////////////////////////////////////////////
//
// License: GPLv3
//
//

package com.best.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class BEST_DB extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "chalobest_5"; // get stored in /data/data/com.efb.ui/databases/efb.db
	private static final int DATABASE_VERSION = 2;

	// Database creation sql statement

	public BEST_DB(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		System.out.println("BEST_DB: onCreate Called");	
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		System.out.println("BEST_DB: onUpgrade Called");
		onCreate(db);
	}

}