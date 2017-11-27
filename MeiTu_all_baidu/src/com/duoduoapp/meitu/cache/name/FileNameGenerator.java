package com.duoduoapp.meitu.cache.name;

public interface FileNameGenerator {

	/** Generates unique file name for image defined by URI */
	String getName(String name);
}
