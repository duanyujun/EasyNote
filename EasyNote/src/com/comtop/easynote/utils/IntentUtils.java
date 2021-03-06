package com.comtop.easynote.utils;

import com.comtop.easynote.R;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

public final class IntentUtils {

	public static final String openCamera(Activity paramActivity, String noteId,
			int paramInt, String userId) {
		

		// 新建当前noteId目录
		String noteIdDir = FileUtils.APP_ATTACH_PATH + "/" + userId + "/" + noteId;
		if (!FileUtils.checkFileExists(noteIdDir)) {
			FileUtils.createDIR(noteIdDir);
		}

		Intent localIntent = new Intent("android.media.action.IMAGE_CAPTURE");
		String strFile = null;
		try {
			strFile = noteIdDir + "/"
					+ StringUtils.getDataFormatFileName("img_") + ".jpg";
			localIntent.putExtra("output",
					Uri.fromFile(FileUtils.createFile(strFile)));
			paramActivity.startActivityForResult(localIntent, paramInt);
			return strFile;
		} catch (Exception localException1) {
			try {
				Toast.makeText(paramActivity,
						paramActivity.getString(R.string.intent_no_camera), 1)
						.show();
				return strFile;
			} catch (Exception localException2) {
			}
		}
		return strFile;
	}
	
	public static final void chooseImage(Activity paramActivity, String noteId,
			int paramInt, String userId) {
		// 新建当前noteId目录
		String noteIdDir = FileUtils.APP_ATTACH_PATH + "/" +userId + "/" + noteId;
		if (!FileUtils.checkFileExists(noteIdDir)) {
			FileUtils.createDIR(noteIdDir);
		}
		
		Intent intent = new Intent();  
        intent.setType("image/*");  
        intent.setAction(Intent.ACTION_GET_CONTENT);  
        paramActivity.startActivityForResult(intent, paramInt);  
		
	}
}
