package co.xarx.trix.domain.projection;

import co.xarx.trix.domain.Network;
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

    String getStationMenuName();
    String getHomeTabName();

    String getFacebookLink();
    String getYoutubeLink();
    String getGooglePlusLink();
    String getTwitterLink();
	
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

    Integer categoriesTaxonomyId();
    
    String getDefaultReadMode();
    String getDefaultOrientationMode();
    
    java.util.Date getCreatedAt();
    java.util.Date getUpdatedAt();
}