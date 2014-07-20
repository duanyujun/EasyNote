package com.comtop.easynote.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.comtop.common.BaseActivity;
import com.comtop.common.MyApplication;
import com.comtop.easynote.R;
import com.comtop.easynote.dao.NoteDAO;
import com.comtop.easynote.model.NoteVO;
import com.comtop.easynote.utils.DatabaseHelper;
import com.comtop.easynote.utils.StringUtils;
import com.comtop.easynote.view.UnderLineEditText;

public class NoteEditActivity extends BaseActivity {
	
	private LinearLayout llAttachlayout;
	private ImageButton ibToggleTool;
	private boolean isAttachLayoutShow = true;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.note_edit);
		
		llAttachlayout = (LinearLayout) findViewById(R.id.attachlayout);
		ibToggleTool = (ImageButton) findViewById(R.id.toggleToolBtn);
		ibToggleTool.setBackgroundDrawable(getResources().getDrawable(R.drawable.newdoc_ancor_up));
	}
	
	public void toggleAttachLayout(View view){
		if(isAttachLayoutShow){
			llAttachlayout.setVisibility(View.GONE);
			isAttachLayoutShow = false;
			ibToggleTool.setBackgroundDrawable(getResources().getDrawable(R.drawable.newdoc_ancor_down));
		}else{
			llAttachlayout.setVisibility(View.VISIBLE);
			isAttachLayoutShow = true;
			ibToggleTool.setBackgroundDrawable(getResources().getDrawable(R.drawable.newdoc_ancor_up));
		}
	}
	
}
