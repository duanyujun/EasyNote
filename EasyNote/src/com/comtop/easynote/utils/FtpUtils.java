package com.comtop.easynote.utils;

import java.io.IOException;
import java.net.SocketException;
import java.util.List;

import org.apache.commons.net.ftp.FTPClient;
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
	
	public static final void uploadFile(List<FileVO> listFiles){
		if(listFiles==null || listFiles.size()==0){
			return;
		}
		
		
	}
	
}
