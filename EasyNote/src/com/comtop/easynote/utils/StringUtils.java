/******************************************************************************
 * Copyright (C) 2014 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * �����Ϊ���ڿ����տ������ơ�δ������˾��ʽ����ͬ�⣬�����κθ��ˡ����岻��ʹ�á�
 * ���ơ��޸Ļ򷢲������.
 *****************************************************************************/

package com.comtop.easynote.utils;

/**
 * �ַ�����������
 * @since JDK1.6
 */
public class StringUtils {
	
	public static boolean isBlank(String str) {
		if ((str == null) || (str.length() == 0)) {
			return true;
		}
		int strLen = str.length();
		for (int i = 0; i < strLen; i++) {
			if (!Character.isWhitespace(str.charAt(i))) {
				return false;
			}
		}
		return true;
	}

	public static boolean isNotBlank(String str) {
		if ((str == null) || (str.length() == 0)) {
			return false;
		}
		int strLen = str.length();
		for (int i = 0; i < strLen; i++) {
			if (!Character.isWhitespace(str.charAt(i))) {
				return true;
			}
		}
		return false;
	}
	
	public static String deleteDotZero(String str){
		if(str == null || str.length() == 0)
			return str;
		if(str.contains(".0")){
			int iDotStartIdx = str.indexOf(".0");
			str = str.substring(0, iDotStartIdx);
		}
		
		return str;
	}
	
	public static boolean isEmpty(String str) {
		return (str == null) || (str.length() == 0);
	}

	public static boolean isNotEmpty(String str) {
		return (str != null) && (str.length() > 0);
	}
}