package com.duoduoapp.meitu.cache;


import com.duoduoapp.meitu.itf.IData;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;

/**
 * 序列化对象的存储与读取.
 * 
 * @author Administrator
 * 
 * @param <V>
 *            必须实现{@link Serializable} 接口
 */
public class VideoDiskCache<V extends Serializable> implements IDiskCache<V> {

	@Override
	public void save(V v, File f) {
		try {
			if (f.exists()) {
				f.delete();
			}
			OutputStream os = new FileOutputStream(f, true);
			ObjectOutputStream oos = new ObjectOutputStream(os);
			oos.writeObject(v);
			oos.flush();
			oos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("序列化失败"+f.getPath());

		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public V get(File f) {
		V v = null;
		try {
			if (!f.exists()) {
				return null;
			}
			InputStream is = new FileInputStream(f);
			ObjectInputStream ois = new ObjectInputStream(is);
			v = (V) ois.readObject();
			ois.close();
		} catch (Exception e) {
			e.printStackTrace();

			System.out.println("反序列化失败" + f.getPath());
			v = null;
		}
		return v;
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub

	}

	@Override
	public void remove(File file) {
		if (file.exists()) {
			file.delete();
		}
	}

}
