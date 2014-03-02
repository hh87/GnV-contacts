package com.xiang.mycontacts;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * methods to get Screen params 
 * @author xiang
 */
public class Util {
	/**
	 * get screen width
	 */
	public static int getScreenWidth(Context context) {
		return context.getResources().getDisplayMetrics().widthPixels;
	}

	/**
	 * get screen height
	 */
	public static int getScreenHeight(Context context) {
		return context.getResources().getDisplayMetrics().heightPixels;
	}

	/**
	 * get screen density
	 */
	public static float getScreenDensity(Context context) {
		return context.getResources().getDisplayMetrics().density;
	}

	/**
	 * dip to px
	 */
	public static int dip2px(Context context, float px) {
		final float scale = getScreenDensity(context);
		return (int) (px * scale + 0.5);
	}
	
	
}
