package com.duoduoapp.meitu.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

import android.os.Environment;

import com.duoduoapp.meitu.itf.IData;

public class HttpUtil{

	public String getHttp(String surl) throws IOException {
		Logger.debug(surl);
		URL url = new URL(surl);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		// 然后, 设置请求方式为GET方式，就是相当于浏览器打开网页
		connection.setRequestMethod("GET");
		// 接着设置超时时间为10秒，10秒内若连接不上，则通知用户网络连接有问题
		connection.setReadTimeout(5 * 1000);
		connection.setConnectTimeout(5 * 1000);
		connection.setRequestProperty("Referer", IData.BASE_REFERER);
		// 若连接上之后，得到网络的输入流，内容就是网页源码的字节码
		InputStream inStream = connection.getInputStream();
		BufferedReader bReader = new BufferedReader(new InputStreamReader(inStream));
		StringBuffer buffer = new StringBuffer();
		String line = null;
		while ((line = bReader.readLine()) != null) {
			buffer.append(line);
		}
		bReader.close();
		return buffer.toString();
	}

	public static String httpDownLoad(String bean, String name) {
		String url = bean;
		String filePath = "";
		filePath = IData.DEFAULT_IMAGE_FOLDER + name;
		FileUtil.creatFolder(filePath);
		String result = null;
		HttpURLConnection conn = null;
		InputStream inStream = null;
		try {
			URL uri = new URL(url);
			conn = (HttpURLConnection) uri.openConnection();
			// conn.setUseCaches(false);
			// conn.setDoInput(true);
			// conn.setDoOutput(false);
			conn.setConnectTimeout(10000);
			conn.setReadTimeout(10000);
			conn.setRequestProperty("Referer", IData.BASE_REFERER);
			// conn.connect();

			if (conn.getResponseCode() == 200) {

				if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
					File file = new File(filePath);
					FileOutputStream fos = null;
					try {
						fos = new FileOutputStream(file);
						inStream = conn.getInputStream();
						// int length = conn.getContentLength();
						byte[] b = new byte[8192];
						int j = 0;
						while ((j = inStream.read(b)) != -1) {
							fos.write(b, 0, j);
						}
						result = filePath;
					} catch (FileNotFoundException e) {
						e.printStackTrace();
						Logger.error(e);
					} catch (IOException e) {
						e.printStackTrace();
						Logger.error(e);
					} finally {
						if (fos != null) {
							try {
								fos.close();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}

				}
			} else {
				Logger.debug("http download response code:" + conn.getResponseCode());
			}
		} catch (Exception e) {
			Logger.error(e);
			if (inStream != null) {
				try {
					inStream.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			if (conn != null) {
				conn.disconnect();
			}

		}

		return result;
	}


	public String getJson(String uri) throws IOException {
		return getJson(uri, false);
	}

	public String getJson(String uri, boolean referer) throws IOException {
		URL url = new URL(uri);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		// 然后, 设置请求方式为GET方式，就是相当于浏览器打开网页
		connection.setRequestMethod("GET");
		// 接着设置超时时间为5秒，5秒内若连接不上，则通知用户网络连接有问题
		connection.setRequestProperty("accept", "*/*");
		connection.setRequestProperty("connection", "Keep-Alive");
		connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
		connection.setReadTimeout(10 * 1000);
		connection.setConnectTimeout(10 * 1000);
//		if (referer) {
//			connection.setRequestProperty("referer", REFERER);
//		}
		// 若连接上之后，得到网络的输入流，内容就是网页源码的字节码
		InputStream inStream = connection.getInputStream();
		BufferedReader bReader = new BufferedReader(new InputStreamReader(inStream));
		StringBuffer result = new StringBuffer();
		String line = null;
		while ((line = bReader.readLine()) != null) {
			result.append(line);
		}
		connection.disconnect();
		bReader.close();
		inStream.close();
		return toUtf8(result.toString());

	}
	public static String toUtf8(String str) {
		String result = null;
		try {
			result = new String(str.getBytes("UTF-8"), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

}
