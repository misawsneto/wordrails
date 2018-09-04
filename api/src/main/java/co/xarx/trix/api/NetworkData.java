package co.xarx.trix.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class NetworkData {

	public Integer id;
	public Integer stationScheduleId;
	public String stationScheduleName;
	public boolean addStationRolesOnSignup;
	public java.util.Map<String, String> alertColors;
	public String urlCarnival;
	public String urlTermsOfUse;
	public String appleStoreAddress;
	public String splashBgColor;
	public String bgSplashLogo;
	public String backgroundColor;
	public java.util.Map<String, String> backgroundColors;
	public Integer categoriesTaxonomyId;
	public boolean configured;
	public String defaultOrientationMode;
	public String defaultReadMode;
	public String domain;
	public boolean emailSignUpValidationEnabled;
	public String facebookAppID;
	public String facebookAppSecret;
	public String facebookLink;
	public String faviconHash;
	public String faviconUrl;
	public Integer faviconId;
	public String flurryAppleKey;
	public String flurryKey;
	public String googleAppID;
	public String googleAppSecret;
	public String googlePlusLink;
	public String homeTabName;
	public String info;
	public String instagramLink;
	public String invitationMessage;
	public String linkedInLink;
	public String loginFooterMessage;
	public String loginImageHash;
	public String loginImageUrl;
	public Integer loginImageId;
	public String loginImageSmallHash;
	public String logoImageHash;
	public String logoImageUrl;
	public Integer logoImageId;
	public String mainColor;
	public String name;
	public String navbarColor;
	public String navbarSecondaryColor;
	public String networkCreationToken;
	public Double newsFontSize;
	public String pinterestLink;
	public String playStoreAddress;
	public java.util.Map<String, String> primaryColors;
	public String primaryFont;
	public java.util.Map<String, String> secondaryColors;
	public String secondaryFont;
	public String splashImageUrl;
	public String splashImageHash;
	public Integer splashImageId;
	public String stationMenuName;
	public String subdomain;
	public Double titleFontSize;
	public String trackingId;
	public String twitterLink;
	public String validationMessage;
	public String webFooter;
	public String youtubeLink;

	public String facebookUrlRedirect = String.format("http://%s.trix.rocks/", subdomain);
	public final String facebookUrlToken = "https://graph.facebook.com/v2.6/oauth/access_token";
	public final String facebookUrlAuthorization = "https://www.facebook.com/v2.6/dialog/oauth?scope=public_profile,email&user_friends&";

	public final String googleUrlRedirect = "urn:ietf:wg:oauth:2.0:oob:auto";
	public final String googleUrlToken = "https://www.googleapis.com/oauth2/v4/token";
	public final String googleUrlAuthorization = "https://accounts.google" +
			".com/o/oauth2/v2/auth?scope=email%20profile&plus.login&";

	public final String facebookUrlRevoke = "https://graph.facebook.com/v2.6/me/permissions";
	public final String googleUrlRevoke = "https://accounts.google.com/o/oauth2/revoke";
	public String namingNetwork;
	public String namingUser;
	public String namingUsers;
	public String namingPost;
	public String namingPosts;
	public String namingTag;
	public String namingTags;
	public String namingStation;
	public String namingStations;
	public List<String> categoriesEvent;
	public List<String> categoriesClassified;

	public boolean carnival;
	public boolean allowCarnival;

	public boolean showEvents = false;
	public boolean allowEvents;

	public boolean facebookLoginAllowed;
	public boolean allowFacebooklogin;

	public boolean googleLoginAllowed;
	public boolean allowGoogleLogin;

	public boolean allowSignup;
	public Boolean allowSocialLogin;
	public Boolean allowSponsors;
	public boolean allowTrixAlert;
	public boolean allowTrixNow;
	public boolean allowCoupon;
	public boolean allowClassifieds;
	public boolean privateApp;
	public boolean allowLateralMenu;

	@Override
	public String toString() {
		return "[" + id + ", " + addStationRolesOnSignup + ", " + alertColors + ", " + allowSignup + ", " + allowSocialLogin + ", " + allowSponsors + ", " + appleStoreAddress + ", " + backgroundColor + ", " + backgroundColors + ", " + categoriesTaxonomyId + ", " + configured + ", " + defaultOrientationMode + ", " + defaultReadMode + ", " + domain + ", " + emailSignUpValidationEnabled + ", " + facebookAppID + ", " + facebookLink + ", " + facebookLoginAllowed + ", " + faviconHash + ", " + faviconId + ", " + flurryAppleKey + ", " + flurryKey + ", " + googleAppID + ", " + googleLoginAllowed + ", " + googlePlusLink + ", " + homeTabName + ", " + info + ", " + instagramLink + ", " + invitationMessage + ", " + linkedInLink + ", " + loginFooterMessage + ", " + loginImageHash + ", " + loginImageId + ", " + loginImageSmallHash + ", " + logoImageHash + ", " + logoImageId + ", " + mainColor + ", " + name + ", " + navbarColor + ", " + navbarSecondaryColor + ", " + networkCreationToken + ", " + newsFontSize + ", " + pinterestLink + ", " + playStoreAddress + ", " + primaryColors + ", " + primaryFont + ", " + secondaryColors + ", " + secondaryFont + ", " + splashImageHash + ", " + splashImageId + ", " + stationMenuName + ", " + subdomain + ", " + titleFontSize + ", " + trackingId + ", " + twitterLink + ", " + validationMessage + ", " + webFooter + ", " + youtubeLink + "]";
	}
}