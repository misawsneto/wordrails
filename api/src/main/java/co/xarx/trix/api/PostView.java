package co.xarx.trix.api;

import co.xarx.trix.domain.ElasticSearchEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.elasticsearch.annotations.Document;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;
import java.util.Set;

@Document(indexName = "test")
@JsonIgnoreProperties(ignoreUnknown = true)
public class PostView implements Serializable, ElasticSearchEntity {

	private static final long serialVersionUID = -1474032487285763669L;

	@Override
	public Serializable getId() {
		return postId;
	}

	@Override
	public String getType() {
		return "post";
	}

	@JsonIgnore
	public String tenantId;

	@Override
	public String getTenantId() {
		return tenantId;
	}

	public static class Category {

		public Category(Integer id, String name) {
			this.id = id;
			this.name = name;
		}

		public Integer id;
		public String name;
	}

	public String title;
	public Integer postId;

	public Integer smallId;
	public Integer mediumId;
	public Integer largeId;
	public String smallHash;
	public String mediumHash;
	public String largeHash;

	public String imageSmallHash;
	public String imageMediumHash;
	public String imageLargeHash;

	public Set<Category> categories;
	public Set<String> tags;
	public Boolean sponsored;
	public java.util.Date date;
	public String snippet;
	public String body;
	public String topper;
	public String state;
	public int readsCount;
	public int favoritesCount;
	public int bookmarksCount;
	public int recommendsCount;
	public int commentsCount;

	public Integer imageId;
	public Integer imageSmallId;
	public Integer imageMediumId;
	public Integer imageLargeId;
	public Integer readTime;

	public boolean imageLandscape;

	public String authorName;
	public String authorUsername;
	public Integer authorId;

	public String authorEmail;
	public String authorTwitter;
	public Integer authorCoverMediumId;
	public Integer authorSmallImageId;
	public Integer authorImageSmallId;
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
	public Integer f;

	public Map<String, String> featuredImage;
}