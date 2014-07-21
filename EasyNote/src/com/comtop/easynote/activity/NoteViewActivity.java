package com.comtop.easynote.activity;

import java.io.File;
import java.util.List;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.comtop.common.BaseActivity;
import com.comtop.easynote.R;
import com.comtop.easynote.adapter.AttachListAdapter;
import com.comtop.easynote.adapter.ViewListAdapter;
import com.comtop.easynote.adapter.ViewListAdapter.OnLongClickListener;
import com.comtop.easynote.constants.Constants;
import com.comtop.easynote.utils.FileUtils;
import com.comtop.easynote.utils.StringUtils;
import com.comtop.easynote.view.UnderLineEditText;

public class NoteViewActivity extends BaseActivity implements OnLongClickListener{
	
	private TextView mNoteId;
	private UnderLineEditText mContent;
	private TextView mTitle;
	private String noteId;
	private String noteTitle;
	private String noteContent;
	private ViewListAdapter adapter;
	private List<File> listData;
	private ListView listView;
	private boolean isFirstRun = true;
	private String attachFilePath;
	private View viewEmpty;
	private View viewLoading;
	
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
		
		attachFilePath = FileUtils.APP_ATTACH_PATH + "/" + noteId;
		
		ScrollView svContent = (ScrollView) findViewById(R.id.sv_content);
		svContent.smoothScrollTo(0, 0);
		initView();
	}
	
	private void initView(){
		viewLoading = findViewById(R.id.load);
		viewEmpty = findViewById(R.id.empty);
		listView = (ListView) findViewById(android.R.id.list);
		listView.setDivider(null);
		listView.setEmptyView(viewLoading);
	}
	
	private void refresh(){
		listData = FileUtils.listFilesInDir(attachFilePath);
		adapter = new ViewListAdapter(this, listData);
		adapter.setOnLongClickListener(this);
		listView.setAdapter(adapter);
		if(listData.size()==0){
			listView.setEmptyView(viewEmpty);
		}
		viewLoading.setVisibility(View.GONE);
	}
	@Override
	protected void onResume() {
		super.onResume();
		if(!isFirstRun){
			listData.clear();
			List<File> tempFiles = FileUtils.listFilesInDir(attachFilePath);
			if(tempFiles.size()==0){
				listView.setEmptyView(viewEmpty);
			}
			viewLoading.setVisibility(View.GONE);
			listData.addAll(tempFiles); 
			adapter.notifyDataSetChanged();
		}else{
			refresh();
			isFirstRun = false;
		}
	}
	
	public void editNote(View view){
		Intent objIntent = new Intent(this, NoteEditActivity.class);
		objIntent.putExtra("noteId", noteId);
		objIntent.putExtra("noteTitle", noteTitle);
		objIntent.putExtra("noteContent", noteContent);
		startActivity(objIntent);
		// ���������л�Ч��
		this.overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
	}
	
	public void back(View view){
		Intent objIntent = new Intent(this, NoteListActivity.class);
		startActivity(objIntent);
		//finish();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
//		if(keyCode == KeyEvent.KEYCODE_BACK){
//			Intent objIntent = new Intent(this, NoteListActivity.class);
//			//openActivity(NoteListActivity.class);
//			startActivity(objIntent);
//			//this.overridePendingTransition(R.anim.push_left_out, R.anim.push_left_in);
//			return true;
//		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onLongClickRefresh() {
		listData.clear();
		List<File> tempFiles = FileUtils.listFilesInDir(attachFilePath);
		if(tempFiles.size()==0){
			listView.setEmptyView(viewEmpty);
		}
		viewLoading.setVisibility(View.GONE);
		listData.addAll(tempFiles); 
		adapter.notifyDataSetChanged();
	}
	
}
