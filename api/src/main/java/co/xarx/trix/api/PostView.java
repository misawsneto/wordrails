package co.xarx.trix.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PostView implements Serializable {

	private static final long serialVersionUID = -1474032487285763669L;

	public String title;

	@Id
	@JsonProperty("postId")
	public Integer id;

	public String featuredImage;

	public String smallHash;
	public String mediumHash;
	public String largeHash;

	public String imageSmallHash;
	public String imageMediumHash;
	public String imageLargeHash;

	public Set<Category> categories;
	public Set<String> tags;
    public List<TermView> terms;
	public Boolean sponsored;
	public java.util.Date date;
	public String snippet;
	public String body;
	public String topper;
	public String state;
	public int readsCount;
	public int bookmarksCount;
	public int recommendsCount;
	public int commentsCount;

	public Integer readTime;

	public boolean imageLandscape;

	public String authorName;
	public String authorUsername;
	public Integer authorId;

	public String authorEmail;
	public String authorTwitter;
	public String authorCoverMediumHash;
	public String authorImageSmallHash;
	public String authorCoverUrl;
	public String authorImageUrl;

	public String stationName;
	public Integer stationId;
	public String stationIdString;

	public String slug;
	public String externalFeaturedImgUrl;
	public String externalVideoUrl;

	public String imageCaptionText;
	public String imageCreditsText;
	public String imageTitleText;

	public String featuredVideoHash;
	public String featuredAudioHash;

	public Double lat;
	public Double lng;

	public String subheading;
	public Date scheduledDate;
	public boolean notify;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getSmallHash() {
		return smallHash;
	}

	public void setSmallHash(String smallHash) {
		this.smallHash = smallHash;
	}

	public String getMediumHash() {
		return mediumHash;
	}

	public void setMediumHash(String mediumHash) {
		this.mediumHash = mediumHash;
	}

	public String getLargeHash() {
		return largeHash;
	}

	public void setLargeHash(String largeHash) {
		this.largeHash = largeHash;
	}

	public String getImageSmallHash() {
		return imageSmallHash;
	}

	public void setImageSmallHash(String imageSmallHash) {
		this.imageSmallHash = imageSmallHash;
	}

	public String getImageMediumHash() {
		return imageMediumHash;
	}

	public void setImageMediumHash(String imageMediumHash) {
		this.imageMediumHash = imageMediumHash;
	}

	public String getImageLargeHash() {
		return imageLargeHash;
	}

	public void setImageLargeHash(String imageLargeHash) {
		this.imageLargeHash = imageLargeHash;
	}

	public Set<Category> getCategories() {
		return categories;
	}

	public void setCategories(Set<Category> categories) {
		this.categories = categories;
	}

	public Set<String> getTags() {
		return tags;
	}

	public void setTags(Set<String> tags) {
		this.tags = tags;
	}

	public Boolean getSponsored() {
		return sponsored;
	}

	public void setSponsored(Boolean sponsored) {
		this.sponsored = sponsored;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getSnippet() {
		return snippet;
	}

	public void setSnippet(String snippet) {
		this.snippet = snippet;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getTopper() {
		return topper;
	}

	public void setTopper(String topper) {
		this.topper = topper;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public int getReadsCount() {
		return readsCount;
	}

	public void setReadsCount(int readsCount) {
		this.readsCount = readsCount;
	}

	public int getBookmarksCount() {
		return bookmarksCount;
	}

	public void setBookmarksCount(int bookmarksCount) {
		this.bookmarksCount = bookmarksCount;
	}

	public int getRecommendsCount() {
		return recommendsCount;
	}

	public void setRecommendsCount(int recommendsCount) {
		this.recommendsCount = recommendsCount;
	}

	public int getCommentsCount() {
		return commentsCount;
	}

	public void setCommentsCount(int commentsCount) {
		this.commentsCount = commentsCount;
	}

	public Integer getReadTime() {
		return readTime;
	}

	public void setReadTime(Integer readTime) {
		this.readTime = readTime;
	}

	public boolean isImageLandscape() {
		return imageLandscape;
	}

	public void setImageLandscape(boolean imageLandscape) {
		this.imageLandscape = imageLandscape;
	}

	public String getAuthorName() {
		return authorName;
	}

	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}

	public String getAuthorUsername() {
		return authorUsername;
	}

	public void setAuthorUsername(String authorUsername) {
		this.authorUsername = authorUsername;
	}

	public Integer getAuthorId() {
		return authorId;
	}

	public void setAuthorId(Integer authorId) {
		this.authorId = authorId;
	}

	public String getAuthorEmail() {
		return authorEmail;
	}

	public void setAuthorEmail(String authorEmail) {
		this.authorEmail = authorEmail;
	}

	public String getAuthorTwitter() {
		return authorTwitter;
	}

	public void setAuthorTwitter(String authorTwitter) {
		this.authorTwitter = authorTwitter;
	}

	public String getAuthorCoverMediumHash() {
		return authorCoverMediumHash;
	}

	public void setAuthorCoverMediumHash(String authorCoverMediumHash) {
		this.authorCoverMediumHash = authorCoverMediumHash;
	}

	public String getAuthorImageSmallHash() {
		return authorImageSmallHash;
	}

	public void setAuthorImageSmallHash(String authorImageSmallHash) {
		this.authorImageSmallHash = authorImageSmallHash;
	}

	public String getAuthorCoverUrl() {
		return authorCoverUrl;
	}

	public void setAuthorCoverUrl(String authorCoverUrl) {
		this.authorCoverUrl = authorCoverUrl;
	}

	public String getAuthorImageUrl() {
		return authorImageUrl;
	}

	public void setAuthorImageUrl(String authorImageUrl) {
		this.authorImageUrl = authorImageUrl;
	}

	public String getStationName() {
		return stationName;
	}

	public void setStationName(String stationName) {
		this.stationName = stationName;
	}

	public Integer getStationId() {
		return stationId;
	}

	public void setStationId(Integer stationId) {
		this.stationId = stationId;
	}

	public String getStationIdString() {
		return stationIdString;
	}

	public void setStationIdString(String stationIdString) {
		this.stationIdString = stationIdString;
	}

	public String getSlug() {
		return slug;
	}

	public void setSlug(String slug) {
		this.slug = slug;
	}

	public String getExternalFeaturedImgUrl() {
		return externalFeaturedImgUrl;
	}

	public void setExternalFeaturedImgUrl(String externalFeaturedImgUrl) {
		this.externalFeaturedImgUrl = externalFeaturedImgUrl;
	}

	public String getExternalVideoUrl() {
		return externalVideoUrl;
	}

	public void setExternalVideoUrl(String externalVideoUrl) {
		this.externalVideoUrl = externalVideoUrl;
	}

	public String getImageCaptionText() {
		return imageCaptionText;
	}

	public void setImageCaptionText(String imageCaptionText) {
		this.imageCaptionText = imageCaptionText;
	}

	public String getImageCreditsText() {
		return imageCreditsText;
	}

	public void setImageCreditsText(String imageCreditsText) {
		this.imageCreditsText = imageCreditsText;
	}

	public String getImageTitleText() {
		return imageTitleText;
	}

	public void setImageTitleText(String imageTitleText) {
		this.imageTitleText = imageTitleText;
	}

	public String getFeaturedVideoHash() {
		return featuredVideoHash;
	}

	public void setFeaturedVideoHash(String featuredVideoHash) {
		this.featuredVideoHash = featuredVideoHash;
	}

	public String getFeaturedAudioHash() {
		return featuredAudioHash;
	}

	public void setFeaturedAudioHash(String featuredAudioHash) {
		this.featuredAudioHash = featuredAudioHash;
	}

	public Double getLat() {
		return lat;
	}

	public void setLat(Double lat) {
		this.lat = lat;
	}

	public Double getLng() {
		return lng;
	}

	public void setLng(Double lng) {
		this.lng = lng;
	}

	public String getSubheading() {
		return subheading;
	}

	public void setSubheading(String subheading) {
		this.subheading = subheading;
	}

	public Date getScheduledDate() {
		return scheduledDate;
	}

	public void setScheduledDate(Date scheduledDate) {
		this.scheduledDate = scheduledDate;
	}

	public boolean isNotify() {
		return notify;
	}

	public void setNotify(boolean notify) {
		this.notify = notify;
	}

	public String getFeaturedImage() {
		return featuredImage;
	}

	public void setFeaturedImage(String featuredImage) {
		this.featuredImage = featuredImage;
	}
}