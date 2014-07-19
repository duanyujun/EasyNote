package com.comtop.common;



import com.comtop.easynote.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class BaseActivity extends Activity {

	/**
	 * 启动一个activity
	 * 
	 * @param cls
	 *            待启动的activity
	 */
	protected void openActivity(Class<?> cls) {
		Intent intent = new Intent(this, cls);
		startActivity(intent);
		// 画面向左切换效果
		this.overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
	}
	

	/**
	 * 启动一个activity
	 * 
	 * @param cls
	 *            待启动的activity
	 * @param pBundle
	 *            需要绑定的数据Bundle
	 */
	protected void openActivity(Class<?> cls, Bundle... pBundle) {
		Intent intent = new Intent(this, cls);
		if (pBundle != null) {
			for (int i = 0; i < pBundle.length; i++) {
				intent.putExtras(pBundle[i]);
			}
		}
		startActivity(intent);
		// 画面向左切换效果
		this.overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
	}
	
}
