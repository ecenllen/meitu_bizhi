package com.duoduoapp.meitu.cache;

import java.io.File;
import java.io.Serializable;

/**
 * SDcard缓存 ,保存对象, 需要保存在文件的对象,必须实现{@link Serializable} 接口
 * 
 * @author Administrator
 * 
 */
public interface IDiskCache<V extends Serializable> {

	/**
	 * 把序列化对象存入文件中
	 * 
	 * @param v
	 *            对象
	 * @param path
	 *            路径
	 */
	public void save(V v, File file);

	/**
	 * 从文件中获取序列化对象<br>
	 * 文件必须是序列化对象保存的文件
	 * 
	 * @param path
	 * @return
	 */
	public V get(File file);

	/**
	 * 删除文件
	 * 
	 * @param file
	 */
	public void remove(File file);

	/**
	 * 清除
	 */
	public void clear();
}
