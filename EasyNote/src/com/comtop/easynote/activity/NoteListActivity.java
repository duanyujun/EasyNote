package com.comtop.easynote.activity;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ListView;

import com.comtop.common.BaseActivity;
import com.comtop.easynote.R;
import com.comtop.easynote.adapter.NoteListAdapter;
import com.comtop.easynote.dao.NoteDAO;
import com.comtop.easynote.model.NoteVO;
import com.comtop.easynote.utils.DatabaseHelper;
import com.comtop.easynote.utils.FileUtils;

public class NoteListActivity extends BaseActivity implements com.comtop.easynote.adapter.NoteListAdapter.OnLongClickListener{

	private ListView lvNote;
	private NoteListAdapter adapter;
	private NoteDAO noteDAO;
	private List<NoteVO> listData;
	private boolean isFirstRun = true;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.note_list_main);
		lvNote = (ListView) findViewById(R.id.lv_note);
		DatabaseHelper dbHelper = new DatabaseHelper(this, DatabaseHelper.DB_NAME, DatabaseHelper.DB_VERSION);
		noteDAO = new NoteDAO(dbHelper);
		LayoutInflater inflater = LayoutInflater.from(this);
		View headerView = inflater.inflate(R.layout.note_list_header, null);
		lvNote.addHeaderView(headerView);
		
		listData = noteDAO.listAllNote(); 
		adapter =new NoteListAdapter(this, listData);
		adapter.setOnLongClickListener(this);
		lvNote.setAdapter(adapter);
		
		initSdDir();
	}
	
	private void initSdDir(){
		if (!FileUtils.checkFileExists(FileUtils.APP_PATH)) {
			FileUtils.createDIR(FileUtils.APP_PATH);
		}
		
		if (!FileUtils.checkFileExists(FileUtils.APP_ATTACH_PATH)) {
			FileUtils.createDIR(FileUtils.APP_ATTACH_PATH);
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		if(!isFirstRun){
			listData.clear();
			listData.addAll(noteDAO.listAllNote()); 
			adapter.notifyDataSetChanged();
		}else{
			isFirstRun = false;
		}
	}
	
	public void openEdit(View view){
		openActivity(NoteEditActivity.class);
	}

	@Override
	public void onLongClickRefresh() {
		listData.clear();
		listData.addAll(noteDAO.listAllNote()); 
		adapter.notifyDataSetChanged();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			Intent objIntent = new Intent(this, LoginActivity.class);
			//openActivity(NoteListActivity.class);
			startActivity(objIntent);
			//this.overridePendingTransition(R.anim.push_left_out, R.anim.push_left_in);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
}
