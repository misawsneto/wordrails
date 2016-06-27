package co.xarx.trix.api;

import co.xarx.trix.api.v2.CategoryData;
import co.xarx.trix.domain.Identifiable;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;


@lombok.Getter @lombok.Setter @lombok.NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PostView implements Serializable, Identifiable {

	private static final long serialVersionUID = -1474032487285763669L;

	public String title;

	@Id
	@JsonProperty("postId")
	public Integer postId;

	public String featuredImageHash;

	public String imageSmallHash;
	public String imageMediumHash;
	public String imageLargeHash;

	public Set<CategoryData> categories;
	public Set<String> tags;
    public List<TermView> terms;
	public Boolean sponsored;
	public java.util.Date date;
	public String snippet;
	public String body;
	public String topper;
	public String state;
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

	public String authorCover;
	public String authorProfilePicture;

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

	public String imageCredits;

	public String featuredVideoHash;
	public String featuredAudioHash;

//	public VideoDto video;

	public Double lat;
	public Double lng;

	public String subheading;
	public Date scheduledDate;
	public boolean notify;

	public ImageView featuredImage;

	@Override
	public Serializable getId() {
		return postId;
	}
}