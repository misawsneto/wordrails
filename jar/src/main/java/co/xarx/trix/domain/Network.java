package co.xarx.trix.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(uniqueConstraints=@UniqueConstraint(columnNames={"subdomain"}))
public class Network implements Serializable{

	private static final long serialVersionUID = 7723825842358687233L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
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
	
	@Column(columnDefinition = "boolean default false", nullable = false)
	public boolean allowSignup;

	@Deprecated
	@Column(columnDefinition = "boolean default false", nullable = false)
	public boolean allowSocialLogin;

	public String facebookAppID;

	@JsonIgnore
	public String facebookAppSecret;

	public String googleAppID;

	@JsonIgnore
	public String googleAppSecret;
	
	@Column(columnDefinition = "boolean default false", nullable = false)
	public boolean allowSponsors;
	
	public String domain;

	@JsonIgnore
	public String networkCreationToken;

	@Lob
	public String info;

	@Lob
	public String loginFooterMessage;
	
//	@Column(columnDefinition="TEXT default '#F3F5F9'")
	public String backgroundColor = "#F3F5F9";
//	@Column(columnDefinition="TEXT default '#242424'")
	public String navbarColor = "#242424";
//	@Column(columnDefinition="TEXT default '#505050'")
	public String navbarSecondaryColor  = "#505050";
//	@Column(columnDefinition="TEXT default '#040404'")
	public String mainColor = "#111111";
//	@Column(columnDefinition="TEXT default 'Lato'")
	public String primaryFont = "Lato";
//	@Column(columnDefinition="TEXT default 'PT Serif'")
	public String secondaryFont = "PT Serif";
	@Column(columnDefinition="Decimal(10,2) default '4.0'")
	public Double titleFontSize = 4.0;
	@Column(columnDefinition="Decimal(10,2) default '1.0'")
	public Double newsFontSize = 1.0;
	
	@NotNull
	@Pattern(regexp = "^((?!-)[A-Za-z0-9-]{1,63})$", message = "Invalid subdomain")
	public String subdomain;
	
	public boolean configured;
	
	@OneToOne
	public Image logo;
	public Integer logoId;
	public Integer logoSmallId;

	public String logoHash;
	public String logoSmallHash;
	public String faviconHash;
	public String splashImageHash;
	public String loginImageHash;
	public String loginImageSmallHash;

	@OneToOne
	public Image favicon;
	public Integer faviconId;
	
	@OneToOne
	public Image splashImage;
	public Integer splashImageId;
	
	@OneToOne
	public Image loginImage;
	public Integer loginImageId;
	public Integer loginImageSmallId;

	@JsonIgnore
	@OneToOne
	public AndroidApp androidApp;
	
	@Column(columnDefinition = "varchar(255) default 'D'", nullable = false)
	public String defaultReadMode;
	@Column(columnDefinition = "varchar(255) default 'V'", nullable = false)
	public String defaultOrientationMode;

	public Integer categoriesTaxonomyId;


	@ElementCollection
	@JoinTable(name="image_hash", joinColumns=@JoinColumn(name="image_id", referencedColumnName = "logo_id"))
	@MapKeyColumn(name="sizeTag", nullable = false)
	@Column(name="hash", nullable = false)
	public Map<String, String> logoHashes;


	@ElementCollection
	@JoinTable(name="image_hash", joinColumns=@JoinColumn(name="image_id", referencedColumnName = "favicon_id"))
	@MapKeyColumn(name="sizeTag", nullable = false)
	@Column(name="hash", nullable = false)
	public Map<String, String> faviconHashes;

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

	@Override
	public boolean equals(Object obj) {
		if (obj != null) {
			try {
				Network network = (Network) obj;
				return Objects.equals(id, network.id) && Objects.equals(name, network.name);
			} catch (ClassCastException e) {}
		}
		return false;
	}
	
	@JsonFormat(shape=JsonFormat.Shape.NUMBER)
	@Temporal(TemporalType.TIMESTAMP)
	@Column(updatable=false)
	public Date createdAt;

	@PrePersist
	void onCreate() {
		createdAt = new Date();
		if(defaultReadMode == null || defaultReadMode.isEmpty())
			defaultReadMode = "D";
		if(defaultOrientationMode == null || defaultOrientationMode.isEmpty())
			defaultOrientationMode = "H";
		
		onChange();
	}
	
	@JsonFormat(shape=JsonFormat.Shape.NUMBER)
	@Temporal(TemporalType.TIMESTAMP)
	public Date updatedAt;

	@PreUpdate
	void onUpdate() {
		updatedAt = new Date();
		onChange();
	}

	@PostLoad //lazy initialize the maps. we need to do this hack because hibernate 4 has a bug to fetch eager null references
	void onPostLoad() {
		if(logo != null) {
			this.logoHashes.size();
		}
		if(favicon != null) {
			this.faviconHashes.size();
		}
	}

	private void onChange() {
		if(logo != null && logo.originalHash != null){
			logoHash = logo.originalHash;
			logoSmallHash = logo.smallHash;

			logoId = logo.original.id;
			logoSmallId = logo.small.id;
		}else{
			logoHash = null;
			logoSmallHash = null;
			logoId = null;
			logoSmallId = null;
		}

		if(favicon != null && favicon.originalHash != null){
			faviconHash = favicon.originalHash;
			faviconId = favicon.original.id;
		}else{
			faviconHash = null;
			faviconId = null;
		}

		if(splashImage != null && splashImage.originalHash != null){
			splashImageHash = splashImage.originalHash;
			splashImageId = splashImage.original.id;
		}else{
			splashImageHash = null;
			splashImageId = null;
		}

		if(loginImage != null && loginImage.originalHash != null){
			loginImageHash = loginImage.originalHash;
			loginImageSmallHash = loginImage.smallHash;
			loginImageId = loginImage.original.id;
			loginImageSmallId = loginImage.small.id;
		}else{
			loginImageHash = null;
			loginImageSmallHash = null;
			loginImageId = null;
			loginImageSmallId = null;
		}
	}

	public Integer getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public boolean isFacebookLoginAllowed() {
		return
				this.facebookAppID != null && !this.facebookAppID.isEmpty() &&
						this.facebookAppSecret != null && !this.facebookAppSecret.isEmpty();
	}

	public boolean isGoogleLoginAllowed() {
		return
				this.googleAppID != null && !this.googleAppID.isEmpty() &&
						this.googleAppSecret != null && !this.googleAppSecret.isEmpty();
	}

}