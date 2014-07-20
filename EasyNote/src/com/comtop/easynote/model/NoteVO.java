package com.comtop.easynote.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class NoteVO {

	private String noteId;
	
	private String userId;
	
	private String noteTitle;
	
	private String noteContent;
	
	private Timestamp modifyTime;
	
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

	public Timestamp getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(Timestamp modifyTime) {
		this.modifyTime = modifyTime;
	}

	public List<FileVO> getListAttachment() {
		return listAttachment;
	}

	public void setListAttachment(List<FileVO> listAttachment) {
		this.listAttachment = listAttachment;
	}
	
}
