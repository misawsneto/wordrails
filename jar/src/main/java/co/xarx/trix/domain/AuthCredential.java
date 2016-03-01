package co.xarx.trix.domain;

import co.xarx.trix.annotation.SdkExclude;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.rometools.utils.Strings;

import javax.persistence.*;

@Entity
@SdkExclude
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"tenantId"}))
public class AuthCredential extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer id;

	@Override
	public Integer getId() {
		return id;
	}

	@JsonIgnore
	public String facebookAppSecret;
	public String facebookAppID;

	@JsonIgnore
	public String googleWebAppSecret;
	public String googleWebAppID;

	@JsonIgnore
	public String googleAndroidAppSecret;
	public String googleAndroidAppID;

	@JsonIgnore
	public String googleAppleAppSecret;
	public String googleAppleAppID;

	public String getFacebookAppID() {
		return facebookAppID;
	}

	public void setFacebookAppID(String facebookAppID) {
		this.facebookAppID = facebookAppID;
	}

	public String getFacebookAppSecret() {
		return facebookAppSecret;
	}

	public void setFacebookAppSecret(String facebookAppSecret) {
		this.facebookAppSecret = facebookAppSecret;
	}

	public String getGoogleWebAppID() {
		return googleWebAppID;
	}

	public void setGoogleWebAppID(String googleWebAppID) {
		this.googleWebAppID = googleWebAppID;
	}

	public String getGoogleWebAppSecret() {
		return googleWebAppSecret;
	}

	public void setGoogleWebAppSecret(String googleWebAppSecret) {
		this.googleWebAppSecret = googleWebAppSecret;
	}

	public String getGoogleAndroidAppID() {
		return googleAndroidAppID;
	}

	public void setGoogleAndroidAppID(String googleAndroidAppID) {
		this.googleAndroidAppID = googleAndroidAppID;
	}

	public String getGoogleAndroidAppSecret() {
		return googleAndroidAppSecret;
	}

	public void setGoogleAndroidAppSecret(String googleAndroidAppSecret) {
		this.googleAndroidAppSecret = googleAndroidAppSecret;
	}

	public String getGoogleAppleAppID() {
		return googleAppleAppID;
	}

	public void setGoogleAppleAppID(String googleAppleAppID) {
		this.googleAppleAppID = googleAppleAppID;
	}

	public String getGoogleAppleAppSecret() {
		return googleAppleAppSecret;
	}

	public void setGoogleAppleAppSecret(String googleAppleAppSecret) {
		this.googleAppleAppSecret = googleAppleAppSecret;
	}

	public boolean isFacebookLoginAllowed() {
		return Strings.isNotEmpty(facebookAppID) && Strings.isNotEmpty(facebookAppSecret);
	}

	public boolean isGoogleWebLoginAllowed() {
		return Strings.isNotEmpty(googleWebAppID) && Strings.isNotEmpty(googleWebAppSecret);
	}

	public boolean isGoogleAndroidLoginAllowed() {
		return Strings.isNotEmpty(googleAndroidAppID) && Strings.isNotEmpty(googleAndroidAppSecret);
	}

	public boolean isGoogleAppleLoginAllowed() {
		return Strings.isNotEmpty(googleAppleAppID) && Strings.isNotEmpty(googleAppleAppSecret);
	}
}
