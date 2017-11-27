package com.duoduoapp.brothers.utils;

import android.annotation.SuppressLint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtil {

	/***
	 * 反格式化时间
	 * @param time
	 * @param format
	 * @return
	 * @throws Exception
	 */
	@SuppressLint("SimpleDateFormat")
	public static long getLongTime(String time, String format)   {
		long re = 0 ;
		Date date = null;
		try {
			date = new SimpleDateFormat(format).parse(time);
			re = date.getTime();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return re;
	}
}
