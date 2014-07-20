package com.comtop.easynote.activity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.comtop.common.BaseActivity;
import com.comtop.common.MyApplication;
import com.comtop.easynote.R;
import com.comtop.easynote.constants.Constants;
import com.comtop.easynote.dao.NoteDAO;
import com.comtop.easynote.model.NoteVO;
import com.comtop.easynote.utils.DatabaseHelper;
import com.comtop.easynote.utils.FileUtils;
import com.comtop.easynote.utils.IntentUtils;
import com.comtop.easynote.utils.StringUtils;
import com.comtop.easynote.view.UnderLineEditText;

public class NoteEditActivity extends BaseActivity {
	
	private LinearLayout llAttachlayout;
	private ImageButton ibToggleTool;
	private boolean isAttachLayoutShow = true;
	private UnderLineEditText mContent;
	private EditText mTitle;
	private TextView mNoteId;
	private Button btnAttachment;
	private String noteId;
	private NoteDAO noteDAO;
	private MyApplication application;
	private boolean isChanged;
	private String strOldTitle = null;
	private String strOldContent = null;
	/** 当id存在时值和noteId一致；当没有Id时，初始化一个 */
	private String toSaveNoteId = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.note_edit);
		
		llAttachlayout = (LinearLayout) findViewById(R.id.attachlayout);
		ibToggleTool = (ImageButton) findViewById(R.id.toggleToolBtn);
		ibToggleTool.setBackgroundDrawable(getResources().getDrawable(R.drawable.newdoc_ancor_up));
		mContent = (UnderLineEditText)findViewById(R.id.et_content);
		mTitle = (EditText)findViewById(R.id.et_title);
		btnAttachment = (Button)findViewById(R.id.edit_attach_btn_content);
		
		initNote(); 
		
		DatabaseHelper dbHelper = new DatabaseHelper(this, DatabaseHelper.DB_NAME, DatabaseHelper.DB_VERSION);
		noteDAO = new NoteDAO(dbHelper);
		application = (MyApplication)getApplication();
	}
	
	/**
	 * 保存
	 * @param view
	 */
	public void saveNote(View view){
		NoteVO noteVO = new NoteVO();
		noteVO.setUserId(application.getUserId());
	    String strTitle = mTitle.getText().toString();
	    String strContent = mContent.getText().toString();
	    if(StringUtils.isBlank(strTitle) && StringUtils.isBlank(strContent)){
	    	Toast.makeText(this, "请至少输入标题", Toast.LENGTH_SHORT).show();
	    	return;
	    }
	    
	    noteVO.setNoteTitle(strTitle);
		noteVO.setNoteContent(strContent);
		if(StringUtils.isNotBlank(noteId)){
			noteVO.setNoteId(noteId);
			noteDAO.updateNote(noteVO);
		}else{
			noteVO.setNoteId(toSaveNoteId);
			noteDAO.insertNote(noteVO);
		}
		
		Intent objIntent = new Intent();
		objIntent.setClass(this, NoteViewActivity.class);
		objIntent.putExtra("noteId", noteId);
		objIntent.putExtra("noteTitle", strTitle);
		objIntent.putExtra("noteContent", strContent);
		Toast.makeText(this, "已保存", Toast.LENGTH_SHORT).show();
		startActivity(objIntent);
	}
	
	/**
	 * 取消修改
	 * @param view
	 */
	public void cancelEdit(View view){
		Intent objIntent = new Intent();
		if(StringUtils.isNotBlank(noteId)){
			objIntent.setClass(this, NoteViewActivity.class);
			objIntent.putExtra("noteId", noteId);
			objIntent.putExtra("noteTitle", strOldTitle);
			objIntent.putExtra("noteContent", strOldContent);
		}else{
			objIntent.setClass(this, NoteListActivity.class);
		}
		startActivity(objIntent);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			checkChanged();
			if(isChanged){
				AlertDialog.Builder builder = new Builder(this);
				builder.setMessage("是否放弃本次修改？");
				builder.setTitle("提示");
				builder.setPositiveButton("放弃",
						new android.content.DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								if(StringUtils.isBlank(noteId)){
									openActivity(NoteListActivity.class);
								}else{
									Intent objIntent = new Intent(NoteEditActivity.this, NoteViewActivity.class);
									objIntent.putExtra("noteId", noteId);
									objIntent.putExtra("noteTitle", strOldTitle);
									objIntent.putExtra("noteContent", strOldContent);
									startActivity(objIntent);
									//openActivity(NoteViewActivity.class);
								}
								dialog.dismiss();
							}
						});

				builder.setNegativeButton("取消",
						new android.content.DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								dialog.dismiss();
							}
						});

				builder.create().show();
			}else{
				if(StringUtils.isBlank(noteId)){
					openActivity(NoteListActivity.class);
				}else{
					Intent objIntent = new Intent(this, NoteEditActivity.class);
					objIntent.putExtra("noteId", noteId);
					objIntent.putExtra("noteTitle", strOldTitle);
					objIntent.putExtra("noteContent", strOldContent);
					startActivity(objIntent);
					//openActivity(NoteViewActivity.class);
				}
			}
			
		}
		return super.onKeyDown(keyCode, event);
	}
	
	/**
	 * 检查是否有过编辑动作
	 */
	private void checkChanged(){
		String strTitle = mTitle.getText().toString();
		String strContent = mContent.getText().toString();
		if(StringUtils.isNotBlank(strTitle) && !strTitle.equals(strOldTitle)
				|| StringUtils.isNotBlank(strContent) && !strContent.equals(strOldContent)){
			isChanged = true;
		}
	}
	
	/**
	 * 初始化笔记
	 */
	private void initNote(){
		Intent objIntent = this.getIntent();
		noteId = objIntent.getStringExtra("noteId");
		String noteContent = objIntent.getStringExtra("noteContent");
		if(StringUtils.isNotBlank(noteContent)){
			strOldContent = noteContent;
			mContent.setText(noteContent);
			mContent.setSelection(noteContent.length());
		}
		String noteTitle = objIntent.getStringExtra("noteTitle");
		if(StringUtils.isNotBlank(noteTitle)){
			strOldTitle = noteTitle;
			mTitle.setText(noteTitle);
			mTitle.setSelection(noteTitle.length());
		}
		if(StringUtils.isNotBlank(noteId)){
			mNoteId = (TextView)findViewById(R.id.tv_note_id);
			mNoteId.setText(noteId);
			toSaveNoteId = noteId;
		}else{
			toSaveNoteId = String.valueOf(System.currentTimeMillis());
		}
		
		//初始化相应的button
		Button btnCamera = (Button) findViewById(R.id.edit_photo_btn);
		Button btnStt = (Button) findViewById(R.id.edit_stt_btn);
		Button btnAudio = (Button) findViewById(R.id.edit_audio_btn);
		Button btnImage = (Button) findViewById(R.id.edit_lib_btn);
		
		btnCamera.setOnClickListener(clickListener);
		btnStt.setOnClickListener(clickListener);
		btnAudio.setOnClickListener(clickListener);
		btnImage.setOnClickListener(clickListener);
		
		btnAttachment.setText(String.valueOf(FileUtils.listFiles(FileUtils.APP_ATTACH_PATH+"/"+toSaveNoteId)));
	}
	
	/**
	 * 打开添加附件列表
	 * @param view
	 */
	public void toggleAttachLayout(View view){
		if(isAttachLayoutShow){
			llAttachlayout.setVisibility(View.GONE);
			isAttachLayoutShow = false;
			ibToggleTool.setBackgroundDrawable(getResources().getDrawable(R.drawable.newdoc_ancor_down));
		}else{
			llAttachlayout.setVisibility(View.VISIBLE);
			isAttachLayoutShow = true;
			ibToggleTool.setBackgroundDrawable(getResources().getDrawable(R.drawable.newdoc_ancor_up));
		}
	}
	
	View.OnClickListener clickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.edit_photo_btn:
				//拍照
				IntentUtils.openCamera(NoteEditActivity.this, toSaveNoteId, Constants.RESULT_PHOTO);
				break;
			case R.id.edit_stt_btn:
				//转文字
				
				break;	
			case R.id.edit_audio_btn:
				//录音
				
				break;
			case R.id.edit_lib_btn:
				//插入图片
				IntentUtils.chooseImage(NoteEditActivity.this, toSaveNoteId, Constants.RESULT_IMAGE);
				break;	

			default:
				break;
			}
			
		}
	};
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == RESULT_OK){
			switch(requestCode){
				case Constants.RESULT_PHOTO:
					btnAttachment.setText(String.valueOf(FileUtils.listFiles(FileUtils.APP_ATTACH_PATH+"/"+toSaveNoteId)));
					//Toast.makeText(this, "success", Toast.LENGTH_SHORT);
					break;
				case Constants.RESULT_IMAGE:
					if (data != null) {  
		                Uri mImageCaptureUri = data.getData();  
		                if (mImageCaptureUri != null) {  
		                    Bitmap image;  
		                    try {  
		                        image = MediaStore.Images.Media.getBitmap(this.getContentResolver(), mImageCaptureUri);  
		                        if (image != null) {  
		                            FileUtils.savePicToSdcard(image,
		                            		FileUtils.APP_ATTACH_PATH + "/" + toSaveNoteId, 
		                            		StringUtils.getDataFormatFileName("img_") + ".jpg");
		                        }  
		                    } catch (Exception e) {  
		                        e.printStackTrace();  
		                    }  
		                } else {  
		                    Bundle extras = data.getExtras();  
		                    if (extras != null) {  
		                        //这里是有些拍照后的图片是直接存放到Bundle中的所以我们可以从这里面获取Bitmap图片  
		                        Bitmap image = extras.getParcelable("data");  
		                        if (image != null) {  
		                        	FileUtils.savePicToSdcard(image,
		                            		FileUtils.APP_ATTACH_PATH + "/" + toSaveNoteId, 
		                            		StringUtils.getDataFormatFileName("img_") + ".jpg");
		                        }  
		                    }  
		                }  
		                
		                btnAttachment.setText(String.valueOf(FileUtils.listFiles(FileUtils.APP_ATTACH_PATH+"/"+toSaveNoteId)));
		            } 
					break;
				default:
					break;	
			}
		}
	};
	
}
