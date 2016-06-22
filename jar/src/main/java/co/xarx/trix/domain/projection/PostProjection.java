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

	Person getAuthor();

	Station getStation();

	String getSlug();

	String getOriginalSlug();

	List<Term> getTerms();

	ImageProjection getFeaturedImage();

	String getState();

	boolean getImageLandscape();

	Integer getBookmarksCount();

	Integer getRecommendsCount();

	Integer getCommentsCount();

	@JsonFormat(shape = JsonFormat.Shape.NUMBER)
	Date getCreatedAt();

	@JsonFormat(shape = JsonFormat.Shape.NUMBER)
	Date getUpdatedAt();

	@JsonFormat(shape = JsonFormat.Shape.NUMBER)
	Date getScheduledDate();

	String getExternalVideoUrl();

	Integer getReadTime();

	String getImageCaptionText();

	String getImageCreditsText();

	String getImageTitleText();

	Boolean getNotify();

	Double getLat();

	Double getLng();

    Set<String> getTags();

	// multimedia: images, video, audio ------------------
	String getImageSmallHash();

	String getImageMediumHash();

	String getImageLargeHash();

	String getFeaturedAudioHash();

	String getFeaturedVideoHash();
}