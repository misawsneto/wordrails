package co.xarx.trix.domain;

import co.xarx.trix.annotation.SdkExclude;
import co.xarx.trix.annotation.SdkInclude;
import co.xarx.trix.domain.page.ContainerSection;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Map;
import java.util.Objects;
import java.util.Set;


@Getter
@Setter
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"tenantId"}))
@JsonIgnoreProperties(value = {
		"faviconHash", "splashImageHash", "loginImageHash", "loginImageSmallHash", "subdomain"
}, allowGetters = true)
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

	@ManyToMany
	public Set<Taxonomy> taxonomies;

	@OneToMany(mappedBy="owningNetwork")
	public Set<Taxonomy> ownedTaxonomies;

	public boolean allowSignup;

	@OneToOne(fetch = FetchType.EAGER)
	public ContainerSection header;

	@OneToOne(fetch = FetchType.EAGER)
	public ContainerSection sidemenu;

	@OneToOne(fetch = FetchType.EAGER)
	public ContainerSection footer;

	@OneToOne
	@SdkExclude
	public AuthCredential authCredential;

	public boolean allowSocialLogin;

	@Deprecated
	public String facebookAppID;
	@Deprecated
	@JsonIgnore
	public String facebookAppSecret;
	@Deprecated
	public String googleAppID;
	@Deprecated
	@JsonIgnore
	public String googleAppSecret;

	public String facebookLink;
	public String youtubeLink;
	public String googlePlusLink;
	public String twitterLink;

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

	@OneToOne
	public Image favicon;
	
	@OneToOne
	public Image splashImage;
	
	@OneToOne
	public Image loginImage;

	@OneToOne
	public Image logoImage;
	
	@Column(columnDefinition = "varchar(255) default 'D'", nullable = false)
	public String defaultReadMode;
	@Column(columnDefinition = "varchar(255) default 'V'", nullable = false)
	public String defaultOrientationMode;

	public Integer categoriesTaxonomyId;

	@Lob
	public String invitationMessage;

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
	public boolean isFacebookLoginAllowed() {
		return this.facebookAppID != null && !this.facebookAppID.isEmpty() &&
						this.facebookAppSecret != null && !this.facebookAppSecret.isEmpty();
	}

	@SdkInclude
	public boolean isGoogleLoginAllowed() {
		return this.googleAppID != null && !this.googleAppID.isEmpty() &&
						this.googleAppSecret != null && !this.googleAppSecret.isEmpty();
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
		if(getDomain() != null)
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