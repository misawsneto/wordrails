package co.xarx.trix.domain.social;


public interface SocialUser {

	String getProviderId();

	String getId();
	String getName();
	String getEmail();
	String getCoverUrl();
	String getProfileUrl();
	String getProfileImageUrl();
}
