package com.comtop.easynote.activity;

import java.io.File;
import java.util.List;
import com.comtop.common.BaseActivity;
import com.comtop.common.MyApplication;
import com.comtop.easynote.R;
import com.comtop.easynote.adapter.AttachListAdapter;
import com.comtop.easynote.adapter.AttachListAdapter.OnLongClickListener;
import com.comtop.easynote.utils.FileUtils;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

public class AttachListActivity extends BaseActivity implements OnLongClickListener{

	private List<File> listData;
	private String noteId;
	private String toSaveNoteId;
	private AttachListAdapter adapter;
	private ListView listView;
	private boolean isFirstRun = true;
	private String attachFilePath;
	private View viewEmpty;
	private View viewLoading;
	private MyApplication application;
	private String userId;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.attachlist);
		Intent objIntent = this.getIntent();
		noteId = objIntent.getStringExtra("noteId");
		toSaveNoteId = objIntent.getStringExtra("toSaveNoteId");
		application = (MyApplication)getApplication();
		userId = application.getUserId();
		
		attachFilePath = FileUtils.APP_ATTACH_PATH + "/" + userId + "/" + toSaveNoteId;
		
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
		adapter = new AttachListAdapter(this, listData, true);
		adapter.setOnLongClickListener(this);
		listView.setAdapter(adapter);
		if(listData.size()==0){
			listView.setEmptyView(viewEmpty);
		}
		viewLoading.setVisibility(View.GONE);
	}
	
	public void back(View view){
		finish();
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
	
}
