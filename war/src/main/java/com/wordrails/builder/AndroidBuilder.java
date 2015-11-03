package com.wordrails.builder;

import com.github.slugify.Slugify;
import com.google.common.base.Charsets;
import com.wordrails.business.*;
import com.wordrails.persistence.AndroidAppRepository;
import com.wordrails.services.AmazonCloudService;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.MergeResult;
import org.eclipse.jgit.api.PullResult;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.*;
import java.io.File;
import java.util.Collection;

@Component
public class AndroidBuilder {

	Logger log = Logger.getLogger(AndroidBuilder.class.getName());

	private static final String ROOT_PACKAGE = "com.wordrails";

	private static final String DEFAULT_KEY_ALIAS = "app_key_alias";
	private static final String DEFAULT_PROJECT_NAME = "trix_client";
	private static final String DEFAULT_APP_NAME = "app_template";
	private static final String DEFAULT_APP_SLUG_NAME = slugify(DEFAULT_APP_NAME);
	private static final String DEFAULT_PACKAGE_NAME = ROOT_PACKAGE + "." + DEFAULT_APP_SLUG_NAME;
	private static final String DEFAULT_HOST = "dev.xarx.co";

	private static final String[] IGNORED_DIRS = {".gradle", ".idea", "build", "libraries"};
	private static final String RELEASES_DIR = "releases/";
	private static final String BUILDS_DIR = "builder/builds/";

	@Autowired
	private AndroidAppRepository androidAppRepository;
	@Autowired
	private AmazonCloudService amazonCloudService;


	@Async
	public void run(String configPath, AndroidApp androidApp, String networkDomain) throws Exception {
		String separator = "/";
		if (configPath.endsWith("/")) {
			separator = "";
		}

		FileRepository fileRepository = new FileRepository(configPath + separator + ".git");
		Git git = new Git(fileRepository);

		log.debug(configPath);

		PullResult pullResult = git.pull().call();
		log.debug(pullResult.toString());

		File templateProjectDir = new File(configPath + separator + DEFAULT_PROJECT_NAME);
		File projectDir = new File(configPath + separator + RELEASES_DIR, androidApp.projectName);
		File buildDir = new File(configPath + separator + BUILDS_DIR, androidApp.projectName);
		File resourcesDir = new File(buildDir.getAbsolutePath(), "resources");
		File apk = new File(projectDir, "/app/build/outputs/apk/app-production-release.apk");
		String packageName = ROOT_PACKAGE + "." + androidApp.packageSuffix;

		if (pullResult.getMergeResult().getMergeStatus() != MergeResult.MergeStatus.ALREADY_UP_TO_DATE
				&& !apk.exists()) {
			Collection<File> filesToRefactor = createProject(androidApp, projectDir, templateProjectDir, resourcesDir, packageName);
			for (File file : filesToRefactor) {
				refactorFile(file, androidApp, packageName);
			}

			addGooglePlayInfo(projectDir, androidApp, buildDir.getAbsolutePath());
			buildAndPublish(false, projectDir);
		}

		String hash = uploadApk(apk, networkDomain);
		String fileUrl = amazonCloudService.getPublicApkURL(networkDomain, hash);

		androidApp.apkUrl = fileUrl;
		androidAppRepository.save(androidApp);

		log.debug(fileUrl);

		log.debug("Done!");
	}

	private String uploadApk(File apk, String domain) throws IOException {
		return amazonCloudService.uploadAPK(apk, apk.length(), domain, "application/vnd.android.package-archive");
	}

	private Collection<File> createProject(AndroidApp androidApp, File projectDir, File templateProjectDir, File resourcesDir, String packageName) throws Exception {
		log.debug("Creating App \"" + androidApp.appName + "\"...");

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

		File drawableDir = new File(projectDir, "/app/src/main/res");

		if(androidApp.logoActionBar != null) {
			downloadIconToDir("logo_action_bar.png", new File(drawableDir, "drawable"), androidApp.logoActionBar.url);
		}
		if(androidApp.logoLogin != null) {
			downloadIconToDir("logo_login.png", new File(drawableDir, "drawable"), androidApp.logoLogin.url);
		}
		if(androidApp.logoSplash != null) {
			downloadIconToDir("logo_splash.png", new File(drawableDir, "drawable"), androidApp.logoSplash.url);
		}
		if(androidApp.launcherIconMDPI != null) {
			downloadIconToDir("ic_launcher.png", new File(drawableDir, "drawable-mdpi"), androidApp.launcherIconMDPI.url);
		}
		if(androidApp.launcherIconHDPI != null) {
			downloadIconToDir("ic_launcher.png", new File(drawableDir, "drawable-hdpi"), androidApp.launcherIconHDPI.url);
		}
		if(androidApp.launcherIconXHDPI != null) {
			downloadIconToDir("ic_launcher.png", new File(drawableDir, "drawable-xhdpi"), androidApp.launcherIconXHDPI.url);
		}
		if(androidApp.launcherIconXXHDPI != null) {
			downloadIconToDir("ic_launcher.png", new File(drawableDir, "drawable-xxhdpi"), androidApp.launcherIconXXHDPI.url);
		}
		if(androidApp.launcherIconXXXHDPI != null) {
			downloadIconToDir("ic_launcher.png", new File(drawableDir, "drawable-xxxhdpi"), androidApp.launcherIconXXXHDPI.url);
		}

		FileUtils.copyDirectory(resourcesDir, new File(projectDir + "/app/src/main/res"));
		FileUtils.moveDirectory(new File(projectDir.getAbsolutePath() + "/app/src/main/java/" + DEFAULT_PACKAGE_NAME.replaceAll("\\.", "/")), new File(projectDir.getAbsolutePath() + "/app/src/main/java/" + packageName.replaceAll("\\.", "/")));
		FileUtils.moveFile(new File(projectDir.getAbsolutePath() + "/" + DEFAULT_PROJECT_NAME + ".iml"), new File(projectDir.getAbsolutePath() + "/" + androidApp.projectName + ".iml"));

		return FileUtils.listFiles(new File(projectDir.getAbsolutePath() + "/app"), new String[]{"java", "xml", "gradle", "properties", "iml"}, true);
	}

	private void downloadIconToDir(String fileName, File dir, String url) {

	}

	private void addGooglePlayInfo(File projectDir, AndroidApp androidApp, String buildPath) throws Exception {
		File listingDir = new File(projectDir.getAbsolutePath(), "/app/src/release/play/pt-BR/listing");
		listingDir.mkdirs();

		if (!isNullOrEmpty(androidApp.appName)) {
			File titleFile = new File(listingDir.getAbsolutePath(), "title");
			FileUtils.writeStringToFile(titleFile, androidApp.appName);
		}

		if (!isNullOrEmpty(androidApp.fullDescription)) {
			File fullDescriptionFile = new File(listingDir.getAbsolutePath(), "fulldescription");
			FileUtils.writeStringToFile(fullDescriptionFile, androidApp.fullDescription);
		}

		if (!isNullOrEmpty(androidApp.shortDescription)) {
			File shortDescriptionFile = new File(listingDir.getAbsolutePath(), "shortdescription");
			FileUtils.writeStringToFile(shortDescriptionFile, androidApp.shortDescription);
		}

		if (!isNullOrEmpty(androidApp.videoUrl)) {
			File videoFile = new File(listingDir.getAbsolutePath(), "video");
			FileUtils.writeStringToFile(videoFile, androidApp.videoUrl);
		}

		copyFileToDir(buildPath + "/icon.png", listingDir.getAbsolutePath() + "/icon");
		copyFileToDir(buildPath + "/featureGraphic.png", listingDir.getAbsolutePath() + "/featureGraphic");

		File screenShotsDir = new File(buildPath, "screenshots");
		if (screenShotsDir.exists()) {
			for (File screenShot : screenShotsDir.listFiles()) {
				copyFileToDir(screenShot.getAbsolutePath(), listingDir.getAbsolutePath() + "/phoneScreenshots");
			}
		}
	}

	private void buildAndPublish(boolean publish, File projectDir) throws Exception {
		log.debug("Building" + (publish ? " and Publishing" : "") + "...");
		String task = publish ? "publishProductionRelease" : "assembleProductionRelease";
		String[] cmd;
		cmd = new String[]{"/bin/bash", "-c", "cd " + projectDir.getAbsolutePath() + " && " + "sh gradlew " + task};

		Process process = Runtime.getRuntime().exec(cmd);

		new Thread(new StreamGobbler(process.getErrorStream(), true)).start();
		new Thread(new StreamGobbler(process.getInputStream(), true)).start();
		process.waitFor();
	}

	private void refactorFile(File file, AndroidApp androidApp, String packageName) throws Exception {
		String content = FileUtils.readFileToString(file, Charsets.UTF_8.toString()).replace("DEBUG = true", "DEBUG = false")
				.replaceAll(DEFAULT_KEY_ALIAS, androidApp.keyAlias)
				.replaceAll(DEFAULT_PACKAGE_NAME, packageName)
				.replaceAll(DEFAULT_APP_NAME, androidApp.appName)
				.replaceAll(DEFAULT_PROJECT_NAME, androidApp.projectName)
				.replaceAll(DEFAULT_HOST, androidApp.host.replace("http://", ""));
		if (androidApp.host.equals("demo.trix.rocks")) {
			content = content.replace("MULTI_NETWORK = false", "MULTI_NETWORK = true");
		} else {
			content = content.replace("MULTI_NETWORK = true", "MULTI_NETWORK = false");
		}
		FileUtils.writeStringToFile(file, content, Charsets.UTF_8.toString());
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