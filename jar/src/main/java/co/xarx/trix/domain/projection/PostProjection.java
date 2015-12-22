package co.xarx.trix.domain.projection;

import co.xarx.trix.domain.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.data.rest.core.config.Projection;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Projection(types = Post.class)
public interface PostProjection {

	Integer getId();

	@JsonFormat(shape = JsonFormat.Shape.NUMBER)
	Date getDate();

	String getTitle();

	String getSubheading();

	String getTopper();

	String getBody();

	Sponsor getSponsor();

	Person getAuthor();

	Station getStation();

	String getSlug();

	String getOriginalSlug();

	List<Term> getTerms();

	ImageProjection getFeaturedImage();

	List<ImageProjection> getImages();

	String getState();

	boolean getImageLandscape();

	Integer getImageId();

	Integer getImageSmallId();

	Integer getImageMediumId();

	Integer getImageLargeId();

	String getImageSmallHash();

	String getImageMediumHash();

	String getImageLargeHash();

	Integer getBookmarksCount();

	Integer getReadsCount();

	Integer getRecommendsCount();

	Integer getCommentsCount();

	Date getUpdatedAt();

	Date getScheduledDate();

	String getExternalFeaturedImgUrl();

	String getExternalVideoUrl();

	Integer getReadTime();

	String getImageCaptionText();

	String getImageCreditsText();

	String getImageTitleText();

	Boolean getNotify();

	Double getLat();

	Double getLng();

    Set<String> getTags();
}