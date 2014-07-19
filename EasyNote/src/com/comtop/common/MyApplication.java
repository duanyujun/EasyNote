package com.comtop.common;

import android.app.Application;


public class MyApplication extends Application {
	
	private static MyApplication mInstance;

	/** 当前登陆用户 */
	private String userId;


	public static MyApplication getInstance() {
		return mInstance;
	}

	/**
	 * Android程序的真正入口
	 */
	@Override
	public void onCreate() {
		super.onCreate();
		mInstance = this;
	}

	/**
	 * 程序导致内存不足时，调用的方法
	 */
	@Override
	public void onLowMemory() {
		super.onLowMemory();

	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

}
