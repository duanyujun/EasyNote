package com.comtop.common.utils;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.BinaryHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * ʹ�ö�����UI���߳�֮���android-async-http��֧�ְ���������post��get�������ڲ��ǲ���Android Handler
 * message ������������Ϣ��
 * 
 */
public class HttpClient {
	/** ����ĸ�·�� */
	private static final String BASE_URL = "http://10.10.36.165:7088/web/lcam/project/mobile/material";

	/** ���ӳ�ʱʱ�� */
	private static final int TIMEOUT = 100000;

	/** ʵ����һ������AsyncHttpClient���� */
	private static AsyncHttpClient client = new AsyncHttpClient();

	/** ��������ʱʱ�� */
	static {
		client.addHeader("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
		client.setTimeout(TIMEOUT);
	}

	/**
	 * ������������·��ƴװ����Ȩ·��
	 * 
	 * @param relativeUrl
	 *            �������·��
	 * @return �����ȫ·��
	 */
	private static String getAbsoluteUrl(String relativeUrl) {
		return BASE_URL + relativeUrl;
	}

	/**
	 * ����url���·��(��������)������Դ
	 * 
	 * @param relativeUrl
	 *            ���·��
	 * @param responseHandler
	 *            AsyncHttpResponseHandler
	 */
	public static void get(String relativeUrl, AsyncHttpResponseHandler responseHandler) {
		client.get(getAbsoluteUrl(relativeUrl), responseHandler);
	}
	
	public static void post(String relativeUrl, RequestParams params, AsyncHttpResponseHandler responseHandler) {
		client.post(getAbsoluteUrl(relativeUrl), params, responseHandler);
	}

	/**
	 * ����url���·��(������)������Դ
	 * 
	 * @param relativeUrl
	 *            ���·��
	 * @param params
	 *            ����
	 * @param responseHandler
	 *            AsyncHttpResponseHandler
	 */
	public static void get(String relativeUrl, RequestParams params, AsyncHttpResponseHandler responseHandler) {
		client.get(getAbsoluteUrl(relativeUrl), params, responseHandler);
	}

	/**
	 * ����url���·�����������������أ�����byte��������
	 * 
	 * @param relativeUrl
	 *            ���·��
	 * @param ResponseHandler
	 *            BinaryHttpResponseHandler
	 */
	public static void get(String relativeUrl, BinaryHttpResponseHandler responseHandler) {
		client.get(getAbsoluteUrl(relativeUrl), responseHandler);
	}

	/**
	 * ����url���·���������������� ������byte������
	 * 
	 * @param relativeUrl
	 *            ���·��
	 * @param params
	 *            ����
	 * @param responseHandler
	 *            BinaryHttpResponseHandler
	 */
	public static void get(String relativeUrl, RequestParams params, BinaryHttpResponseHandler responseHandler) {
		client.get(getAbsoluteUrl(relativeUrl), params, responseHandler);
	}
}
