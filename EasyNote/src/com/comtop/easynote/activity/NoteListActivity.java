package com.comtop.easynote.activity;

import java.util.List;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
	private LinearLayout searchLayout;
	private LinearLayout normalLayout;
	private ImageView ivSearch;
	private Button btnNoteCancel;
	
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
		initSearch();
	}
	
	private void initSdDir(){
		if (!FileUtils.checkFileExists(FileUtils.APP_PATH)) {
			FileUtils.createDIR(FileUtils.APP_PATH);
		}
		
		if (!FileUtils.checkFileExists(FileUtils.APP_ATTACH_PATH)) {
			FileUtils.createDIR(FileUtils.APP_ATTACH_PATH);
		}
	}
	
	private void initSearch(){
		searchLayout = (LinearLayout) findViewById(R.id.searchLayout);
		normalLayout = (LinearLayout) findViewById(R.id.normalLayout);
		ivSearch = (ImageView) findViewById(R.id.search_btn_img);
		btnNoteCancel = (Button)findViewById(R.id.btnNoteCancel);
		ivSearch.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				searchLayout.setVisibility(View.VISIBLE);
				normalLayout.setVisibility(View.GONE);
			}
		});
		btnNoteCancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				searchLayout.setVisibility(View.GONE);
				normalLayout.setVisibility(View.VISIBLE);
			}
		});
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
//		if(keyCode == KeyEvent.KEYCODE_BACK){
//			Intent objIntent = new Intent(this, LoginActivity.class);
//			//openActivity(NoteListActivity.class);
//			startActivity(objIntent);
//			//this.overridePendingTransition(R.anim.push_left_out, R.anim.push_left_in);
//			return true;
//		}
		return super.onKeyDown(keyCode, event);
	}
	
}
