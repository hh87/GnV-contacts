package com.xiang.mycontacts;

import android.content.Context;
import android.graphics.drawable.Drawable;

/**
 * @author xiang
 *	���������������ڲ���������Ʊ����ͼ�꣩
 */
public class ActionItem {
	public Drawable mDrawable;
	public CharSequence mTitle;
	public int			id;
	
	public ActionItem(CharSequence title,int id){
		//this.mDrawable = drawable;
		this.mTitle = title;
	}
	
	public ActionItem(Context context, int titleId,int id){
		this.mTitle = context.getResources().getText(titleId);
		this.id = id;
		//this.mDrawable = context.getResources().getDrawable(drawableId);
	}
	
	public ActionItem(Context context, CharSequence title,int id) {
		this.mTitle = title;
		this.id = id;
		//this.mDrawable = context.getResources().getDrawable(drawableId);
	}
}
