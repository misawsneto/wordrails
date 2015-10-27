package com.wordrails.builder.util;

import com.github.slugify.Slugify;
import com.google.gson.Gson;
import org.apache.commons.io.FileUtils;

import java.io.File;

public class Util {

	private static final Gson GSON = new Gson();

	public static Gson getGson() {
		return GSON;
	}

	public static String slugify(String str) {
		try {
			return new Slugify(true).slugify(str).replaceAll("-", "");
		} catch (Exception e) {
			return null;
		}
	}

	public static boolean isNullOrEmpty(String str) {
		return str == null || str.isEmpty();
	}

	public static void copyFileToDir(String filePath, String dirPath) {
		if (!isNullOrEmpty(filePath) && !isNullOrEmpty(dirPath)) {
			try {
				File file = new File(filePath);
				File dir = new File(dirPath);
				if (file.exists()) {
					FileUtils.copyFileToDirectory(file, dir, true);
				}
			} catch (Exception e) {
			}
		}
	}
}