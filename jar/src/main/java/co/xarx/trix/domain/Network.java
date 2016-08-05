package co.xarx.trix.domain;

import co.xarx.trix.annotation.SdkExclude;
import co.xarx.trix.annotation.SdkInclude;
import co.xarx.trix.domain.page.ContainerSection;
import com.fasterxml.jackson.annotation.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Map;
import java.util.Objects;


@Getter
@Setter
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"tenantId"}))
@JsonIgnoreProperties(value = {
		"faviconHash", "splashImageHash", "loginImageHash", "loginImageSmallHash", "subdomain",
		"facebookLoginAllowed", "googleLoginAllowed"
}, allowGetters = true, ignoreUnknown = true)
public class Network extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 7723825842358687233L;

	@Id
	@Setter(AccessLevel.NONE)
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer id;

	@NotNull
	@Size(min=1, max=100)
	public String name;

	@Size(min=1, max=100)
	public String flurryKey;

	@Size(min=1, max=100)
	public String flurryAppleKey;
	
	@Size(min=1, max=100)
	public String trackingId;

//	@OneToMany(mappedBy="network", cascade=CascadeType.REMOVE)
//	public Set<Station> stations;

//	@ManyToMany
//	public Set<Taxonomy> taxonomies;
//
//	@OneToMany(mappedBy="owningNetwork")
//	public Set<Taxonomy> ownedTaxonomies;

	public boolean allowSignup;

	@OneToOne(fetch = FetchType.EAGER)
	public ContainerSection header;

	@OneToOne(fetch = FetchType.EAGER)
	public ContainerSection sidemenu;

	@OneToOne(fetch = FetchType.EAGER)
	public ContainerSection footer;

	@OneToOne(mappedBy = "network")
	@SdkExclude
	@JsonIdentityInfo(
			generator = ObjectIdGenerators.PropertyGenerator.class,
			property = "id")
	public AuthCredential authCredential;

	public boolean allowSocialLogin;

	public String facebookLink;
	public String youtubeLink;
	public String googlePlusLink;
	public String twitterLink;
	public String pinterestLink;
	public String linkedInLink;
	public String instagramLink;

	public String webFooter;
	
	@Column(columnDefinition = "boolean default false", nullable = false)
	public boolean allowSponsors;

	public String stationMenuName;
	public String homeTabName;

	public String domain;

	@JsonIgnore
	public String networkCreationToken;

	@Lob
	public String info;

	@Lob
	public String loginFooterMessage;

	public String backgroundColor = "#F3F5F9";
	public String navbarColor = "#242424";
	public String navbarSecondaryColor  = "#505050";
	public String mainColor = "#111111";
	public String primaryFont = "Lato";
	public String secondaryFont = "PT Serif";
	@Column(columnDefinition="Decimal(10,2) default '4.0'")
	public Double titleFontSize = 4.0;
	@Column(columnDefinition="Decimal(10,2) default '1.0'")
	public Double newsFontSize = 1.0;

	public boolean configured;

	@Column(columnDefinition = "boolean default false", nullable = false)
	public boolean emailSignUpValidationEnabled;

	@OneToOne
	public Image favicon;

	@SdkInclude
	public Integer getFaviconId(){
		if(favicon != null)
			return favicon.getId();
		return null;
	}
	
	@OneToOne
	public Image splashImage;

	@SdkInclude
	public Integer getSplashImageId(){
		if(splashImage != null)
			return splashImage.getId();
		return null;
	}
	
	@OneToOne
	public Image loginImage;

	@SdkInclude
	public Integer getLoginImageId(){
		if(splashImage != null)
			return loginImage.getId();
		return null;
	}

	@OneToOne
	public Image logoImage;

	@SdkInclude
	public Integer getLogoImageId(){
		if(logoImage != null)
			return logoImage.getId();
		return null;
	}
	
	@Column(columnDefinition = "varchar(255) default 'D'", nullable = false)
	public String defaultReadMode;
	@Column(columnDefinition = "varchar(255) default 'V'", nullable = false)
	public String defaultOrientationMode;

	public Integer categoriesTaxonomyId;

	@Lob
	public String invitationMessage;

	@Lob
	public String validationMessage;

	@Override
	public String toString() {
		return "Network [id=" + id + ", name=" + name + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, name);
	}

	@Lob
	public String playStoreAddress;

	@Lob
	public String appleStoreAddress;

	@Column(columnDefinition = "boolean default false", nullable = false)
	public boolean addStationRolesOnSignup;

	@ElementCollection(fetch = FetchType.EAGER)
	@JoinTable(name = "palette_primary_color", joinColumns = @JoinColumn(name = "network_id"))
	@MapKeyColumn(name = "name", nullable = false, length = 100)
	@Column(name = "color", nullable = false, length = 100)
	public Map<String, String> primaryColors;


	@ElementCollection(fetch = FetchType.EAGER)
	@JoinTable(name = "palette_secondary_color", joinColumns = @JoinColumn(name = "network_id"))
	@MapKeyColumn(name = "name", nullable = false, length = 100)
	@Column(name = "color", nullable = false, length = 100)
	public Map<String, String> secondaryColors;

	@ElementCollection(fetch = FetchType.EAGER)
	@JoinTable(name = "palette_alert_color", joinColumns = @JoinColumn(name = "network_id"))
	@MapKeyColumn(name = "name", nullable = false, length = 100)
	@Column(name = "color", nullable = false, length = 100)
	public Map<String, String> alertColors;

	@ElementCollection(fetch = FetchType.EAGER)
	@JoinTable(name = "palette_background_color", joinColumns = @JoinColumn(name = "network_id"))
	@MapKeyColumn(name = "name", nullable = false, length = 100)
	@Column(name = "color", nullable = false, length = 100)
	public Map<String, String> backgroundColors;

	@Override
	public boolean equals(Object obj) {
		if (obj != null) {
			try {
				Network network = (Network) obj;
				return Objects.equals(id, network.id) && Objects.equals(tenantId, network.tenantId);
			} catch (ClassCastException e) {}
		}
		return false;
	}

	@PrePersist
	void onCreate() {
		if(defaultReadMode == null || defaultReadMode.isEmpty())
			defaultReadMode = "D";
		if(defaultOrientationMode == null || defaultOrientationMode.isEmpty())
			defaultOrientationMode = "H";
	}

	@SdkInclude
	public String getFacebookAppID() {
		if (authCredential != null) return authCredential.getFacebookAppID();
		return null;
	}

	@SdkInclude
	public String getGoogleAppID() {
		if (authCredential != null) return authCredential.getGoogleAppID();
		return null;
	}

	@SdkInclude
	public boolean isFacebookLoginAllowed() {
		return allowSocialLogin && authCredential != null && authCredential.isFacebookLoginAllowed();
	}

	@SdkInclude
	public boolean isGoogleLoginAllowed() {
		return allowSocialLogin && authCredential != null && authCredential.isGoogleLoginAllowed();
	}

	@SdkInclude
	public String getFaviconHash() {
		if (favicon != null) return favicon.getOriginalHash();

		return null;
	}

	@SdkInclude
	public String getSplashImageHash() {
		if (splashImage != null) return splashImage.getOriginalHash();

		return null;
	}

	@SdkInclude
	public String getLoginImageHash() {
		if (loginImage != null) return loginImage.getOriginalHash();

		return null;
	}

	@SdkInclude
	public String getLogoImageHash() {
		if (logoImage != null) return logoImage.getOriginalHash();

		return null;
	}

	@SdkInclude
	public String getSubdomain() {
		return tenantId;
	}

	@JsonIgnore
	public String getRealDomain() {
		if(getDomain() != null && !getDomain().isEmpty())
			return getDomain();

		return getSubdomain() + ".trix.rocks";
	}

	@Deprecated
	@SdkInclude
	public String getLoginImageSmallHash() {
		if (loginImage != null) return loginImage.getSmallHash();

		return null;
	}

}