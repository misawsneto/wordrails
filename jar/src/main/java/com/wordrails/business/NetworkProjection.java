package com.wordrails.business;

import org.springframework.data.rest.core.config.Projection;

@Projection(types=Network.class)
public interface NetworkProjection {
	Integer getId();
	String getName();
	Integer getLogoId();
    Integer getLogoSmallId();
	Integer getFaviconId();
	Integer getSplashImageId();
	Integer getLoginImageId();
    Integer getLoginImageSmallId();
	
	
    String getFlurryKey();
    String getTrackingId();
    Boolean getAllowSignup();
    Boolean getAllowComments();
    Boolean getAllowSponsors();
    String getDomain();
    String getBackgroundColor();
    String getNavbarColor();
    String getMainColor();
    String getPrimaryFont();
    String getSecondaryFont();
    String getSubdomain();
    Boolean getConfigured();
    
    String getDefaultReadMode();
    String getDefaultOrientationMode();
    
    java.util.Date getCreatedAt();
    java.util.Date getUpdatedAt();
}