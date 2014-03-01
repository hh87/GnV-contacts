package com.xiang.mycontacts;



import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * 数据库服务类
 * 包含查询修改删除联系人等方法
 * @author HH
 *
 */
public class DBServices {

	private DBhelper openHelper;
	SQLiteDatabase db;
	private static DBServices mInstance;

	private DBServices(Context c){
		this.openHelper = new DBhelper(c);
		if(openHelper.getWritableDatabase().isOpen())
			openHelper.getWritableDatabase().close();
		db = openHelper.getWritableDatabase();
	}

	public static DBServices getInstance(Context context) {
		if(null == mInstance) {
			mInstance = new DBServices(context);
		}
		return mInstance;
	}

	public void closeDataBase() {
		if (null != openHelper) {
			openHelper.close();
		}
		if (null != db && db.isOpen()) {
			db.close();
		}
	}

	public boolean addContacter(contact info){
		ContentValues values = new ContentValues();
		if(info.getName()!="" && info.getPhonenumber()!=""){
			values.put(DBhelperCreateString.KEY_NAME, info.getName());
			values.put(DBhelperCreateString.KEY_PHONENUMBER, info.getPhonenumber());
			long idx = db.insert(DBhelperCreateString.mycontacts, null, values);
			
			return (idx==-1)?false:true;
		}
		else return false;
	}
	
	public boolean editContacter(contact info){
		ContentValues values = new ContentValues();
		values.put(DBhelperCreateString.KEY_NAME, info.getName());
		values.put(DBhelperCreateString.KEY_PHONENUMBER, info.getPhonenumber());
		String[] whereArgs={"" + info.getId()};
		long idx = db.update(DBhelperCreateString.mycontacts, values, "_id=?", whereArgs);
		if (idx==-1){
			idx = db.insert(DBhelperCreateString.mycontacts, null, values);
		}
		
		return (idx==-1)?false:true;
	}

	public boolean delContacter(contact info){
		String whereClause=DBhelperCreateString.KEY_ID + "=?";
		String[] whereArgs={"" + info.getId()};
		int idx = db.delete(DBhelperCreateString.mycontacts, whereClause, whereArgs);
		return (idx==-1)?false:true;
	}
	
	public List<contact> getcontacterList(){
		String sql="SELECT * FROM " + DBhelperCreateString.mycontacts + " ORDER BY name";
		int index;
		Cursor cur = db.rawQuery(sql, null);
		if(cur.getCount()==0){
			return null;
		}
		List<contact> mListInfo = new ArrayList<contact>();
		cur.moveToFirst();
		for(index=0;index<cur.getCount();index++){
			contact info = new contact();
			cur.moveToPosition(index);
			info.setId(cur.getInt((cur.getColumnIndex(DBhelperCreateString.KEY_ID))));
			info.setName(cur.getString(cur.getColumnIndex(DBhelperCreateString.KEY_NAME)));
			info.setPhonenumber(cur.getString(cur.getColumnIndex(DBhelperCreateString.KEY_PHONENUMBER)));
			mListInfo.add(info);
		}
		return mListInfo;
	}
}
