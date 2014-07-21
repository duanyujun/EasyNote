package com.comtop.easynote.activity;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.comtop.common.BaseActivity;
import com.comtop.easynote.R;
import com.comtop.easynote.constants.Constants;
import com.comtop.easynote.utils.StringUtils;
import com.comtop.easynote.view.UnderLineEditText;

public class NoteViewActivity extends BaseActivity {
	
	private TextView mNoteId;
	private UnderLineEditText mContent;
	private TextView mTitle;
	private String noteId;
	private String noteTitle;
	private String noteContent;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.note_view_main);
		mContent = (UnderLineEditText)findViewById(R.id.et_content);
		mTitle = (TextView)findViewById(R.id.tv_note_title);
		
		Intent objIntent = this.getIntent();
		noteId = objIntent.getStringExtra("noteId");
		noteContent = objIntent.getStringExtra("noteContent");
		if(StringUtils.isNotBlank(noteContent)){
			mContent.setText(noteContent);
		}
		noteTitle = objIntent.getStringExtra("noteTitle");
		if(StringUtils.isNotBlank(noteTitle)){
			mTitle.setText(noteTitle);
		}else{
			mTitle.setText(Constants.DEFAULT_TITLE);
			mTitle.setTextColor(getResources().getColor(R.color.search_hint_gray));
		}
		if(StringUtils.isNotBlank(noteId)){
			mNoteId = (TextView)findViewById(R.id.tv_note_id);
			mNoteId.setText(noteId);
			mContent.setFocusableInTouchMode(false);
			mContent.clearFocus();
		}
		
	}
	
	public void editNote(View view){
		Intent objIntent = new Intent(this, NoteEditActivity.class);
		objIntent.putExtra("noteId", noteId);
		objIntent.putExtra("noteTitle", noteTitle);
		objIntent.putExtra("noteContent", noteContent);
		startActivity(objIntent);
		// 画面向左切换效果
		this.overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
	}
	
	public void back(View view){
		finish();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			Intent objIntent = new Intent(this, NoteListActivity.class);
			//openActivity(NoteListActivity.class);
			startActivity(objIntent);
			//this.overridePendingTransition(R.anim.push_left_out, R.anim.push_left_in);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	
	
}
