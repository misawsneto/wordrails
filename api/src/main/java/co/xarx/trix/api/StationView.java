package co.xarx.trix.api;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class StationView implements Serializable {

	private static final long serialVersionUID = 2504331733945628049L;

	public Integer id;
	public Map<String, String> alertColors;
	public boolean allowComments;
	public boolean allowSocialShare;
	public boolean allowWritersToAddSponsors;
	public boolean allowWritersToNotify;
	public boolean allowClassifieds;
	public String backgroundColor;
	public Map<String, String> backgroundColors;
	public Integer categoriesTaxonomyId;
	public Integer defaultPerspectiveId;
	public Map<String, String> logo;
	public String logoHash;
	public String logoUrl;
	public boolean main;
	public String name;
	public String navbarColor;
	public String stationGroup;
	public int postsTitleSize;
	public String primaryColor;
	public Map<String, String> primaryColors;
	public Map<String, String> secondaryColors;
	public boolean showAuthorData;
	public boolean showAuthorSocialData;
	public boolean sponsored;
	public Set<String> stationPerspectives;
	public String stationSlug;
	public boolean subheading;
	public Integer tagsTaxonomyId;
	public boolean topper;
	public String visibility;
	public boolean writable;
	public List<TermView> categories;
	public Integer commentsCount;
	public String email;
	public int followersCount;
	public String coverImageUrl;
	public String geo;
	public String footer;
	public boolean hideHome;
	public GeoPointView geoPointDto;

	public String description;
	public String adminEmail;
	public String adminBio;
	public String adminName;
	public String logoId;
	public String coverImageId;
	public String adminLogo;
	public boolean privateStation;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Map<String, String> getAlertColors() {
		return alertColors;
	}

	public void setAlertColors(Map<String, String> alertColors) {
		this.alertColors = alertColors;
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

	public boolean isAllowWritersToAddSponsors() {
		return allowWritersToAddSponsors;
	}

	public void setAllowWritersToAddSponsors(boolean allowWritersToAddSponsors) {
		this.allowWritersToAddSponsors = allowWritersToAddSponsors;
	}

	public boolean isAllowWritersToNotify() {
		return allowWritersToNotify;
	}

	public void setAllowWritersToNotify(boolean allowWritersToNotify) {
		this.allowWritersToNotify = allowWritersToNotify;
	}

	public String getBackgroundColor() {
		return backgroundColor;
	}

	public void setBackgroundColor(String backgroundColor) {
		this.backgroundColor = backgroundColor;
	}

	public Map<String, String> getBackgroundColors() {
		return backgroundColors;
	}

	public void setBackgroundColors(Map<String, String> backgroundColors) {
		this.backgroundColors = backgroundColors;
	}

	public Integer getCategoriesTaxonomyId() {
		return categoriesTaxonomyId;
	}

	public void setCategoriesTaxonomyId(Integer categoriesTaxonomyId) {
		this.categoriesTaxonomyId = categoriesTaxonomyId;
	}

	public Integer getDefaultPerspectiveId() {
		return defaultPerspectiveId;
	}

	public void setDefaultPerspectiveId(Integer defaultPerspectiveId) {
		this.defaultPerspectiveId = defaultPerspectiveId;
	}

	public boolean isMain() {
		return main;
	}

	public void setMain(boolean main) {
		this.main = main;
	}

	public String getNavbarColor() {
		return navbarColor;
	}

	public void setNavbarColor(String navbarColor) {
		this.navbarColor = navbarColor;
	}

	public int getPostsTitleSize() {
		return postsTitleSize;
	}

	public void setPostsTitleSize(int postsTitleSize) {
		this.postsTitleSize = postsTitleSize;
	}

	public String getPrimaryColor() {
		return primaryColor;
	}

	public void setPrimaryColor(String primaryColor) {
		this.primaryColor = primaryColor;
	}

	public Map<String, String> getPrimaryColors() {
		return primaryColors;
	}

	public void setPrimaryColors(Map<String, String> primaryColors) {
		this.primaryColors = primaryColors;
	}

	public Map<String, String> getSecondaryColors() {
		return secondaryColors;
	}

	public void setSecondaryColors(Map<String, String> secondaryColors) {
		this.secondaryColors = secondaryColors;
	}

	public boolean isShowAuthorData() {
		return showAuthorData;
	}

	public void setShowAuthorData(boolean showAuthorData) {
		this.showAuthorData = showAuthorData;
	}

	public boolean isShowAuthorSocialData() {
		return showAuthorSocialData;
	}

	public void setShowAuthorSocialData(boolean showAuthorSocialData) {
		this.showAuthorSocialData = showAuthorSocialData;
	}

	public boolean isSponsored() {
		return sponsored;
	}

	public void setSponsored(boolean sponsored) {
		this.sponsored = sponsored;
	}

	public Set<String> getStationPerspectives() {
		return stationPerspectives;
	}

	public void setStationPerspectives(Set<String> stationPerspectives) {
		this.stationPerspectives = stationPerspectives;
	}

	public String getStationSlug() {
		return stationSlug;
	}

	public void setStationSlug(String stationSlug) {
		this.stationSlug = stationSlug;
	}

	public boolean isSubheading() {
		return subheading;
	}

	public void setSubheading(boolean subheading) {
		this.subheading = subheading;
	}

	public Integer getTagsTaxonomyId() {
		return tagsTaxonomyId;
	}

	public void setTagsTaxonomyId(Integer tagsTaxonomyId) {
		this.tagsTaxonomyId = tagsTaxonomyId;
	}

	public boolean isTopper() {
		return topper;
	}

	public void setTopper(boolean topper) {
		this.topper = topper;
	}

	public String getVisibility() {
		return visibility;
	}

	public void setVisibility(String visibility) {
		this.visibility = visibility;
	}

	public boolean isWritable() {
		return writable;
	}

	public void setWritable(boolean writable) {
		this.writable = writable;
	}

	public List<TermView> getCategories() {
		return categories;
	}

	public void setCategories(List<TermView> categories) {
		this.categories = categories;
	}

	public String getLogoHash(){
		if(logo != null){
			logoHash = logo.get("original");
			return logoHash;
		}
		return null;
	}
	public Map<String, String> getLogo() {
		return logo;
	}

	public String getLogoUrl() {
		return logoUrl;
	}

	public void setLogoUrl(String logoUrl) {
		this.logoUrl = logoUrl;
	}
}
