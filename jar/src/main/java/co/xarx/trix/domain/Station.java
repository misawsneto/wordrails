package co.xarx.trix.domain;

import co.xarx.trix.domain.page.Page;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Set;

@Entity
public class Station extends BaseEntity implements Serializable {
	public static final String RESTRICTED = "RESTRICTED";
	public static final String RESTRICTED_TO_NETWORKS = "RESTRICTED_TO_NETWORKS";
	public static final String UNRESTRICTED = "UNRESTRICTED";
	private static final long serialVersionUID = 7821358742575074731L;
	@Size(min = 1, max = 100)
	@NotNull
	public String name;

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

	@OneToMany(mappedBy = "station", cascade = CascadeType.REMOVE)
	public Set<StationRole> personsStationRoles;

	@JsonIgnore
	@OneToMany(mappedBy = "station")
	public Set<Post> posts;

	@OneToMany(mappedBy = "station")
	public Set<Page> pages;

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
	public Integer logoId;
	public Integer logoMediumId;

	public String logoHash;
	public String logoMediumHash;

	public Integer defaultPerspectiveId;

	@PrePersist
	public void onCreate() {
		onChange();
	}

	@PreUpdate
	public void onUpdate() {
		onChange();
	}

	void onChange() {
		if (logo != null && logo.originalHash != null) {
			logoHash = logo.originalHash;
			logoMediumHash = logo.mediumHash;

			logoId = logo.original.id;
			logoMediumId = logo.medium.id;
		}
	}

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

	@Override
	public String toString() {
		return "Station [id=" + id + ", name=" + name + ", visibility=" + visibility + "]";
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isWritable() {
		return writable;
	}

	public void setWritable(boolean writable) {
		this.writable = writable;
	}

	public boolean isMain() {
		return main;
	}

	public void setMain(boolean main) {
		this.main = main;
	}

	public String getVisibility() {
		return visibility;
	}

	public void setVisibility(String visibility) {
		this.visibility = visibility;
	}

	public boolean isAllowComments() {
		return allowComments;
	}

	public void setAllowComments(boolean allowComments) {
		this.allowComments = allowComments;
	}

	public boolean isAllowSocialShare() {
		return allowSocialShare;
	}

	public void setAllowSocialShare(boolean allowSocialShare) {
		this.allowSocialShare = allowSocialShare;
	}

	public boolean isAllowWritersToNotify() {
		return allowWritersToNotify;
	}

	public void setAllowWritersToNotify(boolean allowWritersToNotify) {
		this.allowWritersToNotify = allowWritersToNotify;
	}

	public boolean isAllowWritersToAddSponsors() {
		return allowWritersToAddSponsors;
	}

	public void setAllowWritersToAddSponsors(boolean allowWritersToAddSponsors) {
		this.allowWritersToAddSponsors = allowWritersToAddSponsors;
	}

	public String getBackgroundColor() {
		return backgroundColor;
	}

	public void setBackgroundColor(String backgroundColor) {
		this.backgroundColor = backgroundColor;
	}

	public String getNavbarColor() {
		return navbarColor;
	}

	public void setNavbarColor(String navbarColor) {
		this.navbarColor = navbarColor;
	}

	public String getPrimaryColor() {
		return primaryColor;
	}

	public void setPrimaryColor(String primaryColor) {
		this.primaryColor = primaryColor;
	}

	public Network getNetwork() {
		return network;
	}

	public void setNetwork(Network network) {
		this.network = network;
	}

	public Set<StationRole> getPersonsStationRoles() {
		return personsStationRoles;
	}

	public void setPersonsStationRoles(Set<StationRole> personsStationRoles) {
		this.personsStationRoles = personsStationRoles;
	}

	public Set<Post> getPosts() {
		return posts;
	}

	public void setPosts(Set<Post> posts) {
		this.posts = posts;
	}

	public Set<Page> getPages() {
		return pages;
	}

	public void setPages(Set<Page> pages) {
		this.pages = pages;
	}

	public Set<StationPerspective> getStationPerspectives() {
		return stationPerspectives;
	}

	public void setStationPerspectives(Set<StationPerspective> stationPerspectives) {
		this.stationPerspectives = stationPerspectives;
	}

	public Set<Taxonomy> getOwnedTaxonomies() {
		return ownedTaxonomies;
	}

	public void setOwnedTaxonomies(Set<Taxonomy> ownedTaxonomies) {
		this.ownedTaxonomies = ownedTaxonomies;
	}

	public Integer getCategoriesTaxonomyId() {
		return categoriesTaxonomyId;
	}

	public void setCategoriesTaxonomyId(Integer categoriesTaxonomyId) {
		this.categoriesTaxonomyId = categoriesTaxonomyId;
	}

	public Integer getTagsTaxonomyId() {
		return tagsTaxonomyId;
	}

	public void setTagsTaxonomyId(Integer tagsTaxonomyId) {
		this.tagsTaxonomyId = tagsTaxonomyId;
	}

	public int getPostsTitleSize() {
		return postsTitleSize;
	}

	public void setPostsTitleSize(int postsTitleSize) {
		this.postsTitleSize = postsTitleSize;
	}

	public boolean isTopper() {
		return topper;
	}

	public void setTopper(boolean topper) {
		this.topper = topper;
	}

	public boolean isSubheading() {
		return subheading;
	}

	public void setSubheading(boolean subheading) {
		this.subheading = subheading;
	}

	public boolean isSponsored() {
		return sponsored;
	}

	public void setSponsored(boolean sponsored) {
		this.sponsored = sponsored;
	}

	public boolean isShowAuthorSocialData() {
		return showAuthorSocialData;
	}

	public void setShowAuthorSocialData(boolean showAuthorSocialData) {
		this.showAuthorSocialData = showAuthorSocialData;
	}

	public boolean isShowAuthorData() {
		return showAuthorData;
	}

	public void setShowAuthorData(boolean showAuthorData) {
		this.showAuthorData = showAuthorData;
	}

	public Image getLogo() {
		return logo;
	}

	public void setLogo(Image logo) {
		this.logo = logo;
	}

	public Integer getLogoId() {
		return logoId;
	}

	public void setLogoId(Integer logoId) {
		this.logoId = logoId;
	}

	public Integer getLogoMediumId() {
		return logoMediumId;
	}

	public void setLogoMediumId(Integer logoMediumId) {
		this.logoMediumId = logoMediumId;
	}

	public String getLogoHash() {
		return logoHash;
	}

	public void setLogoHash(String logoHash) {
		this.logoHash = logoHash;
	}

	public String getLogoMediumHash() {
		return logoMediumHash;
	}

	public void setLogoMediumHash(String logoMediumHash) {
		this.logoMediumHash = logoMediumHash;
	}

	public Integer getDefaultPerspectiveId() {
		return defaultPerspectiveId;
	}

	public void setDefaultPerspectiveId(Integer defaultPerspectiveId) {
		this.defaultPerspectiveId = defaultPerspectiveId;
	}
}