package com.comtop.easynote.model;

public class FileVO {

	private String fileId;
	
	private String noteId;
	
	private String filePath;
	
	/** �ļ����ͣ�0ͼƬ 1��Ƶ */
	private int fileType;

	public String getFileId() {
		return fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

	public String getNoteId() {
		return noteId;
	}

	public void setNoteId(String noteId) {
		this.noteId = noteId;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public int getFileType() {
		return fileType;
	}

	public void setFileType(int fileType) {
		this.fileType = fileType;
	}
	
}
