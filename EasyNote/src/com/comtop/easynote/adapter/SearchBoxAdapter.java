/******************************************************************************
 * Copyright (C) 2014 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * �����Ϊ���ڿ����տ������ơ�δ������˾��ʽ����ͬ�⣬�����κθ��ˡ����岻��ʹ�á�
 * ���ơ��޸Ļ򷢲������.
 *****************************************************************************/

package com.comtop.easynote.adapter;

import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import com.comtop.easynote.R;

/**
 * ������������
 * @author duanyujun
 * @since JDK1.6
 * @history 2014-6-12 �½�
 * 
 */
public class SearchBoxAdapter extends BaseAdapter implements Filterable{
	private Context context;
	private ArrayFilter mFilter;
	private List<String> mOriginalIds;//���е�id
	private List<String> mOriginalValues;//���е�Item
	private List<String> mObjects = new ArrayList<String>();//���˺��item
	private List<String> mIds;//���˺��item id
	private final Object mLock = new Object();
	private int maxMatch=100;//�����ʾ���ٸ�ѡ��,������ʾȫ��
	private MyHintItemOnClickListener myHintOnClickListner;
	
	public SearchBoxAdapter(Context context, List<String> mOriginalValues,
			List<String> mOriginalIds){
		this.context = context;
		this.mOriginalValues = mOriginalValues;
		this.mOriginalIds = mOriginalIds;
	}
	
	@Override
	public Filter getFilter() {
		// TODO Auto-generated method stub
		if (mFilter == null) {  
            mFilter = new ArrayFilter();  
        }  
        return mFilter;
	}
	
	private class ArrayFilter extends Filter {

		@Override
		protected FilterResults performFiltering(CharSequence prefix) {
			// TODO Auto-generated method stub
			FilterResults results = new FilterResults();  
			  
            if (prefix == null || prefix.length() == 0) {  
                synchronized (mLock) {
                	Log.i("tag", "mOriginalValues.size="+mOriginalValues.size());
                    ArrayList<String> list = new ArrayList<String>(mOriginalValues);  
                    results.values = list;  
                    results.count = list.size(); 
                    return results;
                }  
            } else {
                String prefixString = prefix.toString().toLowerCase();  
                final int count = mOriginalValues.size();  
  
                final ArrayList<String> newValues = new ArrayList<String>(count);  
                final ArrayList<String> newIds = new ArrayList<String>(count);
                for (int i = 0; i < count; i++) {
                	
                	final String id = mOriginalIds!=null?mOriginalIds.get(i):"";
                    final String value = mOriginalValues.get(i);  
                    final String valueText = value.toLowerCase();  
  
                    // First match against the whole, non-splitted value  
                    if (valueText.contains(prefixString)) {  //Դ�� ,ƥ�俪ͷ
                    	if(mOriginalIds!=null){
                    		newIds.add(id);
                    	}
                    	newValues.add(value);  
                    } 
                    if(maxMatch>0){//����������  
                        if(newValues.size()>maxMatch-1){//��Ҫ̫��  
                            break;  
                        }  
                    }  
                }  
                if(mOriginalIds!=null){
                	mIds = newIds;
                }
                results.values = newValues;  
                results.count = newValues.size();  
            }  
  
            return results;
		}

		@Override
		protected void publishResults(CharSequence constraint,
				FilterResults results) {
			// TODO Auto-generated method stub
			mObjects = (List<String>) results.values;  
            if (results.count > 0) {  
                notifyDataSetChanged();  
            } else {  
                notifyDataSetInvalidated();  
            }
		}
		
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if(mObjects==null){
			return 0;
		}
		return mObjects.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		if(mObjects==null){
			return null;
		}
		return mObjects.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		if(convertView==null){
			holder=new ViewHolder();
			LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView=inflater.inflate(R.layout.search_box_item, null);
			holder.tv=(TextView)convertView.findViewById(R.id.auto_text);  
			if(mOriginalIds!=null){
				holder.tvId = (TextView)convertView.findViewById(R.id.auto_text_id);
			}
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.tv.setText(mObjects.get(position));
		if(mOriginalIds!=null 
				&& mIds!=null
				&& mIds.size()>position){
			holder.tvId.setText(mIds.get(position));
		}
		
		convertView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(myHintOnClickListner!=null){
					myHintOnClickListner.onClick(v);
				}
			}
		});
		
		return convertView;
	}

	class ViewHolder {
		TextView tvId;
        TextView tv;
    }
	
	public List<String> getmOriginalIds() {
		return mOriginalIds;
	}
	
	public void setmOriginalIds(List<String> mOriginalIds) {
		this.mOriginalIds = mOriginalIds;
	}
	
	public void setmIds(List<String> mIds) {
		this.mIds = mIds;
	}

	public List<String> getmObjects() {
		return mObjects;
	}

	public void setmObjects(List<String> mObjects) {
		this.mObjects = mObjects;
	}
	
	public void setMaxMatch(int maxMatch) {
		this.maxMatch = maxMatch;
	}
	
	public List<String> getAllItems(){
		return mOriginalValues;
	}
	
	public void addMyHintItemOnClickListener(MyHintItemOnClickListener listner){
		this.myHintOnClickListner = listner;
	}
	
	public interface MyHintItemOnClickListener{
		public void onClick(View v);
	}
}
