package co.xarx.trix.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"tenantId"}))
public class Network extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 7723825842358687233L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer id;

	@Override
	public Integer getId() {
		return id;
	}

	@NotNull
	@Size(min=1, max=100)
	public String name;

	@Size(min=1, max=100)
	public String flurryKey;

	@Size(min=1, max=100)
	public String flurryAppleKey;
	
	@Size(min=1, max=100)
	public String trackingId;

	@OneToMany(mappedBy="network")
	public Set<NetworkRole> personsNetworkRoles;

	@OneToMany(mappedBy="network", cascade=CascadeType.REMOVE)
	public Set<Station> stations;

	@ManyToMany
	public Set<Taxonomy> taxonomies;

	@OneToMany(mappedBy="network", cascade=CascadeType.ALL)
	public Set<Sponsor> sponsors;

	@OneToMany(mappedBy="network")
	public Set<Section> sections;

	@OneToMany(mappedBy="owningNetwork")
	public Set<Taxonomy> ownedTaxonomies;

	public boolean allowSignup;

	@OneToOne
	public OAuthCredential oauthCredential;

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

	public String getName() {
		return name;
	}

}