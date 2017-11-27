package com.duoduoapp.brothers.utils;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpUtil {

	public static final String TAG = HttpUtil.class.getName();

	public static String getHtmlStr(String surl) throws IOException {
		return getHtmlStr(surl, "UTF-8");
	}

	public static String getHtmlStr(String surl, String charset) throws IOException {
		URL url = new URL(surl);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		// 然后, 设置请求方式为GET方式，就是相当于浏览器打开网页
		connection.setRequestMethod("GET");
		// 接着设置超时时间为5秒，5秒内若连接不上，则通知用户网络连接有问题
		connection.setReadTimeout(5000);
		connection.setConnectTimeout(5000);
		// 若连接上之后，得到网络的输入流，内容就是网页源码的字节码
		InputStream inStream = connection.getInputStream();
		BufferedReader bReader = new BufferedReader(new InputStreamReader(inStream));
		String result = "";
		String line = null;
		while ((line = bReader.readLine()) != null) {
			result += line;
		}
		bReader.close();
		return result;
	}

	/**
	 * 下载文件
	 * 
	 * @param uri
	 *            文件链接
	 * @param filePath
	 *            文件存放路径
	 * @throws IOException
	 */
	public static boolean downLoadFile(String uri, String filePath) {
		OutputStream os = null;
		InputStream is = null;
		try {

			URL url = new URL(uri);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			// connection.setRequestProperty("User-Agent",
			// "Mozilla/4.0(compatible;MSIE 5.0;Windows NT;DigExt)");
			connection.setReadTimeout(5000);
			connection.setConnectTimeout(5000);
			is = connection.getInputStream();
			os = new FileOutputStream(filePath);
			if (is == null || os == null) {
				return false;
			}
			byte[] bs = new byte[8192];
			int len = 0;
			while ((len = is.read(bs)) != -1) {
				os.write(bs, 0, len);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			if (os != null) {
				try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return true;

	}
}
