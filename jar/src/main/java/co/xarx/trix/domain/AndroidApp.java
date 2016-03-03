package co.xarx.trix.domain;

import java.io.Serializable;

public interface AndroidApp extends MultiTenantEntity, Identifiable, Serializable {
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
