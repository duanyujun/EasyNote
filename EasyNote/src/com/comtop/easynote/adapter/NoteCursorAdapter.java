package com.comtop.easynote.adapter;

import com.comtop.easynote.R;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;

public class NoteCursorAdapter extends CursorAdapter {

	private Context context;
	private LayoutInflater inflater;
	
	public NoteCursorAdapter(Context context, Cursor c) {
		super(context, c);
		this.context = context;
		inflater = LayoutInflater.from(context);
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		View view = inflater.inflate(
				R.layout.note_list_item, null);
		bindView(view, context, cursor);
		return view;
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		

	}

}
