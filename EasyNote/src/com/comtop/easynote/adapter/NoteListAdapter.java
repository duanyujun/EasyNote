package com.comtop.easynote.adapter;

import java.util.List;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.comtop.easynote.R;
import com.comtop.easynote.activity.NoteEditActivity;
import com.comtop.easynote.activity.NoteViewActivity;
import com.comtop.easynote.dao.NoteDAO;
import com.comtop.easynote.model.NoteVO;
import com.comtop.easynote.utils.DatabaseHelper;
import com.comtop.easynote.utils.DateTimeUtils;
import com.comtop.easynote.utils.StringUtils;

public class NoteListAdapter extends BaseAdapter implements OnLongClickListener{

	private LayoutInflater inflater;
	private List<NoteVO> list;
	private Context context;
	private OnLongClickListener onLongClickListener;
	
	public NoteListAdapter(Context context, List<NoteVO> list){
		 this.context = context;
		 inflater = LayoutInflater.from(context);
		 this.list = list;
	}
	
	public void setOnLongClickListener(OnLongClickListener onLongClickListener) {
		this.onLongClickListener = onLongClickListener;
	}
	
	@Override
	public int getCount() {
		return (list != null && list.size() != 0) ? list.size() : 0;
	}

	@Override
	public Object getItem(int position) {
		return (list != null && list.size() != 0) ? list.get(position) : null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (null == convertView) {
			convertView = inflater.inflate(
					R.layout.note_list_item, null);
			holder = new ViewHolder();
			holder.noteId = (TextView) convertView
					.findViewById(R.id.tv_note_id);
			holder.noteContent = (TextView) convertView
					.findViewById(R.id.tv_note_content);
			holder.createTime = (TextView) convertView
					.findViewById(R.id.tv_note_create_time);
			holder.attachImageView = (ImageView)convertView
					.findViewById(R.id.iv_attach_icon);
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		NoteVO noteVO = list.get(position);
		final String noteId = noteVO.getNoteId();
		final String noteTitle = noteVO.getNoteTitle();
		final String noteContent = noteVO.getNoteContent();
		holder.noteId.setText(noteId);
		if(StringUtils.isNotBlank(noteTitle)){
			holder.noteContent.setText(noteTitle);
		}else{
			holder.noteContent.setText(noteContent.replaceAll("\n", ""));
		}
		holder.createTime.setText("修改时间：" + 
				DateTimeUtils.formatDate(noteVO.getModifyTime(), DateTimeUtils.ISO_DATETIME_FORMAT));
		if(noteVO.getListAttachment().size()==0){
			holder.attachImageView.setVisibility(View.GONE);
		}
		
		convertView.setOnLongClickListener(this);
		convertView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent objIntent = new Intent();
				objIntent.setClass(context, NoteViewActivity.class);
				objIntent.putExtra("noteId", noteId);
				objIntent.putExtra("noteTitle", noteTitle);
				objIntent.putExtra("noteContent", noteContent);
				context.startActivity(objIntent);
			}
		});
		
		return convertView;
	}
	
	@Override
	public boolean onLongClick(View v) {
		AlertDialog.Builder builder = new Builder(context);
		builder.setMessage("确认删除吗？");
		builder.setTitle("提示");
		final String noteId = ((TextView) v.findViewById(R.id.tv_note_id)).getText()
				.toString();
		DatabaseHelper dbHelper = new DatabaseHelper(context, DatabaseHelper.DB_NAME, DatabaseHelper.DB_VERSION);
		final NoteDAO noteDAO = new NoteDAO(dbHelper);
		builder.setPositiveButton("确认",
				new android.content.DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						noteDAO.deleteNote(new String[]{noteId});
						onLongClickListener.onLongClickRefresh();			
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
		return true;
	}
	
	public interface OnLongClickListener {
		/**
		 * Called when the list should be refreshed.
		 * <p>
		 * A call to {@link PullToRefreshListView #onRefreshComplete()} is
		 * expected to indicate that the refresh has completed.
		 */
		public void onLongClickRefresh();
	}
	
	public final class ViewHolder {
	    
		public TextView noteId;
		
	    public TextView noteContent;
	        
	    public TextView createTime; 
	    
	    public ImageView attachImageView;
	    
	}

}
