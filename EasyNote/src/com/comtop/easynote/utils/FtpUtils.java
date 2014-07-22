package com.comtop.easynote.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.SocketException;
import java.util.List;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import com.comtop.easynote.model.FileVO;

public class FtpUtils {
	
	private static final String URL = "10.10.36.165";
	private static final int PORT = 21;
	private static final String USER_NAME = "android";
	private static final String PASSWORD = "android";
	
	public static final FTPClient getFtpClient() {
		FTPClient ftpClient = new FTPClient();
		boolean loginResult = false;
		try {
			ftpClient.connect(URL, PORT);
			loginResult = ftpClient.login(USER_NAME, PASSWORD);
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		int returnCode = ftpClient.getReplyCode();
		if (loginResult && FTPReply.isPositiveCompletion(returnCode)) {
			return ftpClient;
		} else {
			return null;
		}
	}
	
	public static final void disconnect(FTPClient ftpClient){
		if(ftpClient==null){
			return;
		}
		try {
			ftpClient.disconnect();
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	
	public static final void createDir(FTPClient ftpClient, String path){
		try {
			ftpClient.makeDirectory(path);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/***
	 * 一个个note依次上传，主要不让频繁的更改工作目录
	 * @param ftpClient
	 * @param listFiles 单个note下的文件列表
	 * @param userId 用户Id
	 */
	public static final void uploadFile(FTPClient ftpClient, List<FileVO> listFiles, String userId){
		if(listFiles==null || listFiles.size()==0){
			return;
		}
		String remotePath = "/files/"+userId+"/"+listFiles.get(0).getNoteId();
		FileInputStream fis;
		String fileName;
		try {
			ftpClient.changeWorkingDirectory(remotePath);
			//ftpClient.setBufferSize(1024);
			//ftpClient.setControlEncoding("UTF-8");
			ftpClient.enterLocalPassiveMode();
			for(FileVO fileVO : listFiles){
				fis = new FileInputStream(fileVO.getFilePath());
				fileName = fileVO.getFileId();
				ftpClient.storeFile(fileName, fis);
				fis.close();
				fis = null;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 下载一个note下面的附件
	 * @param ftpClient
	 * @param noteId
	 * @param userId
	 */
	public static final void downloadFile(FTPClient ftpClient, String noteId,
			String userId) {
		if (StringUtils.isBlank(userId)) {
			return;
		}
		String remotePath = "/files/" + userId + "/" + noteId;
		String localPath = FileUtils.APP_ATTACH_PATH + "/" + userId + "/"
				+ noteId;
		OutputStream is;
		try {
			ftpClient.changeWorkingDirectory(remotePath);
			ftpClient.setBufferSize(1024);
			ftpClient.setControlEncoding("UTF-8");
			ftpClient.enterLocalPassiveMode();
			FTPFile[] fs = ftpClient.listFiles();
			for (FTPFile ff : fs) {
				File localFile = new File(localPath + "/" + ff.getName());
				is = new FileOutputStream(localFile);
				ftpClient.retrieveFile(ff.getName(), is);
				is.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
