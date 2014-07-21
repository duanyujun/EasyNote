package com.comtop.easynote.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.Environment;

public class FileUtils {

	public static final String SD_PATH = Environment.getExternalStorageDirectory()+"/";
	
	public static final String APP_PATH = SD_PATH + "com.comtop.easynote";
	
	public static final String APP_ATTACH_PATH = APP_PATH + "/files";
	
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
	public static File createDIR(String dirPath) {
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
	public static File createFile(String filepath) throws IOException {
		File file = new File(filepath);
		file.createNewFile();
		return file;
	}
	
	public static int listFiles(String filepath){
		File file = new File(filepath);
		if(file.exists()){
			return file.list().length;
		}else{
			return 0;
		}
	}
	
	public static List<File> listFilesInDir(String filepath){
		List<File> listFile = new ArrayList<File>();
		File file = new File(filepath);
		if(file.exists()){
			File[] files = file.listFiles();
			for(File temp : files){
				listFile.add(temp);
			}
		}
		return listFile;
	}
	
	/**
	 * ����ѡ��õ�ͼƬ
	 * @param bitmap
	 * @param path
	 * @param fileName
	 * @return
	 */
	public static String savePicToSdcard(Bitmap bitmap, String path,
			String fileName) {
		String filePath = "";
		if (bitmap == null) {
			return filePath;
		} else {

			filePath=path+ "/" + fileName;
			File destFile = new File(filePath);
			OutputStream os = null;
			try {
				os = new FileOutputStream(destFile);
				bitmap.compress(CompressFormat.JPEG, 100, os);
				os.flush();
				os.close();
			} catch (IOException e) {
				filePath = "";
			}
		}
		return filePath;
	}
	
}
