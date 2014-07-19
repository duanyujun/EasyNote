package com.comtop.common;



import com.comtop.easynote.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class BaseActivity extends Activity {

	/**
	 * ����һ��activity
	 * 
	 * @param cls
	 *            ��������activity
	 */
	protected void openActivity(Class<?> cls) {
		Intent intent = new Intent(this, cls);
		startActivity(intent);
		// ���������л�Ч��
		this.overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
	}
	

	/**
	 * ����һ��activity
	 * 
	 * @param cls
	 *            ��������activity
	 * @param pBundle
	 *            ��Ҫ�󶨵�����Bundle
	 */
	protected void openActivity(Class<?> cls, Bundle... pBundle) {
		Intent intent = new Intent(this, cls);
		if (pBundle != null) {
			for (int i = 0; i < pBundle.length; i++) {
				intent.putExtras(pBundle[i]);
			}
		}
		startActivity(intent);
		// ���������л�Ч��
		this.overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
	}
	
}
