package com.comtop.easynote.activity;

import android.content.Intent;
import android.os.Bundle;
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
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.note_view_main);
		mContent = (UnderLineEditText)findViewById(R.id.et_content);
		mTitle = (TextView)findViewById(R.id.tv_note_title);
		
		Intent objIntent = this.getIntent();
		noteId = objIntent.getStringExtra("noteId");
		String noteContent = objIntent.getStringExtra("noteContent");
		if(StringUtils.isNotBlank(noteContent)){
			mContent.setText(noteContent);
		}
		String noteTitle = objIntent.getStringExtra("noteTitle");
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
		//Toast.makeText(this, "edit", Toast.LENGTH_SHORT).show();
		openActivity(NoteEditActivity.class);
	}
	
	
	
}
