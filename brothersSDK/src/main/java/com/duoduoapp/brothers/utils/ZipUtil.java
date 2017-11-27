package com.duoduoapp.brothers.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import android.content.Context;
import android.os.Environment;

public class ZipUtil {

	private Context context;
	private static final String fileName = "result.json";
	private static final String zipName = "ddapp_tmp.zip";

	public ZipUtil(Context context) {
		this.context = context;
	}

	public boolean zipFile() {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			String path = Environment.getExternalStorageDirectory().getPath();
			File file = new File(path + "/ddapp_tmp.zip");
			if (file.exists())
				file.delete();
			return zipFileWithTier(path + "/", path + "/ddapp_tmp.zip");
		} else {
			File file = new File(context.getFilesDir().getPath() + "/"
					+ zipName);
			if (file.exists())
				file.delete();
			return zipFileWithTier(context.getFilesDir().getPath() + "/",
					context.getFilesDir().getPath() + "/ddapp_tmp.zip");
		}
	}

	public static void upZipFile(String file) {
		try {
			unzipFilesWithTier(readFileByte(file), file + File.separator);
		} catch (IOException e) {

			e.printStackTrace();
		}
	}

	/*
	 * Compress the specify file that contains sub-folders and store them to a
	 * zipfile.
	 * 
	 * @param srcFiles: the file will be compressed
	 * 
	 * @param zipPath: the location which stores the zipfile.
	 */
	public static boolean zipFileWithTier(String srcFiles, String zipPath) {
		try {
			FileOutputStream zipFile = new FileOutputStream(zipPath);
			BufferedOutputStream buffer = new BufferedOutputStream(zipFile);
			ZipOutputStream out = new ZipOutputStream(buffer);
			File file1 = new File(srcFiles + "result.json");
			if (file1.exists()) {
				zipFiles(srcFiles + "result.json", out, "");
				out.close();
				return true;
			}
			out.close();
			return false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}

	/*
	 * Recursive the specify file also contains the folder which may don't
	 * include any file.
	 * 
	 * @param filePath: compress file
	 * 
	 * @param ZipOutputStream: the zipfile's outputStream.
	 * 
	 * @param prefix: the prefix indicates the parent folder name of the file
	 * that makes the tier relation.
	 */
	public static void zipFiles(String filePath, ZipOutputStream out,
			String prefix) throws IOException {
		File file = new File(filePath);
		if (file.isDirectory()) {
			if (file.listFiles().length == 0) {
				ZipEntry zipEntry = new ZipEntry(prefix + file.getName() + "/");
				out.putNextEntry(zipEntry);
				out.closeEntry();
			} else {
				prefix += file.getName() + File.separator;
				for (File f : file.listFiles())
					zipFiles(f.getAbsolutePath(), out, prefix);
			}
		} else {
			FileInputStream in = new FileInputStream(file);
			ZipEntry zipEntry = new ZipEntry(prefix + file.getName());
			out.putNextEntry(zipEntry);
			byte[] buf = new byte[1024];
			int len;
			while ((len = in.read(buf)) > 0) {
				out.write(buf, 0, len);
			}
			out.closeEntry();
			in.close();
		}

	}

	/*
	 * Unzip the file also contains the folder which the listFiles's length is
	 * 0.
	 * 
	 * @param bytes: the content of the zipfile by byte array. *
	 * 
	 * @param prefix: the prefix is the root of the store path.
	 * 
	 * @IOExcetion: the ioexception during unzipFiles.
	 */
	public static void unzipFilesWithTier(byte[] bytes, String prefix)
			throws IOException {

		InputStream bais = new ByteArrayInputStream(bytes);
		ZipInputStream zin = new ZipInputStream(bais);
		ZipEntry ze;
		while ((ze = zin.getNextEntry()) != null) {
			if (ze.isDirectory()) {
				File file = new File(prefix + ze.getName());
				if (!file.exists())
					file.mkdirs();
				continue;
			}
			File file = new File(prefix + ze.getName());
			if (!file.getParentFile().exists())
				file.getParentFile().mkdirs();
			ByteArrayOutputStream toScan = new ByteArrayOutputStream();
			byte[] buf = new byte[1024];
			int len;
			while ((len = zin.read(buf)) > 0) {
				toScan.write(buf, 0, len);
			}
			byte[] fileOut = toScan.toByteArray();
			toScan.close();
			writeByteFile(fileOut, new File(prefix + ze.getName()));
		}
		zin.close();
		bais.close();
	}

	@SuppressWarnings("resource")
	public static byte[] readFileByte(String filename) throws IOException {

		if (filename == null || filename.equals("")) {
			throw new NullPointerException("File is not exist!");
		}
		File file = new File(filename);
		long len = file.length();
		byte[] bytes = new byte[(int) len];

		BufferedInputStream bufferedInputStream = new BufferedInputStream(
				new FileInputStream(file));
		int r = bufferedInputStream.read(bytes);
		if (r != len)
			throw new IOException("Read file failure!");
		bufferedInputStream.close();

		return bytes;

	}

	public static String writeByteFile(byte[] bytes, File file) {
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(file);
			fos.write(bytes);
		} catch (FileNotFoundException e) {
			e.printStackTrace();

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return "success";
	}

	static final int BUFFER = 2048;

	public static boolean uzip(String fileName, String filePath) {
		boolean result = false;
		try {

			ZipFile zipFile = new ZipFile(fileName);
			Enumeration emu = zipFile.entries();
			int i = 0;
			while (emu.hasMoreElements()) {
				ZipEntry entry = (ZipEntry) emu.nextElement();
				// 会把目录作为一个file读出一次，所以只建立目录就可以，之下的文件还会被迭代到。
				if (entry.isDirectory()) {
					new File(filePath + entry.getName()).mkdirs();
					continue;
				}
				BufferedInputStream bis = new BufferedInputStream(
						zipFile.getInputStream(entry));
				File file = new File(filePath + entry.getName());
				// 加入这个的原因是zipfile读取文件是随机读取的，这就造成可能先读取一个文件
				// 而这个文件所在的目录还没有出现过，所以要建出目录来。
				File parent = file.getParentFile();
				if (parent != null && (!parent.exists())) {
					parent.mkdirs();
				}
				FileOutputStream fos = new FileOutputStream(file);
				BufferedOutputStream bos = new BufferedOutputStream(fos, BUFFER);

				int count;
				byte data[] = new byte[BUFFER];
				while ((count = bis.read(data, 0, BUFFER)) != -1) {
					bos.write(data, 0, count);
				}
				bos.flush();
				bos.close();
				bis.close();
			}
			zipFile.close();
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public boolean writeJsonFile(String jsonData) {
		boolean result = false;
		try {
			if (Environment.getExternalStorageState().equals(
					Environment.MEDIA_MOUNTED)) {
				String filePath = Environment.getExternalStorageDirectory()
						+ "/result.json";
				File file = new File(filePath);
				if (file.exists()) {
					file.delete();
				}
				file.createNewFile();
				FileWriter fw = new FileWriter(filePath);
				PrintWriter out = new PrintWriter(fw);
				out.write(jsonData);
				out.println();
				fw.close();
				out.close();
				result = true;
			} else {
				File file = new File(context.getFilesDir().getPath() + "/"
						+ fileName);
				if (file.exists())
					file.delete();
				FileOutputStream out = context.openFileOutput(fileName,
						Context.MODE_PRIVATE);
				out.write(jsonData.getBytes());
				out.flush();
				out.close();
				result = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public void deleteFile() {
		try {
			String filePath = Environment.getExternalStorageDirectory()
					+ "/result.json";
			File file = new File(Environment.getExternalStorageDirectory()
					+ "/ddapp_tmp.zip");
			File file1 = new File(filePath);
			if (file1.exists())
				file1.delete();
			if (file.exists())
				file.delete();
		} catch (Exception e) {
		}
	}

}