package com.comtop.easynote.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.Environment;

public class FileUtils {

	public static final String SD_PATH = Environment.getExternalStorageDirectory()+"/";
	
	public static final String APP_PATH = SD_PATH + "com.comtop.easynote";
	
	public static final String APP_ATTACH_PATH = APP_PATH + "/files";
	
	/**
	 * 判断文件是否存在
	 * @param filepath
	 * @return
	 */
	public static boolean checkFileExists(String filepath) {
		File file = new File(filepath);
		return file.exists();
	}

	/**
	 * 创建目录
	 * @param dirpath
	 * @return
	 */
	public static File createDIR(String dirPath) {
	    File dir=new File(dirPath);
	    dir.mkdir();
	    return dir;
	}
	
	/**
	 * 创建文件
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
	
	/**
	 * 保存选择好的图片
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
