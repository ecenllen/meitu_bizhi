package com.duoduoapp.meitu.cache;

import java.util.Set;

/**
 * 内存数据,缓存接口
 * 
 * @author Administrator
 * 
 * @param <V>
 *            泛型,你想缓存的数据对象
 */
public interface IMemoryCache<V> {

	/**
	 * 得到这个数据
	 * 
	 * @param key
	 * @return
	 */
	public V get(Object key);

	/**
	 * 设置这个数据
	 * 
	 * @param key
	 * @param values
	 */
	public void put(Object key, V values);

	/**
	 * 是否有这个key
	 * 
	 * @param key
	 * @return
	 */
	public boolean containsKey(Object key);

	/**
	 * 清除所有缓存
	 */
	public void clear();

	/**
	 * 移除缓存
	 * 
	 * @param key
	 */
	public void remove(Object key);

	/**
	 * 大小
	 * 
	 * @return
	 */
	public int size();

	/**
	 * 获取keys
	 * 
	 * @return
	 */
	public Set<Object> getKeys();
}
