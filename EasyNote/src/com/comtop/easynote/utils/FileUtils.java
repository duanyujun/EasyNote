package com.comtop.easynote.utils;

import java.io.File;
import java.io.IOException;

import android.os.Environment;

public class FileUtils {

	public static final String SD_PATH = Environment.getExternalStorageDirectory()+"/";
	
	public static final String APP_PATH = SD_PATH + "com.comtop.easynote";
	
	public static final String APP_ATTACH_PATH = SD_PATH + "/files";
	
	/**
	 * �ж��ļ��Ƿ����
	 * @param filepath
	 * @return
	 */
	public static boolean checkFileExists(String filepath) {
		File file = new File(filepath);
		return file.exists();
	}

	/**
	 * ����Ŀ¼
	 * @param dirpath
	 * @return
	 */
	public File createDIR(String dirPath) {
	    File dir=new File(dirPath);
	    dir.mkdir();
	    return dir;
	}
	
	/**
	 * �����ļ�
	 * @param filepath
	 * @return
	 * @throws IOException
	 */
	public File createFile(String filepath) throws IOException {
		File file = new File(filepath);
		file.createNewFile();
		return file;
	}
	
}
