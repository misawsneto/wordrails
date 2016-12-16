package co.xarx.trix.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class NetworkData {
	public java.lang.Integer id;
	public boolean addStationRolesOnSignup;
	public java.util.Map<java.lang.String, java.lang.String> alertColors;
	public boolean allowSignup;
	public boolean allowSocialLogin;
	public boolean allowSponsors;
	public java.lang.String appleStoreAddress;
	public java.lang.String splashBgColor;
	public java.lang.String backgroundColor;
	public java.util.Map<java.lang.String, java.lang.String> backgroundColors;
	public java.lang.Integer categoriesTaxonomyId;
	public boolean configured;
	public java.lang.String defaultOrientationMode;
	public java.lang.String defaultReadMode;
	public java.lang.String domain;
	public boolean emailSignUpValidationEnabled;
	public java.lang.String facebookAppID;
	public java.lang.String facebookAppSecret;
	public java.lang.String facebookLink;
	public boolean facebookLoginAllowed;
	public java.lang.String faviconHash;
	public java.lang.Integer faviconId;
	public java.lang.String flurryAppleKey;
	public java.lang.String flurryKey;
	public java.lang.String googleAppID;
	public java.lang.String googleAppSecret;
	public boolean googleLoginAllowed;
	public java.lang.String googlePlusLink;
	public java.lang.String homeTabName;
	public java.lang.String info;
	public java.lang.String instagramLink;
	public java.lang.String invitationMessage;
	public java.lang.String linkedInLink;
	public java.lang.String loginFooterMessage;
	public java.lang.String loginImageHash;
	public java.lang.Integer loginImageId;
	public java.lang.String loginImageSmallHash;
	public java.lang.String logoImageHash;
	public java.lang.Integer logoImageId;
	public java.lang.String mainColor;
	public java.lang.String name;
	public java.lang.String navbarColor;
	public java.lang.String navbarSecondaryColor;
	public java.lang.String networkCreationToken;
	public java.lang.Double newsFontSize;
	public java.lang.String pinterestLink;
	public java.lang.String playStoreAddress;
	public java.util.Map<java.lang.String, java.lang.String> primaryColors;
	public java.lang.String primaryFont;
	public java.util.Map<java.lang.String, java.lang.String> secondaryColors;
	public java.lang.String secondaryFont;
	public java.lang.String splashImageHash;
	public java.lang.Integer splashImageId;
	public java.lang.String stationMenuName;
	public java.lang.String subdomain;
	public java.lang.Double titleFontSize;
	public java.lang.String trackingId;
	public java.lang.String twitterLink;
	public java.lang.String validationMessage;
	public java.lang.String webFooter;
	public java.lang.String youtubeLink;

	public java.lang.String facebookUrlRedirect = java.lang.String.format("http://%s.trix.rocks/", subdomain);
	public final java.lang.String facebookUrlToken = "https://graph.facebook.com/v2.6/oauth/access_token";
	public final java.lang.String facebookUrlAuthorization = "https://www.facebook.com/v2.6/dialog/oauth?scope=public_profile,email&";

	public final java.lang.String googleUrlRedirect = "urn:ietf:wg:oauth:2.0:oob:auto";
	public final java.lang.String googleUrlToken = "https://www.googleapis.com/oauth2/v4/token";
	public final java.lang.String googleUrlAuthorization = "https://accounts.google" +
			".com/o/oauth2/v2/auth?scope=email%20profile&";

	public final java.lang.String facebookUrlRevoke = "https://graph.facebook.com/v2.6/me/permissions";
	public final java.lang.String googleUrlRevoke = "https://accounts.google.com/o/oauth2/revoke";

	@Override
	public String toString() {
		return "[" + id + ", " + addStationRolesOnSignup + ", " + alertColors + ", " + allowSignup + ", " + allowSocialLogin + ", " + allowSponsors + ", " + appleStoreAddress + ", " + backgroundColor + ", " + backgroundColors + ", " + categoriesTaxonomyId + ", " + configured + ", " + defaultOrientationMode + ", " + defaultReadMode + ", " + domain + ", " + emailSignUpValidationEnabled + ", " + facebookAppID + ", " + facebookLink + ", " + facebookLoginAllowed + ", " + faviconHash + ", " + faviconId + ", " + flurryAppleKey + ", " + flurryKey + ", " + googleAppID + ", " + googleLoginAllowed + ", " + googlePlusLink + ", " + homeTabName + ", " + info + ", " + instagramLink + ", " + invitationMessage + ", " + linkedInLink + ", " + loginFooterMessage + ", " + loginImageHash + ", " + loginImageId + ", " + loginImageSmallHash + ", " + logoImageHash + ", " + logoImageId + ", " + mainColor + ", " + name + ", " + navbarColor + ", " + navbarSecondaryColor + ", " + networkCreationToken + ", " + newsFontSize + ", " + pinterestLink + ", " + playStoreAddress + ", " + primaryColors + ", " + primaryFont + ", " + secondaryColors + ", " + secondaryFont + ", " + splashImageHash + ", " + splashImageId + ", " + stationMenuName + ", " + subdomain + ", " + titleFontSize + ", " + trackingId + ", " + twitterLink + ", " + validationMessage + ", " + webFooter + ", " + youtubeLink + "]";
	}
}