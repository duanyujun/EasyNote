/******************************************************************************
 * Copyright (C) 2014 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
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
 * 搜索框组件
 * @author duanyujun
 * @since JDK1.6, Min Android API Level: 8
 * @history 2014-6-12 新建
 * 
 */
public class SearchBoxLite extends LinearLayout {

	/** 1、需实现的加载提示数据的接口 */
	private HintDataProvider myHintDataProvider;
	/** 2、需实现的点击提示条目事件接口 */
	private HintItemOnClickListener myHintItemOnClickListener;
	/** 3、需实现的点击搜索按钮接口 */
	private SearchButtonOnClickListener mySearchButtonOnClickListener;
	/** LayoutInflater */
	private LayoutInflater mInflater;
	/** 上下文 */
	private Context mContext;
	/** 整个Search View */
	private View mSearchBoxView;
	/** 自动提示适配器 */
	private SearchBoxAdapter mAdapter;
	/** 自动提示框  */
	private AutoCompleteTextView mAutoCompleteTv;
	/** 删除按钮 */
	private ImageView mDeleteContentIv;
	/** 搜索按钮 */
	private ImageView mSearchBtnIv;
	/** 自动提示数据 */
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
	 * 初始化
	 * @param context
	 */
	private void initViews(Context context){
		mAutoCompleteTv = (AutoCompleteTextView) mSearchBoxView.findViewById(R.id.search_box_atv);
		mDeleteContentIv = (ImageView) mSearchBoxView.findViewById(R.id.search_box_delete);
		mSearchBtnIv = (ImageView) mSearchBoxView.findViewById(R.id.search_box_btn_query);
		initViewAttrbutes();
	}
	
	/**
	 * 初始化各个View的相关属性
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
		
		//增加监听器
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
		
		//增加搜索框点击
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
	 * 隐藏输入法
	 * @param view View
	 */
	private void hideIME(View view){
		InputMethodManager imm = (InputMethodManager) mContext
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
	}
	
	/**
	 * 提供给外部隐藏输入法
	 */
	public void hideAutoTextIME(){
		if(mAutoCompleteTv!=null){
			hideIME(mAutoCompleteTv);
		}
	}
	
	/**
	 * 刷新自动提示列表, 在异步请求到数据后调用该方法刷新
	 * @param listHintString 提示名称列表集合
	 * @param listHintId 提示名称对应的Id集合
	 */
	public void refreshHintList(List<String> listHintString,
			List<String> listHintId, List<String> listNoteContent){
		mOriginalValues.clear();
		mOriginalValues.addAll(listHintString);
		mAdapter.getmObjects().clear();
		mAdapter.getmObjects().addAll(listHintString);
		//添加note 内容
		mAdapter.getmNoteContents().clear();
		mAdapter.getmNoteContents().addAll(listNoteContent);
		mAdapter.setmOriginalIds(listHintId);
		mAdapter.setmIds(listHintId);
		mAdapter.notifyDataSetChanged();
	}
	
	/**
	 * 设置自动提示条目数量
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
					//TODO 加载数据
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
     * 外部可以拿到搜索框中的内容
     * @return String 
     */
    public String getSearchString(){
    	return mAutoCompleteTv.getText().toString();
    }
    
    /**
     * 搜索提示数据的接口
     */
    public interface HintDataProvider{
    	/**
    	 * 需要实现的加载提示数据的方法
    	 * @param s String 当前搜索框中的文字
    	 */
    	public void loadHintData(String s);
    	
    }
	
    /**
     * 提示点击事件接口
     */
    public interface HintItemOnClickListener{
    	
    	/**
    	 * 需要实现的hint item点击事件
    	 * @param v
    	 */
		public void onClick(View v);
    }
    
    /**
     * 搜索框按钮点击事件接口
     */
    public interface SearchButtonOnClickListener{
    	/**
    	 * 点击搜索框按钮
    	 * @param v
    	 */
    	public void onClick(View v);
    }
    
}
