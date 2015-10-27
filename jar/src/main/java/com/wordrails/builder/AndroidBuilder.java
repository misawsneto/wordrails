package com.wordrails.builder;

import com.google.common.base.Charsets;
import com.wordrails.builder.util.StreamGobbler;
import com.wordrails.builder.util.Util;
import com.wordrails.business.AndroidApp;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileFilter;
import java.util.Collection;


public class AndroidBuilder {
	private static final String ROOT_PACKAGE = "com.wordrails";

	private static final String DEFAULT_KEY_ALIAS = "app_key_alias";
	private static final String DEFAULT_PROJECT_NAME = "trix_client";
	private static final String DEFAULT_APP_NAME = "app_template";
	private static final String DEFAULT_APP_SLUG_NAME = Util.slugify(DEFAULT_APP_NAME);
	private static final String DEFAULT_PACKAGE_NAME = ROOT_PACKAGE + "." + DEFAULT_APP_SLUG_NAME;
	private static final String DEFAULT_HOST = "dev.xarx.co";

	private static final String[] IGNORED_DIRS = {".gradle", ".idea", "build", "libraries"};
	private static final String RELEASES_DIR = "releases/";
	private static final String BUILDS_DIR = "builds/";

	public void run(String configPath, AndroidApp androidApp) throws Exception {
		String separator = "/";
		if (configPath.endsWith("/")) {
			separator = "";
		}

		File templateProjectDir = new File(configPath + separator + DEFAULT_PROJECT_NAME);
		File projectDir = new File(configPath + separator + RELEASES_DIR, androidApp.getProjectName());
		File buildDir = new File(configPath + separator + BUILDS_DIR, androidApp.getProjectName());
		File resourcesDir = new File(buildDir.getAbsolutePath(), "resources");
		String packageName = ROOT_PACKAGE + "." + androidApp.getPackageSuffix();

		Collection<File> filesToRefactor = createProject(androidApp, projectDir, templateProjectDir, resourcesDir, packageName);
		for (File file : filesToRefactor) {
			refactorFile(file, androidApp, packageName);
		}

		addGooglePlayInfo(projectDir, androidApp, buildDir.getAbsolutePath());
		buildAndPublish(false, projectDir);

		System.out.println("Done!");
	}

	private Collection<File> createProject(AndroidApp androidApp, File projectDir, File templateProjectDir, File resourcesDir, String packageName) throws Exception {
		System.out.println("Creating App \"" + androidApp.getAppName() + "\"...");

		if (projectDir.exists()) {
			FileUtils.deleteDirectory(projectDir);
		}
		projectDir.mkdirs();

		FileUtils.copyDirectory(templateProjectDir, projectDir, new FileFilter() {
			@Override
			public boolean accept(File pathname) {
				for (String dirName : IGNORED_DIRS) {
					if (pathname.isDirectory() && pathname.getAbsolutePath().endsWith(dirName)) {
						return false;
					}
				}
				return true;
			}
		});
		FileUtils.copyDirectory(resourcesDir, new File(projectDir + "/app/src/main/res"));
		FileUtils.moveDirectory(new File(projectDir.getAbsolutePath() + "/app/src/main/java/" + DEFAULT_PACKAGE_NAME.replaceAll("\\.", "/")), new File(projectDir.getAbsolutePath() + "/app/src/main/java/" + packageName.replaceAll("\\.", "/")));
		FileUtils.moveFile(new File(projectDir.getAbsolutePath() + "/" + DEFAULT_PROJECT_NAME + ".iml"), new File(projectDir.getAbsolutePath() + "/" + androidApp.getProjectName() + ".iml"));

		return FileUtils.listFiles(new File(projectDir.getAbsolutePath() + "/app"), new String[]{"java", "xml", "gradle", "properties", "iml"}, true);
	}

	private void addGooglePlayInfo(File projectDir, AndroidApp androidApp, String buildPath) throws Exception {
		File listingDir = new File(projectDir.getAbsolutePath(), "/app/src/release/play/pt-BR/listing");
		listingDir.mkdirs();

		if (!Util.isNullOrEmpty(androidApp.getAppName())) {
			File titleFile = new File(listingDir.getAbsolutePath(), "title");
			FileUtils.writeStringToFile(titleFile, androidApp.getAppName());
		}

		if (!Util.isNullOrEmpty(androidApp.getFullDescription())) {
			File fullDescriptionFile = new File(listingDir.getAbsolutePath(), "fulldescription");
			FileUtils.writeStringToFile(fullDescriptionFile, androidApp.getFullDescription());
		}

		if (!Util.isNullOrEmpty(androidApp.getShortDescription())) {
			File shortDescriptionFile = new File(listingDir.getAbsolutePath(), "shortdescription");
			FileUtils.writeStringToFile(shortDescriptionFile, androidApp.getShortDescription());
		}

		if (!Util.isNullOrEmpty(androidApp.getVideoUrl())) {
			File videoFile = new File(listingDir.getAbsolutePath(), "video");
			FileUtils.writeStringToFile(videoFile, androidApp.getVideoUrl());
		}

		Util.copyFileToDir(buildPath + "/icon.png", listingDir.getAbsolutePath() + "/icon");
		Util.copyFileToDir(buildPath + "/featureGraphic.png", listingDir.getAbsolutePath() + "/featureGraphic");

		File screenShotsDir = new File(buildPath, "screenshots");
		if (screenShotsDir.exists()) {
			for (File screenShot : screenShotsDir.listFiles()) {
				Util.copyFileToDir(screenShot.getAbsolutePath(), listingDir.getAbsolutePath() + "/phoneScreenshots");
			}
		}
	}

	private void buildAndPublish(boolean publish, File projectDir) throws Exception {
		System.out.println("Building" + (publish ? " and Publishing" : "") + "...");
		String task = publish ? "publishProductionRelease" : "assembleProductionRelease";
		String[] cmd;
		cmd = new String[]{"/bin/bash", "-c", "cd " + projectDir.getAbsolutePath() + " && " + "sh gradlew " + task};

		Process process = Runtime.getRuntime().exec(cmd);
		new StreamGobbler(process.getErrorStream(), false).start();
		new StreamGobbler(process.getInputStream(), false).start();
		process.waitFor();
	}

	private void refactorFile(File file, AndroidApp androidApp, String packageName) throws Exception {
		String content = FileUtils.readFileToString(file, Charsets.UTF_8.toString()).replace("DEBUG = true", "DEBUG = false").replaceAll(DEFAULT_KEY_ALIAS, androidApp.getKeyAlias()).replaceAll(DEFAULT_PACKAGE_NAME, packageName).replaceAll(DEFAULT_APP_NAME, androidApp.getAppName()).replaceAll(DEFAULT_PROJECT_NAME, androidApp.getProjectName()).replaceAll(DEFAULT_HOST, androidApp.getHost().replace("http://", ""));
		if (androidApp.getHost().equals("demo.trix.rocks")) {
			content = content.replace("MULTI_NETWORK = false", "MULTI_NETWORK = true");
		} else {
			content = content.replace("MULTI_NETWORK = true", "MULTI_NETWORK = false");
		}
		FileUtils.writeStringToFile(file, content, Charsets.UTF_8.toString());
	}
}