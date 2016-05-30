package co.xarx.trix.domain;

import com.rometools.utils.Strings;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"tenantId"}))
public class AuthCredential extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer id;

	@Override
	public Integer getId() {
		return id;
	}

//	@JsonIgnore
	public String facebookAppSecret;
	public String facebookAppID;

//	@JsonIgnore
	public String googleWebAppSecret;
	public String googleWebAppID;

//	@JsonIgnore
	public String googleAndroidAppSecret;
	public String googleAndroidAppID;

//	@JsonIgnore
	public String googleAppleAppSecret;
	public String googleAppleAppID;

	@OneToOne
	@NotNull
	public Network network;

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
