package com.comtop.easynote.constants;

import java.util.HashMap;
import java.util.Map;

public final class Constants {

	public static final String USER_NAME = "userName";

	public static final String PASSWORD = "password";
	
	public static final String SHARE_NAME = "myLoginInfo";
	
	public final static String RSA_MODULUS = "009AD5BED30B11E6FB60769DE68558FC69";
	
	public final static String RSA_PUBLIC_EXPONENT = "010001";
	
	public final static String RSA_PRIVATE_EXPONENT = "62B65EE041B07A274AF3BC81985CD301";
	
	public final static String DEFAULT_TITLE = "Œﬁ±ÍÃ‚";
	
	public final static int RESULT_PHOTO = 1;
	
	public final static int RESULT_STT = 2;
	
	public final static int RESULT_AUDIO = 3;
	
	public final static int RESULT_IMAGE = 4;
	
	public static final Map<String, String> nameAndMine = new HashMap<String, String>();

	static {
		nameAndMine.put("apk", "application/vnd.android.package-archive");
		nameAndMine.put("jpg", "image/jpeg");
		nameAndMine.put("jpeg", "image/jpeg");
		nameAndMine.put("gif", "image/gif");
		nameAndMine.put("png", "image/png");
		nameAndMine.put("doc", "application/msword");
		nameAndMine.put("docx","application/vnd.openxmlformats-officedocument.wordprocessingml.document");
		nameAndMine.put("ppt", "application/mspowerpoint");
		nameAndMine.put("pptx", "application/mspowerpoint");
		nameAndMine.put("pdf", "application/pdf");
		nameAndMine.put("xls", "application/msexcel");
		nameAndMine.put("xlsx", "application/msexcel");
		nameAndMine.put("swf", "application/x-shockwave-flash");
		nameAndMine.put("mp3", "audio/mp3");
		nameAndMine.put("wma", "audio/x-ms-wma");
		nameAndMine.put("wav", "audio/x-wav");
		nameAndMine.put("amr", "audio/amr");
		nameAndMine.put("txt", "text/plain");
		nameAndMine.put("tiff", "image/tiff");
		nameAndMine.put("bmp", "image/bmp");
		nameAndMine.put("html", "text/html");
		nameAndMine.put("htm", "text/html");
		nameAndMine.put("mp4", "video/mp4");
		nameAndMine.put("java", "text/html");
		nameAndMine.put("ogg", "audio/amr-wb");
		nameAndMine.put("3gpp", "audio/3gpp");
		nameAndMine.put("aac", "audio/aac");
		nameAndMine.put("mpo", "image/mpo");
	}
}
