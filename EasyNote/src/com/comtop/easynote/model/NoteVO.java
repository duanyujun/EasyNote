package com.comtop.easynote.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class NoteVO {

	private String noteId;
	
	private String userId;
	
	private String noteTitle;
	
	private String noteContent;
	
	private String modifyTime;
	
	private int version;
	
	private List<FileVO> listAttachment = 
			new ArrayList<FileVO>();
	
	public String getNoteId() {
		return noteId;
	}

	public void setNoteId(String noteId) {
		this.noteId = noteId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getNoteTitle() {
		return noteTitle;
	}

	public void setNoteTitle(String noteTitle) {
		this.noteTitle = noteTitle;
	}

	public String getNoteContent() {
		return noteContent;
	}

	public void setNoteContent(String noteContent) {
		this.noteContent = noteContent;
	}

	public String getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(String modifyTime) {
		this.modifyTime = modifyTime;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public List<FileVO> getListAttachment() {
		return listAttachment;
	}

	public void setListAttachment(List<FileVO> listAttachment) {
		this.listAttachment = listAttachment;
	}
	
}
