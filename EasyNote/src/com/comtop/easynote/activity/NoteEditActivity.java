package com.comtop.easynote.activity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Timer;
import java.util.TimerTask;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.media.AudioManager;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
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
	private RelativeLayout rlRecordLayout;
	private Button btnAttachment;
	private Button finishRecordButton;
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
		
		wl = ((PowerManager)getSystemService("power")).newWakeLock(6, "comtop");
	    am = ((AudioManager)getSystemService("audio"));
		
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
			
			//处理音频
			this.audioBtn.setEnabled(true);
			if (this.mMediaRecorder != null)
				this.mMediaRecorder.stop();
			if (this.mUpdateTimerTask != null) {
				this.mUpdateTimerTask.cancel();
				this.mUpdateTimerTask = null;
			}
			if (this.mUpdateVolumTask != null) {
				this.mUpdateVolumTask.cancel();
				this.mUpdateVolumTask = null;
			}
			if(this.mMediaRecorder!=null){
				this.mMediaRecorder.release();
			}
			this.mMediaRecorder = null;
			if (!isFinishing()) {
				this.recordTimeText.setText("00:00");
				this.topBarView.setLayoutParams(new LinearLayout.LayoutParams(
						-1, -1));
			}
			this.isRecording = false;
			this.wl.release();
			this.am.abandonAudioFocus(this.afChangeListener);
			
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
		audioBtn = (Button) findViewById(R.id.edit_audio_btn);
		Button btnImage = (Button) findViewById(R.id.edit_lib_btn);
		recordTimeText = (TextView)findViewById(R.id.record_time);
		volumBar = (ProgressBar)findViewById(R.id.recordVolume);
		topBarView = findViewById(R.id.top_bar);
		finishRecordButton = ((Button)findViewById(R.id.btnRecordFinish));
		
		btnCamera.setOnClickListener(clickListener);
		btnStt.setOnClickListener(clickListener);
		audioBtn.setOnClickListener(clickListener);
		btnImage.setOnClickListener(clickListener);
		finishRecordButton.setOnClickListener(clickListener);
		
		
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
				v.setEnabled(false);
				toggleRecordAudioAction();
				break;
			case R.id.edit_lib_btn:
				//插入图片
				IntentUtils.chooseImage(NoteEditActivity.this, toSaveNoteId, Constants.RESULT_IMAGE);
				break;	
			case R.id.btnRecordFinish:
				NoteEditActivity.this.audioBtn.setEnabled(true);
			    NoteEditActivity.this.toggleRecordAudioAction();
			default:
				break;
			}
			
			
		}
	};
	
	private String audioFilepath = null;
	private AudioManager am = null;
	private Button audioBtn;
	private MediaRecorder mMediaRecorder = null;
	private TextView recordTimeText = null;
	private ProgressBar volumBar = null;
	private View topBarView = null;
	private PowerManager.WakeLock wl = null;
	private boolean isRecording = false;
	private long mStartTime = 0L;
	private Timer mTimer = new Timer();
	private Toast mToast;
	private TimerTask mUpdateTimerTask = null;
	private final Handler mUpdateUIHandler = new Handler() {
		public void handleMessage(Message paramAnonymousMessage) {
			switch (paramAnonymousMessage.what) {
			default:
				return;
			case 0:
				NoteEditActivity.this.recordTimeText
						.setText(paramAnonymousMessage.getData().getString(
								"volum"));
				return;
			case 1:
			}
			NoteEditActivity.this.volumBar
					.setProgress(paramAnonymousMessage.arg1);
		}
	};
	private TimerTask mUpdateVolumTask = null;
	private AudioManager.OnAudioFocusChangeListener musicAudioFocusChangeListenerForAsr = new AudioManager.OnAudioFocusChangeListener() {
		public void onAudioFocusChange(int paramAnonymousInt) {
			if (-1 == paramAnonymousInt) {
//				if (NoteEditActivity.this.sndaRecognizeDlg != null)
//					NoteEditActivity.this.sndaRecognizeDlg
//							.SuspendRecognize(false);
				if (NoteEditActivity.this.am != null)
					NoteEditActivity.this.am
							.abandonAudioFocus(NoteEditActivity.this.musicAudioFocusChangeListenerForAsr);
			}
		}
	};
	
	private AudioManager.OnAudioFocusChangeListener afChangeListener = new AudioManager.OnAudioFocusChangeListener()
	  {
	    public void onAudioFocusChange(int paramAnonymousInt)
	    {
	      if ((paramAnonymousInt == -1) || (paramAnonymousInt == -2) || (paramAnonymousInt == -3))
	      {
	    	NoteEditActivity.this.audioBtn.setEnabled(true);
	        NoteEditActivity.this.mMediaRecorder.stop();
	        NoteEditActivity.this.mUpdateTimerTask.cancel();
	        NoteEditActivity.this.mUpdateVolumTask.cancel();
	        NoteEditActivity.this.mUpdateTimerTask = null;
	        NoteEditActivity.this.mUpdateVolumTask = null;
	        NoteEditActivity.this.mMediaRecorder.release();
	        NoteEditActivity.this.mMediaRecorder = null;
	        NoteEditActivity.this.recordTimeText.setText("00:00");
	        //NoteEditActivity.this.topBarView.setLayoutParams(new LinearLayout.LayoutParams(-1, -1));
	        NoteEditActivity.this.topBarView.setVisibility(View.VISIBLE);
	        NoteEditActivity.this.isRecording = false;
	        NoteEditActivity.this.wl.release();
	        NoteEditActivity.this.am.abandonAudioFocus(NoteEditActivity.this.afChangeListener);
	        //NoteEditActivity.this.showDialog(12);
	      }
	    }
	  };
	
	private void toggleRecordAudioAction() {
		if (this.mMediaRecorder == null) {
			this.wl.acquire();
			//NoteEditActivity.this.
			NoteEditActivity.this.topBarView.setVisibility(View.GONE);
//			SharedPreferences localSharedPreferences = PreferenceManager
//					.getDefaultSharedPreferences(this);
			//String str = "translte_audio_action" + Inote.getUserSndaID();
			//localSharedPreferences.edit().putBoolean(str, false).commit();
			this.mMediaRecorder = new MediaRecorder();
			try {
				this.mMediaRecorder.setAudioSource(1);
				this.mMediaRecorder.setOutputFormat(3);
				this.mMediaRecorder.setAudioEncoder(1);
				this.mMediaRecorder.setAudioChannels(1);
				
				String noteIdDir = FileUtils.APP_ATTACH_PATH + "/" + toSaveNoteId;
				if (!FileUtils.checkFileExists(noteIdDir)) {
					FileUtils.createDIR(noteIdDir);
				}
				
				this.audioFilepath = noteIdDir + "/"
						+ StringUtils.getDataFormatFileName("audio_") + ".amr";
				
				File localFile1 = FileUtils.createFile(this.audioFilepath);
				this.mMediaRecorder.setOutputFile(localFile1.getAbsolutePath());
				this.mMediaRecorder.prepare();
				this.am.requestAudioFocus(this.afChangeListener, 4, 1);
				this.mMediaRecorder.start();
				this.mStartTime = System.currentTimeMillis();
//				this.topBarView.setLayoutParams(new LinearLayout.LayoutParams(
//						0, -1));
				this.mUpdateTimerTask = new UpdateTimerTask();
				this.mUpdateVolumTask = new UpdateVolumeTask();
				this.mTimer.schedule(this.mUpdateTimerTask, 1000L, 1000L);
				this.mTimer.schedule(this.mUpdateVolumTask, 0L, 100L);
				this.isRecording = true;
				return;
			} catch (Exception localException2) {
				localException2.printStackTrace();
				Toast.makeText(getApplicationContext(), getString(2131427848),
						0).show();
				this.mMediaRecorder.release();
				this.mMediaRecorder = null;
				this.wl.release();
				this.am.abandonAudioFocus(this.afChangeListener);
				this.audioBtn.setEnabled(true);
				return;
			}
		}
		try {
			NoteEditActivity.this.topBarView.setVisibility(View.VISIBLE);
			this.mMediaRecorder.stop();
			this.mUpdateTimerTask.cancel();
			this.mUpdateVolumTask.cancel();
			this.mUpdateTimerTask = null;
			this.mUpdateVolumTask = null;
			this.mMediaRecorder.release();
			this.mMediaRecorder = null;
			this.mStartTime = 0L;
			//addFileToAttachFileList(IOUtil.getExternalFile(this.audioFilepath));
			btnAttachment.setText(String.valueOf(FileUtils.listFiles(FileUtils.APP_ATTACH_PATH+"/"+toSaveNoteId)));
			
			//showToast(getString(2131427872));
			this.recordTimeText.setText("00:00");
//			this.topBarView.setLayoutParams(new LinearLayout.LayoutParams(-1,
//					-1));
			this.isRecording = false;
			this.wl.release();
			this.am.abandonAudioFocus(this.afChangeListener);
			return;
		} catch (Exception localException1) {
			localException1.printStackTrace();
			Toast.makeText(getApplicationContext(), getString(2131427848), 0)
					.show();
			this.mUpdateTimerTask.cancel();
			this.mUpdateVolumTask.cancel();
			this.mUpdateTimerTask = null;
			this.mUpdateVolumTask = null;
			if (this.mMediaRecorder != null) {
				this.mMediaRecorder.release();
				this.mMediaRecorder = null;
			}
			this.mStartTime = 0L;
			this.recordTimeText.setText("00:00");
			this.topBarView.setLayoutParams(new LinearLayout.LayoutParams(-1,
					-1));
			this.isRecording = false;
			this.wl.release();
			this.am.abandonAudioFocus(this.afChangeListener);
		}
	}
	
	class UpdateTimerTask extends TimerTask {
		UpdateTimerTask() {
		}

		public void run() {
			Message localMessage = new Message();
			localMessage.what = 0;
			long l = (System.currentTimeMillis() - NoteEditActivity.this.mStartTime) / 1000L;
			NoteEditActivity localNoteEditActivity = NoteEditActivity.this;
			Object[] arrayOfObject = new Object[2];
			arrayOfObject[0] = Long.valueOf(l / 60L);
			arrayOfObject[1] = Long.valueOf(l % 60L);
			String str = localNoteEditActivity.getString(R.string.timer_format,
					arrayOfObject);
			Bundle localBundle = new Bundle();
			localBundle.putString("volum", str);
			localMessage.setData(localBundle);
			NoteEditActivity.this.mUpdateUIHandler.sendMessage(localMessage);
		}
	}

	class UpdateVolumeTask extends TimerTask {
		UpdateVolumeTask() {
		}

		public void run() {
			if (NoteEditActivity.this.mMediaRecorder == null)
				return;
			try {
				Message localMessage = new Message();
				localMessage.what = 1;
				localMessage.arg1 = (100 * NoteEditActivity.this.mMediaRecorder
						.getMaxAmplitude() / 32768);
				NoteEditActivity.this.mUpdateUIHandler
						.sendMessage(localMessage);
				return;
			} catch (Exception localException) {
				cancel();
			}
		}
	}
	
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
