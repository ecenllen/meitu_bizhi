package com.duoduoapp.meitu.utils;




import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class PlayerFileUtil {

	/**
	 * 根据路径创建文件夹 文件夹以"/"结尾,不然就默认文件
	 * 
	 * @param path
	 *            路径
	 */
	public static void creatFolder(String path) {
		File file = new File(path);
		if (!file.exists()) {
			Boolean isSuccess=file.mkdirs();
			if(!isSuccess)
			{
				System.out.println("新建文件夹失败:"+file.getPath());
			}
			Logger.debug("creat folder:" + file.getAbsolutePath());
		}
//		String pa = path.replace(SDCARD, "");
//		String[] files = pa.split(File.separator);
//
//		String p = "";
//		for (int i = 0; i < files.length; i++) {
//			if ("".equals(files[i])) {
//				continue;
//			}
//			p = p + File.separator + files[i];
//			File file = new File(SDCARD + p);
//			if (!file.exists()) {
//				Boolean isSuccess=file.mkdirs();
//				if(!isSuccess)
//				{
//					System.out.println("新建文件夹失败:"+file.getPath());
//				}
//				Logger.debug("creat folder:" + file.getAbsolutePath());
//			}
//		}

	}

	/**
	 * 删除目录（文件夹）以及目录下的文件
	 * 
	 * @param sPath
	 *            被删除目录的文件路径
	 * @return 目录删除成功返回true，否则返回false
	 */
	public static boolean deleteDirectory(String sPath) {
		boolean flag = false;
		// 如果sPath不以文件分隔符结尾，自动添加文件分隔符
		if (!sPath.endsWith(File.separator)) {
			sPath = sPath + File.separator;
		}
		File dirFile = new File(sPath);
		// 如果dir对应的文件不存在，或者不是一个目录，则退出
		if (!dirFile.exists()) {
			return false;
		}
		if (!dirFile.isDirectory()) {
			return false;
		} else if (dirFile.isFile()) {
			return deleteFile(sPath);
		}
		flag = true;
		// 删除文件夹下的所有文件(包括子目录)
		File[] files = dirFile.listFiles();
		for (int i = 0; i < files.length; i++) {
			// 删除子文件
			if (files[i].isFile()) {
				flag = deleteFile(files[i].getAbsolutePath());
				if (!flag){
					break;
				}

			} // 删除子目录
			else {
				flag = deleteDirectory(files[i].getAbsolutePath());
				if (!flag){
					break;
				}

			}
		}
		if (!flag){
			return false;
		}
		// 删除当前目录
		if (dirFile.delete()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 删除文件
	 * 
	 * @param sPath
	 * @return
	 */
	public static boolean deleteFile(String sPath) {
		boolean flag = false;
		File file = new File(sPath);
		// 路径为文件且不为空则进行删除
		if (file.isFile() && file.exists()) {
			file.delete();
			flag = true;
		}
		return flag;
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
		Logger.debug("文件保存路径:" + re);
		return re;
	}
}
