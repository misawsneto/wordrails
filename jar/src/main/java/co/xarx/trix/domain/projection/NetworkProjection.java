package co.xarx.trix.domain.projection;

import co.xarx.trix.domain.Network;
import org.springframework.data.rest.core.config.Projection;

import java.util.Map;

@Projection(types=Network.class)
public interface NetworkProjection {
	Integer getId();
	String getName();

	ImageProjection getLogoImage();
	ImageProjection getSplashImage();
	ImageProjection getLoginImage();
	ImageProjection getFaviconImage();

	String getStationMenuName();
	String getHomeTabName();

	String getFacebookLink();
	String getYoutubeLink();
	String getGooglePlusLink();
	String getTwitterLink();

	String getInstagramLink();
	String getPinterestLink();
	String getLinkedInLink();
	
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

	Map<String, String> getPrimaryColors();
	Map<String, String> getSecondaryColors();
	Map<String, String> getAlertColors();
	Map<String, String> getBackgroundColors();

	Integer categoriesTaxonomyId();

	String getDefaultReadMode();
	String getDefaultOrientationMode();

	java.util.Date getCreatedAt();
	java.util.Date getUpdatedAt();
}