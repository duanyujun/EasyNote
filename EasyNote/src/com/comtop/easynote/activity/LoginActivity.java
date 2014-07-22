package com.comtop.easynote.activity;

import com.comtop.common.BaseActivity;
import com.comtop.common.MyApplication;
import com.comtop.easynote.R;
import com.comtop.easynote.constants.Constants;
import com.comtop.easynote.utils.EncryUtil;
import com.comtop.easynote.utils.StringUtils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends BaseActivity {

	private EditText mUsername;
	private EditText mPassword;
	private TextView mClearPassword;
	private SharedPreferences sharedPreferences;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.login_main);
		mUsername = (EditText)findViewById(R.id.et_username);
		mPassword= (EditText)findViewById(R.id.et_password);
		mClearPassword = (TextView)findViewById(R.id.tv_clear_password);
		initUserInf();
		Button btnLogin = (Button)findViewById(R.id.btn_login);
		btnLogin.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(StringUtils.isBlank(mUsername.getText().toString())){
					if(StringUtils.isBlank(mPassword.getText().toString())){
						Toast.makeText(LoginActivity.this, "�������û���������", Toast.LENGTH_SHORT).show();
					}else{
						Toast.makeText(LoginActivity.this, "�������û���", Toast.LENGTH_SHORT).show();
					}
				}else{
					if(StringUtils.isBlank(mPassword.getText().toString())){
						Toast.makeText(LoginActivity.this, "����������", Toast.LENGTH_SHORT).show();
					}else{
						MyApplication application = MyApplication.getInstance();
						application.setUserId(mUsername.getText().toString());
						saveUserInf(mUsername.getText().toString(), mPassword.getText().toString());
						hideIME(v);
						openActivity(NoteListActivity.class);
					}
				}
			}
		});
		
		mClearPassword.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				clearPassword();
			}
		});
	}
	
	private void initUserInf() {
		sharedPreferences = getSharedPreferences(Constants.SHARE_NAME, MODE_PRIVATE);
		if (sharedPreferences!=null &&
				sharedPreferences.contains(Constants.USER_NAME)) {
			String userName = sharedPreferences.getString(Constants.USER_NAME, "");
			mUsername.setText(userName);
			String password = sharedPreferences.getString(Constants.PASSWORD, "");
			if(StringUtils.isNotBlank(password)){
				if(EncryUtil.decrypt(password)!=null){
					password = EncryUtil.decrypt(password).toString();
				}
			}
			mPassword.setText(password);
		}
	}

	private void saveUserInf(String userAccount, String password) {
		// ��ȡsharedPreference�ı༭��
		Editor editor = sharedPreferences.edit();
		editor.putString(Constants.USER_NAME, userAccount);
		if(StringUtils.isNotBlank(password)){
			editor.putString(Constants.PASSWORD, password);
		}
		editor.commit();
	}
	
	public void clearPassword(){
		Editor editor = sharedPreferences.edit();
		editor.remove(Constants.PASSWORD);
		editor.commit();
		mPassword.setText("");
	}
	
	/**
	 * �������뷨
	 * @param view View
	 */
	private void hideIME(View view){
		InputMethodManager imm = (InputMethodManager) this
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
	}
	
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
//		if(keyCode == KeyEvent.KEYCODE_BACK){
//			android.os.Process.killProcess(android.os.Process.myPid());
//			return true;
//		}
		return super.onKeyDown(keyCode, event);
	}
	
}
