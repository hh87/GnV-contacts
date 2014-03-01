package com.xiang.mycontacts;

/**
 * 创建数据库中表的sql语句
 * @author HH
 *	
 */
public class DBhelperCreateString {
	public static final String mycontacts = "mycontacts";
	public static final String KEY_ID = "_id";
	public static final String KEY_NAME = "name";
	public static final String KEY_PHONENUMBER = "phonenumber";

	public static String Create_TB()
	{
		return "CREATE TABLE IF NOT EXISTS " + mycontacts + "("
				+ KEY_ID + " integer primary key autoincrement,"
				+ KEY_NAME + " text,"
				+ KEY_PHONENUMBER + " text)";
	}
}
