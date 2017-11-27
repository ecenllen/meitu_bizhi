package com.duoduoapp.meitu.itf;

/**
 * 对侧滑的操作回调函数
 * 
 * @author Shanlin
 * 
 */
public interface SlidingMenuListener {

	/**
	 * 设置是否滑动
	 * 
	 * @param sliding
	 */
	public void setSliding(boolean sliding);

	/**
	 * 显示侧边
	 */
	public void showSliding();

	/**
	 * 隐藏侧边
	 */
	public void hideSliding();

}
