package co.xarx.trix.elasticsearch.domain;

import co.xarx.trix.api.Category;
import co.xarx.trix.domain.ElasticSearchEntity;
import co.xarx.trix.domain.Person;
import co.xarx.trix.util.Constants;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldIndex;

import java.util.Date;
import java.util.Map;
import java.util.Set;

@Document(indexName = "${elasticsearch.index}", type = Constants.ObjectType.POST)
public class ESPost implements ElasticSearchEntity {

	@Id
	public Integer id;
	public String tenantId;
	public Integer networkId;

	public Integer authorId;

	@JsonIgnore
	@Field(index = FieldIndex.not_analyzed)
	public Person author;

	public Integer stationId;
	public String stationName;

	public String title;

	public Set<Category> categories;
	public Set<String> tags;
	public Boolean sponsored;
	public Date date;
	public String body;
	public String topper;
	public String state;

	public int readsCount;
	public int bookmarksCount;
	public int recommendsCount;
	public int commentsCount;

	public String slug;
	public Integer readTime;

	public Map<String, String> featuredImage;
	public boolean imageLandscape;
	public String featuredImageCaption;
	public String featuredImageCredits;
	public String featuredImageTitle;

	public String featuredVideoHash;
	public String featuredAudioHash;

	public Double lat;
	public Double lng;

	public String subheading;
	public Date scheduledDate;
	public boolean notify;

	@Id
	@Override
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Person getAuthor() {
		return author;
	}

	public void setAuthor(Person author) {
		this.author = author;
	}

	public String getStationName() {
		return stationName;
	}

	public void setStationName(String stationName) {
		this.stationName = stationName;
	}

	@Override
	public String getType() {
		return Constants.ObjectType.POST;
	}

	@Override
	public String getTenantId() {
		return tenantId;
	}

	@Override
	public Integer getNetworkId() {
		return networkId;
	}

	@Override
	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	@Override
	public void setNetworkId(Integer networkId) {
		this.networkId = networkId;
	}

	public void setStationId(Integer stationId) {
		this.stationId = stationId;
	}

	public Integer getStationId() {
		return stationId;
	}

	public Set<String> getTags() {
		return tags;
	}

	public void setTags(Set<String> tags) {
		this.tags = tags;
	}

	public Integer getAuthorId() {
		return authorId;
	}

	public void setAuthorId(Integer authorId) {
		this.authorId = authorId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Set<Category> getCategories() {
		return categories;
	}

	public void setCategories(Set<Category> categories) {
		this.categories = categories;
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

	public String getSlug() {
		return slug;
	}

	public void setSlug(String slug) {
		this.slug = slug;
	}

	public Integer getReadTime() {
		return readTime;
	}

	public void setReadTime(Integer readTime) {
		this.readTime = readTime;
	}

	public Map<String, String> getFeaturedImage() {
		return featuredImage;
	}

	public void setFeaturedImage(Map<String, String> featuredImage) {
		this.featuredImage = featuredImage;
	}

	public boolean isImageLandscape() {
		return imageLandscape;
	}

	public void setImageLandscape(boolean imageLandscape) {
		this.imageLandscape = imageLandscape;
	}

	public String getFeaturedImageCaption() {
		return featuredImageCaption;
	}

	public void setFeaturedImageCaption(String featuredImageCaption) {
		this.featuredImageCaption = featuredImageCaption;
	}

	public String getFeaturedImageCredits() {
		return featuredImageCredits;
	}

	public void setFeaturedImageCredits(String featuredImageCredits) {
		this.featuredImageCredits = featuredImageCredits;
	}

	public String getFeaturedImageTitle() {
		return featuredImageTitle;
	}

	public void setFeaturedImageTitle(String featuredImageTitle) {
		this.featuredImageTitle = featuredImageTitle;
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
}
