package com.duoduoapp.meitu.cache;

import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;
////////////////////////////////////////////////////////////////
//
//                             _ooOoo_
//                            o8888888o
//                            88" . "88
//                            (| -_- |)
//                            O\  =  /O
//                         ____/`---'\____
//                       .'  \\|     |//  `.
//                      /  \\|||  :  |||//  \
//                     /  _||||| -:- |||||-  \
//                     |   | \\\  -  /// |   |
//                     | \_|  ''\---/''  |   |
//                     \  .-\__  `-`  ___/-. /
//                   ___`. .'  /--.--\  `. . __
//                ."" '<  `.___\_<|>_/___.'  >'"".
//               | | :  `- \`.;`\ _ /`;.`/ - ` : | |
//               \  \ `-.   \_ __\ /__ _/   .-` /  /
//          ======`-.____`-.___\_____/___.-`____.-'======
//                             `=---='
//          ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
//                     	佛祖保佑           永无BUG							
//            佛曰:  
//                   写字楼里写字间，写字间里程序员；  
//                   程序人员写程序，又拿程序换酒钱。  
//                   酒醒只在网上坐，酒醉还来网下眠；  
//                   酒醉酒醒日复日，网上网下年复年。  
//                   但愿老死电脑间，不愿鞠躬老板前；  
//                   奔驰宝马贵者趣，公交自行程序员。  
//                   别人笑我忒疯癫，我笑自己命太贱；  
//                   不见满街漂亮妹，哪个归得程序员？  
//
////////////////////////////////////////////////////////////////

/**
 * 
 * 缓存 ,线程安全的缓存
 * 
 * @author Administrator
 * @param <V>泛型,需要保存在内存中的对象
 */
public class VideoMemoryCache<V> implements IMemoryCache<V> {

	private final HashMap<Object, V>	cacheMap;
	private ReentrantLock				lock	= new ReentrantLock();	// 线程锁,保证线程安全的

	public VideoMemoryCache() {
		cacheMap = new HashMap<Object, V>();
	}

	@Override
	public V get(Object key) {
		V v = null;
		try {
			lock.tryLock();
			v = cacheMap.containsKey(key) ? cacheMap.get(key) : null;
		} finally {
			lock.unlock();
		}
		return v;
	}

	@Override
	public void put(Object key, V value) {
		try {
			lock.tryLock();
			cacheMap.put(key, value);
		} finally {
			lock.unlock();
		}
	}

	@Override
	public void clear() {
		try {
			lock.tryLock();
			cacheMap.clear();
		} finally {
			lock.unlock();
		}
	}

	@Override
	public void remove(Object key) {
		try {
			lock.tryLock();
			if (cacheMap.containsKey(key)) {
				cacheMap.remove(key);
			}
		} finally {
			lock.unlock();
		}
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		int size = 0;
		try {
			lock.tryLock();
			size = cacheMap.size();
		} finally {
			lock.unlock();
		}
		return size;
	}

	@Override
	public boolean containsKey(Object key) {
		boolean contins = false;
		try {
			lock.tryLock();
			contins = cacheMap.containsKey(key);
		} finally {
			lock.unlock();
		}
		return contins;
	}

	@Override
	public Set<Object> getKeys() {
		Set<Object> keys = null;
		try {
			lock.tryLock();
			keys = cacheMap.keySet();
		} finally {
			lock.unlock();
		}
		return keys;
	}

}
