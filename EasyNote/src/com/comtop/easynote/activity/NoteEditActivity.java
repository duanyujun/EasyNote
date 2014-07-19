package com.comtop.easynote.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;
import com.comtop.common.BaseActivity;
import com.comtop.common.MyApplication;
import com.comtop.easynote.R;
import com.comtop.easynote.dao.NoteDAO;
import com.comtop.easynote.model.NoteVO;
import com.comtop.easynote.utils.DatabaseHelper;
import com.comtop.easynote.utils.StringUtils;
import com.comtop.easynote.view.UnderLineEditText;

public class NoteEditActivity extends BaseActivity {
	
	private NoteDAO noteDAO;
	private TextView mNoteId;
	private UnderLineEditText mContent;
	private MyApplication application;
	private boolean isEditing = true;
	private String noteId;
	private GestureDetector mGestureDetector = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.note_edit_main);
		mContent = (UnderLineEditText)findViewById(R.id.et_content);
		
		Intent objIntent = this.getIntent();
		noteId = objIntent.getStringExtra("noteId");
		String noteContent = objIntent.getStringExtra("noteContent");
		if(StringUtils.isNotBlank(noteContent)){
			mContent.setText(noteContent);
			mContent.setSelection(noteContent.length());
		}
		if(StringUtils.isNotBlank(noteId)){
			mNoteId = (TextView)findViewById(R.id.tv_note_id);
			mNoteId.setText(noteId);
			mContent.setFocusableInTouchMode(false);
			mContent.clearFocus();
			isEditing = false;
		}
		
		DatabaseHelper dbHelper = new DatabaseHelper(this, DatabaseHelper.DB_NAME, DatabaseHelper.DB_VERSION);
		noteDAO = new NoteDAO(dbHelper);
		application = (MyApplication)getApplication();
		
        mGestureDetector = new GestureDetector(this, mOnGestureListener);
	
	}
	
	public void editNote(View view){
		//Toast.makeText(this, "edit", Toast.LENGTH_SHORT).show();
		mContent.setFocusableInTouchMode(true);
		mContent.requestFocus();
		isEditing = true;
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			if(StringUtils.isNotBlank(mContent.getText().toString())){
				if(isEditing){
					saveNote();
					mContent.setFocusableInTouchMode(false);
					mContent.clearFocus();
					isEditing = false;
					Toast.makeText(this, "已保存", Toast.LENGTH_SHORT).show();
					return true;
				}
			}else{
				if(isEditing && StringUtils.isNotBlank(noteId)){
					noteDAO.deleteNote(new String[]{noteId});
					isEditing = false;
				}
			}
		}
		return super.onKeyDown(keyCode, event);
	}
	
	@Override
    public boolean onTouchEvent(MotionEvent event) {
        return mGestureDetector.onTouchEvent(event);
    }
	
	private OnGestureListener mOnGestureListener = new GestureDetector.SimpleOnGestureListener(){
		@Override
		public boolean onDoubleTap(MotionEvent e) {
			return false;
		}
	};
	
	/**
	 * 保存笔记
	 */
	private void saveNote(){
		NoteVO noteVO = new NoteVO();
		noteVO.setUserId(application.getUserId());
		noteVO.setNoteContent(mContent.getText().toString());
		if(StringUtils.isNotBlank(noteId)){
			noteVO.setNoteId(noteId);
			noteDAO.updateNote(noteVO);
		}else{
			noteVO.setNoteId(String.valueOf(System.currentTimeMillis()));
			noteDAO.insertNote(noteVO);
		}
	}
	
}
