package com.comtop.easynote.adapter;

import java.io.File;
import java.util.List;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.comtop.easynote.R;
import com.comtop.easynote.constants.Constants;
import com.comtop.easynote.dao.NoteDAO;
import com.comtop.easynote.utils.DatabaseHelper;
import com.comtop.easynote.utils.FileSizeUtil;
import com.comtop.easynote.utils.StringUtils;

public class AttachListAdapter extends BaseAdapter implements OnLongClickListener{

	private LayoutInflater inflater;
	private List<File> list;
	private Context context;
	private OnLongClickListener onLongClickListener;
	private boolean isEditable;
	
	public AttachListAdapter(Context context, List<File> list, boolean isEditable){
		 this.context = context;
		 this.isEditable = isEditable;
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
					R.layout.attachitem, null);
			holder = new ViewHolder();
			holder.attachImage = (ImageView) convertView.findViewById(R.id.list_select_icon);
			holder.attachAbsPath = (TextView)convertView.findViewById(R.id.attach_abs_path);
			holder.fileTitle = (TextView)convertView.findViewById(R.id.attach_title);
			holder.fileSize = (TextView)convertView.findViewById(R.id.attach_size);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		final File file = list.get(position);
		if(file.getName().endsWith(".jpg")){
			Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
			holder.attachImage.setImageBitmap(bitmap);
		}else{
			holder.attachImage.setImageDrawable(context.getResources().getDrawable(R.drawable.fi_mp3));
		}
		final String strAttachAbsPath = file.getAbsolutePath();
		final String strAttachTitle = file.getName();
		String strFileSize = FileSizeUtil.getAutoFileOrFilesSize(strAttachAbsPath);
		final String strAttachFileSize = strFileSize;
		
		holder.attachAbsPath.setText(strAttachAbsPath);
		holder.fileTitle.setText(strAttachTitle);
		holder.fileSize.setText(strAttachFileSize);
		
		convertView.setOnLongClickListener(this);
		convertView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//Toast.makeText(context, "click", Toast.LENGTH_SHORT).show();
				Uri localUri = Uri.fromFile(file);
			    int i = strAttachTitle.lastIndexOf(".");
			    String strFileType = strAttachTitle.substring(i+1);
			    String strMimeType = Constants.nameAndMine.get(strFileType.toLowerCase());
			    if(StringUtils.isBlank(strMimeType)){
			    	strMimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(strFileType.toLowerCase());
			    }
			    Intent localIntent = new Intent("android.intent.action.VIEW");
		        localIntent.putExtra("NoNeedEditOperation", true);
		        localIntent.setDataAndType(localUri, strMimeType);
		        context.startActivity(localIntent);
			}
		});
		
		return convertView;
	}
	
	@Override
	public boolean onLongClick(View v) {
		AlertDialog.Builder builder = new Builder(context);
		builder.setMessage("确认删除吗？");
		builder.setTitle("提示");
		final String strFileAbsPath = ((TextView)v.findViewById(R.id.attach_abs_path)).getText().toString();
		builder.setPositiveButton("确认",
				new android.content.DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						File file = new File(strFileAbsPath);
						if(file.exists()){
							file.delete();
						}
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
	    
	    public ImageView attachImage;
	    
	    public TextView attachAbsPath;
	    
	    public TextView fileTitle;
	    
	    public TextView fileSize;
	}

}
