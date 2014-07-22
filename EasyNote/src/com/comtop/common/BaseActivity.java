package com.comtop.common;



import java.lang.reflect.Field;

import com.comtop.easynote.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

public class BaseActivity extends Activity {
	
	/** '��'��'��' ��ʾ�� */
	protected AlertDialog mAlertDialog;

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
	
	/**
	 * ͨ�����������öԻ����Ƿ�Ҫ�رգ��ڱ�У��ʱ�ܹ��ã� ��Ϊ���û���д����ʱ��ȷ��ʱĬ��Dialog����ʧ�� ���Դﲻ��У���Ч��
	 * ��mShowing�ֶξ������������Ƿ�Ҫ��ʧ�ģ�������Dialog����˽�б����� ����ֻ��ͨ������ȥ���������
	 * 
	 * @param pDialog
	 * @param pIsClose
	 */
	public void setAlertDialogIsClose(DialogInterface pDialog, Boolean pIsClose) {
		try {
			Field field = pDialog.getClass().getSuperclass().getDeclaredField("mShowing");
			field.setAccessible(true);
			field.set(pDialog, pIsClose);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected AlertDialog showAlertDialog(String TitleID, String Message) {
		mAlertDialog = new AlertDialog.Builder(this).setTitle(TitleID).setMessage(Message).show();
		return mAlertDialog;
	}

	protected AlertDialog showAlertDialog(int pTitelResID, String pMessage,
			DialogInterface.OnClickListener pOkClickListener) {
		String title = getResources().getString(pTitelResID);
		return showAlertDialog(title, pMessage, pOkClickListener, null, null);
	}

	protected AlertDialog showAlertDialog(String pTitle, String pMessage,
			DialogInterface.OnClickListener pOkClickListener, DialogInterface.OnClickListener pCancelClickListener,
			DialogInterface.OnDismissListener pDismissListener) {
		mAlertDialog = new AlertDialog.Builder(this).setTitle(pTitle).setMessage(pMessage)
				.setPositiveButton(android.R.string.ok, pOkClickListener)
				.setNegativeButton(android.R.string.cancel, pCancelClickListener).show();
		if (pDismissListener != null) {
			mAlertDialog.setOnDismissListener(pDismissListener);
		}
		return mAlertDialog;
	}

	protected AlertDialog showAlertDialog(String pTitle, String pMessage, String pPositiveButtonLabel,
			String pNegativeButtonLabel, DialogInterface.OnClickListener pOkClickListener,
			DialogInterface.OnClickListener pCancelClickListener, DialogInterface.OnDismissListener pDismissListener) {
		mAlertDialog = new AlertDialog.Builder(this).setTitle(pTitle).setMessage(pMessage)
				.setPositiveButton(pPositiveButtonLabel, pOkClickListener)
				.setNegativeButton(pNegativeButtonLabel, pCancelClickListener).show();
		if (pDismissListener != null) {
			mAlertDialog.setOnDismissListener(pDismissListener);
		}
		return mAlertDialog;
	}

	protected ProgressDialog showProgressDialog(int pTitelResID, String pMessage,
			DialogInterface.OnCancelListener pCancelClickListener) {
		String title = getResources().getString(pTitelResID);
		return showProgressDialog(title, pMessage, pCancelClickListener);
	}

	protected ProgressDialog showProgressDialog(String pTitle, String pMessage,
			DialogInterface.OnCancelListener pCancelClickListener) {
		mAlertDialog = ProgressDialog.show(this, pTitle, pMessage, true, true);
		//mAlertDialog.setOnCancelListener(pCancelClickListener);
		return (ProgressDialog) mAlertDialog;
	}
	
}
