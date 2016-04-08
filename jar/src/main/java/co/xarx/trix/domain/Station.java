package co.xarx.trix.domain;

import co.xarx.trix.annotation.SdkInclude;
import co.xarx.trix.domain.page.Page;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.rest.core.annotation.RestResource;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Set;


@Getter
@Setter
@Entity
@JsonIgnoreProperties(value = {
		"logoHash", "logoMediumHash"
}, allowGetters = true)
public class Station extends BaseEntity implements Serializable {
	public static final String RESTRICTED = "RESTRICTED";
	public static final String RESTRICTED_TO_NETWORKS = "RESTRICTED_TO_NETWORKS";
	public static final String UNRESTRICTED = "UNRESTRICTED";
	private static final long serialVersionUID = 7821358742575074731L;

	@Size(min = 1, max = 100)
	@NotNull
	public String name;

	@Id
	@Setter(AccessLevel.NONE)
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer id;

	@NotNull
	@Column(columnDefinition = "boolean default false", nullable = false)
	public boolean writable;

	@NotNull
	@Column(columnDefinition = "boolean default false", nullable = false)
	public boolean main = false;

	@NotNull
	public String visibility;

	@Column(columnDefinition = "boolean default false", nullable = false)
	public boolean allowComments;

	@Column(columnDefinition = "boolean default false", nullable = false)
	public boolean allowSocialShare;

	@Column(columnDefinition = "boolean default false", nullable = false)
	public boolean allowWritersToNotify;

	@Column(columnDefinition = "boolean default false", nullable = false)
	public boolean allowWritersToAddSponsors;

	@Column(columnDefinition = "varchar(255) default '#ffffff'")
	public String backgroundColor = "#ffffff";

	@Column(columnDefinition = "varchar(255) default '#ffffff'")
	public String navbarColor = "#ffffff";

	@Column(columnDefinition = "varchar(255) default '#5C78B0'")
	public String primaryColor = "#5C78B0";

	@ManyToOne
	public Network network;

	@RestResource(exported = false)
	@OneToMany(mappedBy = "station", cascade = CascadeType.REMOVE)
	public Set<StationRole> personsStationRoles;

	@JsonIgnore
	@OneToMany(mappedBy = "station")
	public Set<Post> posts;

	@OneToMany(mappedBy = "station", cascade = CascadeType.REMOVE)
	public Set<Page> pages;

	@SdkInclude
	@Size(min = 1)
	@NotNull
	@OneToMany(mappedBy = "station", cascade = CascadeType.PERSIST)
	public Set<StationPerspective> stationPerspectives;

	@OneToMany(mappedBy = "owningStation", cascade = CascadeType.PERSIST)
	public Set<Taxonomy> ownedTaxonomies;

	public Integer categoriesTaxonomyId;

	public Integer tagsTaxonomyId;

	public int postsTitleSize;

	@Column(columnDefinition = "boolean default false", nullable = false)
	public boolean topper;

	@Column(columnDefinition = "boolean default false", nullable = false)
	public boolean subheading;

	@Column(columnDefinition = "boolean default false", nullable = false)
	public boolean sponsored;

	@Column(columnDefinition = "boolean default false", nullable = false)
	public boolean showAuthorSocialData;

	@Column(columnDefinition = "boolean default false", nullable = false)
	public boolean showAuthorData;

	@OneToOne
	public Image logo;

	public Integer defaultPerspectiveId;

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((visibility == null) ? 0 : visibility.hashCode());
		result = prime * result + (writable ? 1231 : 1237);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		Station other = (Station) obj;
		if (id == null) {
			if (other.id != null) return false;
		} else if (!id.equals(other.id)) return false;
		if (name == null) {
			if (other.name != null) return false;
		} else if (!name.equals(other.name)) return false;
		if (visibility == null) {
			if (other.visibility != null) return false;
		} else if (!visibility.equals(other.visibility)) return false;
		return writable == other.writable;
	}

	@SdkInclude
	public String getLogoHash() {
		if (logo != null) return logo.getOriginalHash();

		return null;
	}

	@SdkInclude
	public String getLogoMediumHash() {
		if (logo != null) return logo.getMediumHash();

		return null;
	}
}