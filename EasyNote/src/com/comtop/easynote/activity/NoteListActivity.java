package com.comtop.easynote.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.net.ftp.FTPClient;

import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.comtop.common.BaseActivity;
import com.comtop.common.MyApplication;
import com.comtop.common.utils.HttpClient;
import com.comtop.easynote.R;
import com.comtop.easynote.adapter.NoteListAdapter;
import com.comtop.easynote.dao.NoteDAO;
import com.comtop.easynote.model.HttpEntryVO;
import com.comtop.easynote.model.NoteVO;
import com.comtop.easynote.utils.DatabaseHelper;
import com.comtop.easynote.utils.FileUtils;
import com.comtop.easynote.utils.FtpUtils;
import com.comtop.easynote.utils.StringUtils;
import com.comtop.easynote.view.SearchBoxLite;
import com.comtop.easynote.view.SearchBoxLite.HintDataProvider;
import com.comtop.easynote.view.SearchBoxLite.HintItemOnClickListener;
import com.comtop.easynote.view.SearchBoxLite.SearchButtonOnClickListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class NoteListActivity extends BaseActivity implements com.comtop.easynote.adapter.NoteListAdapter.OnLongClickListener{

	private Gson objGson = new Gson();
	private ListView lvNote;
	private NoteListAdapter adapter;
	private NoteDAO noteDAO;
	private List<NoteVO> listData;
	private boolean isFirstRun = true;
	private LinearLayout searchLayout;
	private LinearLayout normalLayout;
	private ImageView ivSearch;
	private Button btnNoteCancel;
	private SearchBoxLite searchBoxLite;
	private MyApplication application;
	private String userId;
	private FTPClient ftpClient;
	private boolean isLogin;
	
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
		
		application = (MyApplication)getApplication();
		userId = application.getUserId();
		initSdDir();
		
		listData = noteDAO.listAllNote(userId); 
		adapter =new NoteListAdapter(this, listData, FileUtils.APP_ATTACH_PATH+"/"+userId);
		adapter.setOnLongClickListener(this);
		lvNote.setAdapter(adapter);
		
		initSearch();
	}
	
	public void syncNote(View view){
		showProgressDialog(R.string.progress_dialog_title,
				"����ͬ�����Ե�һ��~", null);
		RequestParams params = new RequestParams();
		params.put("userId", userId);
		//��ȡ���ظ��û�ȫ����Note
		final List<NoteVO> listNoteVO = noteDAO.listAllNote(userId);
		if(listNoteVO.size()>0){
			HttpEntryVO httpEntryVO = new HttpEntryVO();
			httpEntryVO.setListNoteVO(listNoteVO);
			//תΪjson
			String strLocalNotes = objGson.toJson(httpEntryVO, HttpEntryVO.class);
			params.put("localNotes", strLocalNotes);
		}
		
		HttpClient.post("/syncNote/retrieveRemoteNotes", params, new AsyncHttpResponseHandler() {
			// ��ʼ��������
			@Override
			public void onStart() {
				//showAlertDialog("��ܰ��ʾ", "���ڼ������Ե�һ��~");
			}

			// ����ɹ�
			@Override
			public void onSuccess(String response) {
				if (StringUtils.isNotBlank(response)) {
					HttpEntryVO listRemoteNoteVO = objGson.fromJson(response, 
							new TypeToken<HttpEntryVO>() {}.getType());
					handleSyncNotes(listNoteVO, listRemoteNoteVO.getListNoteVO());
				} else {
					
				}
				if(mAlertDialog!=null){
					mAlertDialog.dismiss();
					mAlertDialog = null;
				}
			}

			// ����ʧ��
			@Override
			public void onFailure(int stateCode, Throwable e, String data) {
				
			}

		});
	}
	
	private void handleSyncNotes(List<NoteVO> listLocalNotes, 
			List<NoteVO> listRemoteNotes){
		//�����ص�note����map��
		Map<String, NoteVO> localNoteMap = new HashMap<String, NoteVO>();
		for(NoteVO noteVO : listLocalNotes){
			localNoteMap.put(noteVO.getNoteId(), noteVO);
		}
		
		//��Ҫ����������note���浽���ص�list
		List<NoteVO> listNeedInsertLocal = new ArrayList<NoteVO>();
		//��Ҫ����������note���µ����ص�list
		List<NoteVO> listNeedUpdateLocal = new ArrayList<NoteVO>();
		//�����������˴�������note
		Map<String, NoteVO> remoteNoteMap = new HashMap<String, NoteVO>();
		NoteVO localNoteVO;
		for(NoteVO noteVO : listRemoteNotes){
			localNoteVO = localNoteMap.get(noteVO.getNoteId());
			if(localNoteVO==null){
				listNeedInsertLocal.add(noteVO);
			}else{
				//�жϱ��غͷ�������note�İ汾
				if(noteVO.getVersion()>localNoteVO.getVersion()){
					listNeedUpdateLocal.add(noteVO);
				}
			}
			//��ӵ�remoteNoteMap
			remoteNoteMap.put(noteVO.getNoteId(), noteVO);
		}
		//ftp�ͻ���
		if(!isLogin){
			ftpClient = FtpUtils.getFtpClient();
		}
		isLogin = true;
		//������Ҫ�����
		for(NoteVO noteVO : listNeedInsertLocal){
			//�������ݿ�
			noteDAO.insertNote(noteVO);
			//���������ɾ���ļ��У��½��ļ��У�ftp��ȡ����
			FileUtils.delteDir(FileUtils.APP_ATTACH_PATH +"/"+userId+"/"+noteVO.getNoteId());
			FileUtils.createDIR(FileUtils.APP_ATTACH_PATH +"/"+userId+"/"+noteVO.getNoteId());
			if(noteVO.getListAttachment().size()>0){
				FtpUtils.downloadFile(ftpClient, noteVO.getNoteId(), userId);
			}
		}
		//������Ҫ���µ�
		for(NoteVO noteVO : listNeedUpdateLocal){
			//�������ݿ�
			noteDAO.updateNote(noteVO);
			//���������ɾ���ļ��У��½��ļ��У�ftp��ȡ����
			FileUtils.delteDir(FileUtils.APP_ATTACH_PATH +"/"+userId+"/"+noteVO.getNoteId());
			FileUtils.createDIR(FileUtils.APP_ATTACH_PATH +"/"+userId+"/"+noteVO.getNoteId());
			if(noteVO.getListAttachment().size()>0){
				FtpUtils.downloadFile(ftpClient, noteVO.getNoteId(), userId);
			}
		}
		//�������У����Ƿ�������û�еģ�Ȼ���ϴ�������ftp
		List<NoteVO> listUploadImage = new ArrayList<NoteVO>();
		for(NoteVO noteVO : listLocalNotes){
			if(remoteNoteMap.get(noteVO.getNoteId())==null){
				listUploadImage.add(noteVO);
			}
		}
		boolean isDirCreated = false;
		for(NoteVO noteVO : listUploadImage){
			if(noteVO.getListAttachment().size()>0){
				//��Ŀ¼
				if(!isDirCreated){
					FtpUtils.createDir(ftpClient, "/files");
					FtpUtils.createDir(ftpClient, "/files/"+userId);
					FtpUtils.createDir(ftpClient, "/files/"+userId+"/"+noteVO.getNoteId());
					isDirCreated = true;
				}
				FtpUtils.uploadFile(ftpClient, noteVO.getListAttachment(), userId);
			}
		}
		
	}
	
	private void initSdDir(){
		if (!FileUtils.checkFileExists(FileUtils.APP_PATH)) {
			FileUtils.createDIR(FileUtils.APP_PATH);
		}
		
		if (!FileUtils.checkFileExists(FileUtils.APP_ATTACH_PATH)) {
			FileUtils.createDIR(FileUtils.APP_ATTACH_PATH);
		}
		
		if (!FileUtils.checkFileExists(FileUtils.APP_ATTACH_PATH+"/"+userId)) {
			FileUtils.createDIR(FileUtils.APP_ATTACH_PATH +"/"+userId);
		}
	}
	
	private void initSearch(){
		searchLayout = (LinearLayout) findViewById(R.id.searchLayout);
		normalLayout = (LinearLayout) findViewById(R.id.normalLayout);
		ivSearch = (ImageView) findViewById(R.id.search_btn_img);
		btnNoteCancel = (Button)findViewById(R.id.btnNoteCancel);
		searchBoxLite = (SearchBoxLite)findViewById(R.id.searchBox);
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
				hideIME(v);
			}
		});
		
		//������ʾ���ݽӿ�
		searchBoxLite.addHintDataProvider(new HintDataProvider() {
			@Override
			public void loadHintData(String s) {
				loadLocalHintData(s);
			}
		});
		
		//������ʾ����item����¼��ӿ�
		searchBoxLite.addHintItemOnClickListener(new HintItemOnClickListener() {
			@Override
			public void onClick(View v) {
				TextView strTvId = (TextView)v.findViewById(R.id.auto_text_id); 
				String noteId = strTvId.getText().toString();
				listData.clear();
				listData.addAll(noteDAO.searchNoteById(noteId)); 
				adapter.notifyDataSetChanged();
			}
		});
		//����������ť����¼��ӿ�
		searchBoxLite.addSearchButtonOnClickListener(new SearchButtonOnClickListener() {
			@Override
			public void onClick(View v) {
				String input = searchBoxLite.getSearchString();
				if(StringUtils.isBlank(input)){
					listData.clear();
					listData.addAll(noteDAO.listAllNote(userId)); 
					adapter.notifyDataSetChanged();
				}else{
					listData.clear();
					listData.addAll(noteDAO.searchNoteList(input)); 
					adapter.notifyDataSetChanged();
				}
			}
		});
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
	protected void onResume() {
		super.onResume();
		if(!isFirstRun){
			listData.clear();
			listData.addAll(noteDAO.listAllNote(userId)); 
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
		listData.addAll(noteDAO.listAllNote(userId)); 
		adapter.notifyDataSetChanged();
	}
	
	/**
	 * ��������������
	 * @param s
	 */
	private void loadLocalHintData(String input){
		List<NoteVO> lstItemVO = noteDAO.searchNoteList(input);
		//id�б�
		List<String> lstHintId = new ArrayList<String>();
		List<String> lstHintString = new ArrayList<String>();
		List<String> lstNoteContentString = new ArrayList<String>();
		for(NoteVO vo : lstItemVO){
			lstHintId.add(vo.getNoteId());
			lstHintString.add(vo.getNoteTitle());
			lstNoteContentString.add(vo.getNoteContent());
		}
		searchBoxLite.refreshHintList(lstHintString, lstHintId, lstNoteContentString);
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
