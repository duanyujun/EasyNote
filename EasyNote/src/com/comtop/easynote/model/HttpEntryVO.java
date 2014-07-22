package com.comtop.easynote.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 对List<NoteVO>进行封装
 * @author duanyujun
 *
 */
public class HttpEntryVO {

	private List<NoteVO> listNoteVO = new ArrayList<NoteVO>();

	public List<NoteVO> getListNoteVO() {
		return listNoteVO;
	}

	public void setListNoteVO(List<NoteVO> listNoteVO) {
		this.listNoteVO = listNoteVO;
	}
	
	
}
