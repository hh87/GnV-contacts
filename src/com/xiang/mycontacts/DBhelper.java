package com.xiang.mycontacts;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 数据库建立，继承自SQLiteOpenHelper
 * @author HH
 *
 */
public class DBhelper extends SQLiteOpenHelper{
	public static final String APP_DB_NAME = "Contacts.db";
	public static final Integer APP_DB_VERSION = 1;

	public DBhelper(Context c){
		this(c,APP_DB_VERSION);
	}
	public DBhelper(Context c , Integer version){
		this(c, APP_DB_NAME, null, version);
	}
	public DBhelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(DBhelperCreateString.Create_TB());
		
		
	}
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
				
	}	

}
