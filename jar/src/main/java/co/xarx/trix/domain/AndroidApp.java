package co.xarx.trix.domain;

import java.io.Serializable;

public interface AndroidApp extends MultiTenantEntity, Identifiable, Serializable {

	Integer MDPI_SIZE = 48;
	Integer HDPI_SIZE = 72;
	Integer XHDPI_SIZE = 96;
	Integer XXHDPI_SIZE = 144;
	Integer XXXHDPI_SIZE = 192;
	Integer ORIGINAL_SIZE = 512;

	String getProjectName();

	String getAppName();

	String getKeyAlias();

	String getPackageSuffix();

	String getHost();

	String getShortDescription();

	String getFullDescription();

	String getVideoUrl();

	String getApkUrl();

	File getIcon();
}
