package com.duoduoapp.brothers.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.Log;

import com.duoduoapp.brothers.bean.LeagueInfo;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

public class DBHelper extends OrmLiteSqliteOpenHelper {

	public static final String DB_NAME = "kupaogonglue";

	public static final int VERSION = 2;

	private static final String TAG = DBHelper.class.getName();

	public static DBHelper dbHelper = null;

	private RuntimeExceptionDao<LeagueInfo, Integer> homeDao = null;

	private static HashMap<String, DBChangeListener> listeners = new HashMap<String, DBChangeListener>();

	public DBHelper(Context context) {

		super(context, DB_NAME, null, VERSION);
	}

	public DBHelper(Context context, String databaseName,
			CursorFactory factory, int databaseVersion) {
		super(context, databaseName, factory, databaseVersion);
	}

	public static DBHelper getInstance(Context context) {
		if (dbHelper == null) {
			dbHelper = new DBHelper(context);
		}
		return dbHelper;
	}

	@SuppressWarnings("rawtypes")
	public void setListener(Class clazz, DBChangeListener listener) {
		if (listener != null) {
			listeners.put(clazz.getName(), listener);
		}
	}

	@SuppressWarnings("rawtypes")
	public void removeListner(Class clazz) {
		if (listeners != null) {
			listeners.remove(clazz.getName());
		}
	}

	@Override
	public void onCreate(SQLiteDatabase database, ConnectionSource source) {
		try {

			TableUtils.createTable(connectionSource, LeagueInfo.class);
		} catch (Exception e) {
			e.printStackTrace();
			Log.e(TAG, "Creat table error!!!");
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase database, ConnectionSource source,
			int arg2, int arg3) {
		try {
			TableUtils.dropTable(connectionSource, LeagueInfo.class, true);
		} catch (Exception e) {
			e.printStackTrace();
			Log.e(TAG, "Upgrade table error!!!");
		}
	}

	public RuntimeExceptionDao<LeagueInfo, Integer> getLeagueInfoDao() {
		if (homeDao == null) {
			homeDao = getRuntimeExceptionDao(LeagueInfo.class);
		}
		return homeDao;
	}

	public void insertLeagueInfo(LeagueInfo info){
		RuntimeExceptionDao<LeagueInfo, Integer> dao = getLeagueInfoDao();
		dao.create(info);
		notifyDBChanged();
	}
	/**
	 * 增加数据
	 * 
	 * @param infos
	 */
	public void insertLeagueInfo(List<LeagueInfo> infos) {
		RuntimeExceptionDao<LeagueInfo, Integer> dao = getLeagueInfoDao();
		for (LeagueInfo info : infos) {
			dao.create(info);
		}
		notifyDBChanged();
	}

	public void deleteLeagueInfo(LeagueInfo info) {
		RuntimeExceptionDao<LeagueInfo, Integer> dao = getLeagueInfoDao();
		dao.delete(info);
		notifyDBChanged();
	}
	/**
	 * 删除
	 * 
	 * @param infos
	 */
	public void deleteLeagueInfo(List<LeagueInfo> infos) {
		RuntimeExceptionDao<LeagueInfo, Integer> dao = getLeagueInfoDao();
		for (LeagueInfo info : infos) {
			dao.delete(info);
		}
		notifyDBChanged();
	}

	/**
	 * 更新数据库favorites
	 * 
	 * @param LeagueInfos
	 */
	public void updateLeagueInfo(LeagueInfo LeagueInfo) {
		RuntimeExceptionDao<LeagueInfo, Integer> dao = getLeagueInfoDao();
		dao.update(LeagueInfo);
	}

	public List<LeagueInfo> queryLeagueInfos(){
		List<LeagueInfo> LeagueInfos = new ArrayList<LeagueInfo>();
		LeagueInfos = getLeagueInfoDao().queryForAll();
		
		return LeagueInfos ;
	}
	
	/**
	 * 通知注册监听的,数据库更新了
	 */
	private void notifyDBChanged() {
		Set<String> key = listeners.keySet();
		for (String listener : key) {
			if (listener == null) {
				continue;
			}
			if (listeners.get(listener) != null) {
				listeners.get(listener).onDBChanged();
			}
		}
	}

	public interface DBChangeListener {
		public void onDBChanged();
	}
}
