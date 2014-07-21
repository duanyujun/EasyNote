package com.comtop.easynote.dao;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.comtop.easynote.model.FileVO;
import com.comtop.easynote.model.NoteVO;
import com.comtop.easynote.utils.DatabaseHelper;
import com.comtop.easynote.utils.DateTimeUtils;
import com.comtop.easynote.utils.StringUtils;

public class NoteDAO {
	
	private DatabaseHelper helper;
	
	public NoteDAO(DatabaseHelper helper){
		this.helper = helper;
	}

	/**
	 * 新增note
	 * @param noteVO NoteVO
	 */
	public String insertNote(NoteVO noteVO){
		if(noteVO==null){
			return null;
		}
		StringBuilder sbNoteSql = new StringBuilder();
		StringBuilder sbFileSql = new StringBuilder();
		SQLiteDatabase db = helper.getWritableDatabase();
		db.beginTransaction();
		//t_note表新增sql
		sbNoteSql.append(" insert into ").append(DatabaseHelper.T_NOTE)
		  		 .append(" (note_id, user_id, note_title, note_content) values ")
		         .append(" ('").append(noteVO.getNoteId())
		         .append("','").append(noteVO.getUserId())
		         .append("','").append(noteVO.getNoteTitle())
		         .append("','").append(noteVO.getNoteContent())
		         .append("')");
		db.execSQL(sbNoteSql.toString());
		//t_file表新增sql
		if(noteVO.getListAttachment().size()>0){
			for(int i=0; i<noteVO.getListAttachment().size(); i++){
				FileVO fileVO = noteVO.getListAttachment().get(i);
				if(i!=0){
					sbFileSql.delete(0, sbFileSql.length());
				}
				sbFileSql.append(" insert into ").append(DatabaseHelper.T_FILE)
		  		 .append(" (file_id, note_id, file_path, file_type) values ")
		         .append(" ('").append(fileVO.getFileId())
		         .append("','").append(noteVO.getNoteId())
		         .append("','").append(fileVO.getFilePath())
		         .append("',").append(fileVO.getFileType())
		         .append(" )");
				db.execSQL(sbFileSql.toString());
			}
		}
		db.setTransactionSuccessful();
		db.endTransaction();
		db.close();
		
		return noteVO.getNoteId();
	}
	
	/**
	 * 删除选中的note
	 * @param noteIds
	 */
	public void deleteNote(String[] noteIds){
		if(noteIds==null || noteIds.length==0){
			return;
		}
		SQLiteDatabase db = helper.getWritableDatabase();
		StringBuilder sbDeleteSql = new StringBuilder();
		db.beginTransaction();
		//删除t_note
		sbDeleteSql.append("delete from ")
			.append(DatabaseHelper.T_NOTE)
			.append(" where note_id in(");
		for(int i=0; i<noteIds.length; i++){
			sbDeleteSql.append("'")
			  	.append(noteIds[i]).append("'");
			if(i!=(noteIds.length-1)){
				sbDeleteSql.append(",");
			}
		}
		sbDeleteSql.append(")");
		db.execSQL(sbDeleteSql.toString());
		//删除t_file
		sbDeleteSql.delete(0, sbDeleteSql.length());
		for(int i=0; i<noteIds.length; i++){
			String[] args = {noteIds[i]};
			db.delete(DatabaseHelper.T_FILE, "note_id=?", args);
		}
		db.setTransactionSuccessful();
		db.endTransaction();
		db.close();
	}
	
	/**
	 * 读取笔记
	 * @param noteId 笔记Id
	 * @return NoteVO
	 */
	public NoteVO readNoteVO(String noteId){
		NoteVO noteVO = new NoteVO();
		StringBuilder sbReadSql = new StringBuilder();
		SQLiteDatabase db = helper.getReadableDatabase();
		sbReadSql.append(" select * from ").append(DatabaseHelper.T_NOTE)
			.append(" where note_id = '").append(noteId).append("'");
		Cursor cursor = db.rawQuery(sbReadSql.toString(), null);
		while(cursor.moveToNext()){
			noteVO.setNoteId(cursor.getString(0));
			noteVO.setUserId(cursor.getString(1));
			noteVO.setNoteTitle(cursor.getString(2));
			noteVO.setNoteContent(cursor.getString(3));
			noteVO.setModifyTime(Timestamp.valueOf(cursor.getString(4)));
		}
		
		//读取FileVO
		sbReadSql.delete(0, sbReadSql.length());
		sbReadSql.append(" select * from ").append(DatabaseHelper.T_FILE)
		.append(" where note_id = '").append(noteId).append("'");
		cursor = db.rawQuery(sbReadSql.toString(), null);
		while(cursor.moveToNext()){
			FileVO fileVO = new FileVO();
			fileVO.setFileId(cursor.getString(0));
			fileVO.setNoteId(noteId);
			fileVO.setFilePath(cursor.getString(2));
			fileVO.setFileType(cursor.getInt(3));
			noteVO.getListAttachment().add(fileVO);
		}
		db.close(); 
		return noteVO;
	}
	
	/**
	 * 更新笔记
	 */
	public int updateNote(NoteVO noteVO){
		StringBuilder sbFileSql = new StringBuilder();
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put("note_title", noteVO.getNoteTitle());
		cv.put("note_content", noteVO.getNoteContent());
		cv.put("modify_time", DateTimeUtils.formatDateTime(
				new Timestamp(new Date().getTime()), DateTimeUtils.ISO_DATETIME_FORMAT));
		String[] args = {noteVO.getNoteId()}; 
		db.beginTransaction();
		int iResult = db.update(DatabaseHelper.T_NOTE, cv, "note_id=?", args);
		
		//删除该笔记已经存在的文件
		db.delete(DatabaseHelper.T_FILE, "note_id=?", args);
		//新增文件
		//t_file表新增sql
		if(noteVO.getListAttachment().size()>0){
			for(int i=0; i<noteVO.getListAttachment().size(); i++){
				FileVO fileVO = noteVO.getListAttachment().get(i);
				if(i!=0){
					sbFileSql.delete(0, sbFileSql.length());
				}
				sbFileSql.append(" insert into ").append(DatabaseHelper.T_FILE)
		  		 .append(" (file_id, note_id, file_path, file_type) values ")
		         .append(" ('").append(fileVO.getFileId())
		         .append("','").append(noteVO.getNoteId())
		         .append("','").append(fileVO.getFilePath())
		         .append("',").append(fileVO.getFileType())
		         .append(" )");
				db.execSQL(sbFileSql.toString());
			}
		}
		
		db.setTransactionSuccessful();
		db.endTransaction();
		db.close();
		return iResult;
	}
	
	
	/**
	 * 读取全部笔记
	 * @return NoteVO集合
	 */
	public List<NoteVO> listAllNote(){
		List<NoteVO> lstNote = new ArrayList<NoteVO>();
		StringBuilder sbReadSql = new StringBuilder();
		SQLiteDatabase db = helper.getReadableDatabase();
		sbReadSql.append(" select * from ").append(DatabaseHelper.T_NOTE)
				 .append(" order by modify_time desc ");
		Cursor cursor = db.rawQuery(sbReadSql.toString(), null);
		while(cursor.moveToNext()){
			NoteVO noteVO = new NoteVO();
			noteVO.setNoteId(cursor.getString(0));
			noteVO.setUserId(cursor.getString(1));
			noteVO.setNoteTitle(cursor.getString(2));
			noteVO.setNoteContent(cursor.getString(3));
			noteVO.setModifyTime(Timestamp.valueOf(cursor.getString(4)));
			lstNote.add(noteVO);
		}
		
		for(NoteVO noteVO : lstNote){
			//读取FileVO
			sbReadSql.delete(0, sbReadSql.length());
			sbReadSql.append(" select * from ").append(DatabaseHelper.T_FILE)
			.append(" where note_id = '").append(noteVO.getNoteId()).append("'");
			cursor = db.rawQuery(sbReadSql.toString(), null);
			while(cursor.moveToNext()){
				FileVO fileVO = new FileVO();
				fileVO.setFileId(cursor.getString(0));
				fileVO.setNoteId(noteVO.getNoteId());
				fileVO.setFilePath(cursor.getString(2));
				fileVO.setFileType(cursor.getInt(3));
				noteVO.getListAttachment().add(fileVO);
			}
		}
		
		db.close(); 
		return lstNote;
	}
	
	/**
	 * 查询全部笔记
	 * @param input 搜索字符
	 * @return NoteVO集合
	 */
	public List<NoteVO> searchNoteList(String input){
		List<NoteVO> lstNote = new ArrayList<NoteVO>();
		StringBuilder sbReadSql = new StringBuilder();
		SQLiteDatabase db = helper.getReadableDatabase();
		sbReadSql.append(" select * from ").append(DatabaseHelper.T_NOTE)
				 .append(" where note_title like '%").append(input).append("%'")
				 .append(" or (note_content like '%").append(input).append("%') ");
		Cursor cursor = db.rawQuery(sbReadSql.toString(), null);
		String strNoteTitle;
		String strNoteContent;
		while(cursor.moveToNext()){
			NoteVO noteVO = new NoteVO();
			noteVO.setNoteId(cursor.getString(0));
			noteVO.setUserId(cursor.getString(1));
			strNoteTitle = cursor.getString(2);
			strNoteContent = cursor.getString(3);
			if(StringUtils.isBlank(strNoteTitle)){
				noteVO.setNoteTitle(strNoteContent);
			}else{
				noteVO.setNoteTitle(strNoteTitle);
			}
			noteVO.setNoteContent(strNoteContent);
			noteVO.setModifyTime(Timestamp.valueOf(cursor.getString(4)));
			lstNote.add(noteVO);
		}
		
		db.close(); 
		return lstNote;
	}
	
	/**
	 * 通过NoteId查询笔记
	 * @param input 搜索字符
	 * @return NoteVO集合
	 */
	public List<NoteVO> searchNoteById(String noteId){
		List<NoteVO> lstNote = new ArrayList<NoteVO>();
		StringBuilder sbReadSql = new StringBuilder();
		SQLiteDatabase db = helper.getReadableDatabase();
		sbReadSql.append(" select * from ").append(DatabaseHelper.T_NOTE)
				 .append(" where note_id = '").append(noteId).append("'");
		Cursor cursor = db.rawQuery(sbReadSql.toString(), null);
		String strNoteTitle;
		String strNoteContent;
		while(cursor.moveToNext()){
			NoteVO noteVO = new NoteVO();
			noteVO.setNoteId(cursor.getString(0));
			noteVO.setUserId(cursor.getString(1));
			strNoteTitle = cursor.getString(2);
			strNoteContent = cursor.getString(3);
			if(StringUtils.isBlank(strNoteTitle)){
				noteVO.setNoteTitle(strNoteContent);
			}else{
				noteVO.setNoteTitle(strNoteTitle);
			}
			noteVO.setNoteContent(strNoteContent);
			noteVO.setModifyTime(Timestamp.valueOf(cursor.getString(4)));
			lstNote.add(noteVO);
		}
		
		db.close(); 
		return lstNote;
	}
	
}
