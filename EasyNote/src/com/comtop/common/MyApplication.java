package com.comtop.common;

import android.app.Application;


public class MyApplication extends Application {
	
	private static MyApplication mInstance;

	/** ��ǰ��½�û� */
	private String userId;


	public static MyApplication getInstance() {
		return mInstance;
	}

	/**
	 * Android������������
	 */
	@Override
	public void onCreate() {
		super.onCreate();
		mInstance = this;
	}

	/**
	 * �������ڴ治��ʱ�����õķ���
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
