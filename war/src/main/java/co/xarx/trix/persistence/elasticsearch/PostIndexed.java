package co.xarx.trix.persistence.elasticsearch;

import co.xarx.trix.domain.*;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;
import java.util.Set;


public class PostIndexed {

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

	public Boolean sponsored;
	public Sponsor sponsor;
	public Set<Comment> comments;
	public Set<Image> images;
	public Person author;
	public Station station;
	public Set<Term> terms;
	@JsonFormat(shape = JsonFormat.Shape.NUMBER)
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

	public String stationName;
	public Integer stationId;
	public String stationIdString;

	public String slug;
	public String externalFeaturedImgUrl;
	public String externalVideoUrl;

	public String imageCaptionText;
	public String imageCreditsText;
	public String imageTitleText;


	public Double lat;

	public Double lng;

	public String subheading;

	@JsonFormat(shape = JsonFormat.Shape.NUMBER)
	public Date scheduledDate;

	public boolean notify;
}
