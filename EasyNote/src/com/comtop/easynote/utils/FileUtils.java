package com.comtop.easynote.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
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
	
	/**
	 * ɾ��һ��Ŀ¼��
	 * @param path
	 */
	public static void delteDir(String path) {
		if (StringUtils.isBlank(path)) {
			return;
		}
		File file = new File(path);
		if(!file.exists()){
			return;
		}
		if (!file.isDirectory()) {
			file.delete();
		} else if (file.isDirectory()) {
			String[] filelist = file.list();
			for (int i = 0; i < filelist.length; i++) {
				File delfile = new File(path + "/" + filelist[i]);
				if (!delfile.isDirectory()) {
					delfile.delete();
				} else if (delfile.isDirectory()) {
					delteDir(path + "/" + filelist[i]);
				}
			}
			file.delete();
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
	 * ����Ŀ¼�µ��ļ������Ұ����ļ���������
	 * @param filepath
	 * @return
	 */
	public static List<File> listFilesInDirByType(String filepath){
		List<File> listFile = listFilesInDir(filepath);
		listFile = sortFileType(listFile);
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
	
	/**
	 * �����ļ���������
	 * @param listFiles
	 */
	public static final List<File> sortFileType(List<File> listFiles){
		if(listFiles==null || listFiles.size()==0)
			return listFiles;
		List<File> listReturn = new ArrayList<File>(listFiles.size());
		List<File> listSound = new ArrayList<File>();
		for(File file : listFiles){
			if(file.getName().endsWith(".jpg")){
				listReturn.add(file);
			}else{
				listSound.add(file);
			}
		}
		for(File file : listSound){
			listReturn.add(file);
		}
		return listReturn;
	}
	
}
