/******************************************************************************
 * Copyright (C) 2014 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * �����Ϊ���ڿ����տ������ơ�δ������˾��ʽ����ͬ�⣬�����κθ��ˡ����岻��ʹ�á�
 * ���ơ��޸Ļ򷢲������.
 *****************************************************************************/

package com.comtop.easynote.view;

import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.comtop.easynote.R;
import com.comtop.easynote.utils.StringUtils;
import com.comtop.easynote.adapter.SearchBoxAdapter;
import com.comtop.easynote.adapter.SearchBoxAdapter.MyHintItemOnClickListener;

/**
 * ���������
 * @author duanyujun
 * @since JDK1.6, Min Android API Level: 8
 * @history 2014-6-12 �½�
 * 
 */
public class SearchBoxLite extends LinearLayout {

	/** 1����ʵ�ֵļ�����ʾ���ݵĽӿ� */
	private HintDataProvider myHintDataProvider;
	/** 2����ʵ�ֵĵ����ʾ��Ŀ�¼��ӿ� */
	private HintItemOnClickListener myHintItemOnClickListener;
	/** 3����ʵ�ֵĵ��������ť�ӿ� */
	private SearchButtonOnClickListener mySearchButtonOnClickListener;
	/** LayoutInflater */
	private LayoutInflater mInflater;
	/** ������ */
	private Context mContext;
	/** ����Search View */
	private View mSearchBoxView;
	/** �Զ���ʾ������ */
	private SearchBoxAdapter mAdapter;
	/** �Զ���ʾ��  */
	private AutoCompleteTextView mAutoCompleteTv;
	/** ɾ����ť */
	private ImageView mDeleteContentIv;
	/** ������ť */
	private ImageView mSearchBtnIv;
	/** �Զ���ʾ���� */
	private List<String> mOriginalValues
		=new ArrayList<String>(0);
	private List<String> mNoteContent
		= new ArrayList<String>(0);
	
	
	public SearchBoxLite(Context context) {
		super(context);
		mContext = context;
	}
	
	public SearchBoxLite(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		mInflater = LayoutInflater.from(context);
		mSearchBoxView = mInflater.inflate(R.layout.search_box_lite, null);
		LinearLayout.LayoutParams mParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		mSearchBoxView.setLayoutParams(mParams);
		initViews(context);
		addView(mSearchBoxView);
	}
	
	/**
	 * ��ʼ��
	 * @param context
	 */
	private void initViews(Context context){
		mAutoCompleteTv = (AutoCompleteTextView) mSearchBoxView.findViewById(R.id.search_box_atv);
		mDeleteContentIv = (ImageView) mSearchBoxView.findViewById(R.id.search_box_delete);
		mSearchBtnIv = (ImageView) mSearchBoxView.findViewById(R.id.search_box_btn_query);
		initViewAttrbutes();
	}
	
	/**
	 * ��ʼ������View���������
	 */
	private void initViewAttrbutes(){
		mAutoCompleteTv.setThreshold(1);
//		mAutoCompleteTv.setHint(
//				getResources().getString(R.string.search_hint));
		mAutoCompleteTv.setHintTextColor(
				getResources().getColor(R.color.search_hint_gray));
		mAutoCompleteTv.setOnFocusChangeListener(
				new OnFocusChangeListener() {
					@Override
					public void onFocusChange(View v, boolean hasFocus) {
						if (v.getId()!=mSearchBoxView.getId()  && hasFocus) {
							hideIME(v);
						}
					}
		});
		
		mDeleteContentIv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mAutoCompleteTv.setText("");
				hideIME(v);
			}
		});
		
		//���Ӽ�����
		mAutoCompleteTv.addTextChangedListener(mTextWatcher);
		List<String> lstHintId = new ArrayList<String>(0);
		mAdapter = new SearchBoxAdapter(mContext,
				mOriginalValues,lstHintId);
		mAdapter.addMyHintItemOnClickListener(new MyHintItemOnClickListener() {
			@Override
			public void onClick(View v) {
				TextView strTv = (TextView)v.findViewById(R.id.auto_text);
				mAutoCompleteTv.setText(strTv.getText());
				mAutoCompleteTv.clearFocus();
				hideIME(mAutoCompleteTv);
				myHintItemOnClickListener.onClick(v);
			}
		});
		
		//������������
		mSearchBtnIv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mySearchButtonOnClickListener.onClick(v);
				hideIME(v);
			}
		});
		
		mAutoCompleteTv.setAdapter(mAdapter);
	}
	
	/**
	 * �������뷨
	 * @param view View
	 */
	private void hideIME(View view){
		InputMethodManager imm = (InputMethodManager) mContext
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
	}
	
	/**
	 * �ṩ���ⲿ�������뷨
	 */
	public void hideAutoTextIME(){
		if(mAutoCompleteTv!=null){
			hideIME(mAutoCompleteTv);
		}
	}
	
	/**
	 * ˢ���Զ���ʾ�б�, ���첽�������ݺ���ø÷���ˢ��
	 * @param listHintString ��ʾ�����б���
	 * @param listHintId ��ʾ���ƶ�Ӧ��Id����
	 */
	public void refreshHintList(List<String> listHintString,
			List<String> listHintId, List<String> listNoteContent){
		mOriginalValues.clear();
		mOriginalValues.addAll(listHintString);
		mAdapter.getmObjects().clear();
		mAdapter.getmObjects().addAll(listHintString);
		//���note ����
		mAdapter.getmNoteContents().clear();
		mAdapter.getmNoteContents().addAll(listNoteContent);
		mAdapter.setmOriginalIds(listHintId);
		mAdapter.setmIds(listHintId);
		mAdapter.notifyDataSetChanged();
	}
	
	/**
	 * �����Զ���ʾ��Ŀ����
	 * @param maxHintSize
	 */
	public void setMaxHintSize(int maxHintSize){
		mAdapter.setMaxMatch(maxHintSize);
	}
	
	TextWatcher mTextWatcher = new TextWatcher() {
		
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			//do nothing
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			// TODO Auto-generated method stub
			String strInput = s.toString();
			if(StringUtils.isNotBlank(strInput)){
				if(before==0){
					//TODO ��������
					myHintDataProvider.loadHintData(strInput);
				}
				mDeleteContentIv.setVisibility(View.VISIBLE);
			}else{
				mDeleteContentIv.setVisibility(View.GONE);
			}
		}

		@Override
		public void afterTextChanged(Editable s) {
			String strAfter = s.toString();
			if(StringUtils.isBlank(strAfter)){
				mOriginalValues.clear();
				mOriginalValues.addAll(new ArrayList<String>(0));
				mAdapter.setmOriginalIds(new ArrayList<String>(0));
			}
		}
       
    };
    
    public void addHintDataProvider(HintDataProvider hintDataProvider){
    	this.myHintDataProvider = hintDataProvider;
    }
    
    public void addHintItemOnClickListener(HintItemOnClickListener listener){
    	this.myHintItemOnClickListener = listener;
    }
    
    public void addSearchButtonOnClickListener(SearchButtonOnClickListener listener){
    	this.mySearchButtonOnClickListener = listener;
    }
    
    public List<String> getmNoteContent() {
		return mNoteContent;
	}

	public void setmNoteContent(List<String> mNoteContent) {
		this.mNoteContent = mNoteContent;
	}

	/**
     * �ⲿ�����õ��������е�����
     * @return String 
     */
    public String getSearchString(){
    	return mAutoCompleteTv.getText().toString();
    }
    
    /**
     * ������ʾ���ݵĽӿ�
     */
    public interface HintDataProvider{
    	/**
    	 * ��Ҫʵ�ֵļ�����ʾ���ݵķ���
    	 * @param s String ��ǰ�������е�����
    	 */
    	public void loadHintData(String s);
    	
    }
	
    /**
     * ��ʾ����¼��ӿ�
     */
    public interface HintItemOnClickListener{
    	
    	/**
    	 * ��Ҫʵ�ֵ�hint item����¼�
    	 * @param v
    	 */
		public void onClick(View v);
    }
    
    /**
     * ������ť����¼��ӿ�
     */
    public interface SearchButtonOnClickListener{
    	/**
    	 * ���������ť
    	 * @param v
    	 */
    	public void onClick(View v);
    }
    
}
