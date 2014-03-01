package com.xiang.mycontacts;

import android.content.Context;
import android.graphics.drawable.Drawable;

/**
 * 下拉列表项
 * @author HH
 *
 */
public class ActionItem {
	public Drawable mDrawable;
	public CharSequence mTitle;
	public int			id;
	
	public ActionItem(CharSequence title,int id){
		this.mTitle = title;
	}
	
	public ActionItem(Context context, int titleId,int id){
		this.mTitle = context.getResources().getText(titleId);
		this.id = id;
	}
	
	public ActionItem(Context context, CharSequence title,int id) {
		this.mTitle = title;
		this.id = id;
	}
}
