package com.duoduoapp.meitu.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.duoduoapp.meitu.itf.IData;

public class FileUtil{

	/**
	 * 根据路径创建文件夹 文件夹以"/"结尾,不然就默认文件
	 * 
	 * @param path
	 *            路径
	 */
	public static void creatFolder(String path) {

		String pa = path.replace(IData.SDCARD, "");
		String[] files = pa.split(File.separator);

		String p = "";
		for (int i = 0; i < files.length; i++) {
			if ("".equals(files[i])) {
				continue;
			}
			p = p + File.separator + files[i];
			File file = new File(IData.SDCARD + p);
			if (!file.exists()) {
				file.mkdir();
			}
			//Logger.debug("creat folder:" + p);
		}

	}

	/**
	 * 保存文件
	 * 
	 * @param file
	 * @param path
	 *            保存路径加名称
	 * @throws IOException
	 */
	public static String saveFile(File file, String path) throws IOException {
		String re = "";
		re = saveFile(file, new File(path));
		return re;
	}

	public static String saveFile(File in, File out) throws IOException {
		String re = "";
		FileInputStream fis = new FileInputStream(in);
		FileOutputStream fos = new FileOutputStream(out);
		byte[] b = new byte[8192];
		int j = 0;
		while ((j = fis.read(b)) != -1) {
			fos.write(b, 0, j);
		}
		re = out.getAbsolutePath();
		fos.flush();
		fos.close();
		fis.close();
		Logger.debug("文件保存路径:"+re);
		return re;
	}
}
