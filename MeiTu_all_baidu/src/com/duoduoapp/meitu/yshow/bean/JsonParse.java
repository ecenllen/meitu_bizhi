package com.duoduoapp.meitu.yshow.bean;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class JsonParse {

	public static byte[] readInputStream(InputStream instream) throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = 0;
		while ((len = instream.read(buffer)) != -1) {
			bos.write(buffer, 0, len);
		}
		return bos.toByteArray();
	}

	/*
	 * ��ȡKK��Ƶ��ķ����б�
	 */
	public static kkjsonModel ParseKKChangXiangModel(String kkurl) throws Exception {
		// kkurl = URLEncoder.encode(kkurl, "utf-8");
		InputStream inStream = null;
		URL url = null;
		try {
			url = new URL(kkurl);
			inStream = url.openStream();
			byte[] data = readInputStream(inStream);
			String jsonstr = new String(data);
			kkjsonModel programs = ParseKKChangXiangModelByJson(jsonstr);
			return programs;
		} catch (Exception ex) {
			System.out.println("...1128..." + ex.toString());
			ex.printStackTrace();
			return null;
		} finally {
			if (inStream != null) {
				try {
					inStream.close();
				} catch (Exception ex) {

				}
			}
		}
	}

	private static kkjsonModel ParseKKChangXiangModelByJson(String jonstr) {

		Gson gson = new Gson();
		kkjsonModel ps = gson.fromJson(jonstr, new TypeToken<kkjsonModel>() {}.getType());
		return ps;
	}
}
