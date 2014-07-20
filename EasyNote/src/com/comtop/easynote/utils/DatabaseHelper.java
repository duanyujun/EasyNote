package com.comtop.easynote.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

public class DatabaseHelper extends SQLiteOpenHelper {
	
	private static final int VERSION = 1;
	
	public static String DB_NAME = "easyNote";
	
	public static int DB_VERSION = 1;
	
	public static String T_NOTE = "t_note";
	
	public static String T_FILE = "t_file";
	
	public DatabaseHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	}
	public DatabaseHelper(Context context,String name){
		this(context,name,VERSION);
	}
	public DatabaseHelper(Context context,String name,int version){
		this(context, name,null,version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		StringBuilder sbNoteSql = new StringBuilder();
		sbNoteSql.append("create table ").append(T_NOTE);
		sbNoteSql.append("(note_id text primary key, ");
		sbNoteSql.append(" user_id text, note_title text, note_content text, ");
		//sbNoteSql.append(" modify_time timestamp default CURRENT_TIMESTAMP) ");
		sbNoteSql.append(" modify_time timestamp default (datetime('now', 'localtime'))) ");
		db.execSQL(sbNoteSql.toString());
		
		StringBuilder sbFileSql = new StringBuilder();
		sbFileSql.append("create table ").append(T_FILE);
		sbFileSql.append("(file_id text primary key, ");
		sbFileSql.append(" note_id text, file_path text, ");
		sbFileSql.append(" file_type integer) ");
		db.execSQL(sbFileSql.toString());
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
	}
	

}
