package co.xarx.trix.api.v2;

import co.xarx.trix.domain.Identifiable;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;


@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PostData implements Serializable, Identifiable {

	@Id
	private Integer id;

	public Integer postId;
	public int blogId;
	public int siteId;

	private String title;
	private String snippet;
	private String body;
	private String subheading;
	public String cardType;
	private String topper;
	private String state;

	private Set<Integer> categoriesIds;
	private Set<CategoryData> categories;
	private Set<String> tags;
	private Set<TermData> terms;

	private Integer authorId;
	private String authorName;
	private String authorUsername;
	private PersonData author;

	private String authorImageHash;
	private String authorCoverHash;

//	public String authorCoverUrl;
//	public String authorImageUrl;
//	public String authorImageSmallUrl;
//	public String authorCoverMediumUrl;

	private Integer stationId;
	private String stationName;

	private Date date;

	private String imageUrl;
	private String smallImageUrl;
	private String mediumImageUrl;
	private String largeImageUrl;

	private String imageHash;
	private String videoHash;
	private String audioHash;

	private PostImageData image;
	private VideoData video;
	private AudioData audio;

	private String slug;

	private Double lat;
	private Double lng;

	public Integer focusX;
	public Integer focusY;

	public boolean bookmarked;
	public boolean recommended;

	private Integer bookmarksCount = 0;
	private Integer recommendsCount = 0;
	private Integer commentsCount = 0;

	private Integer readTime;

	private String externalVideoUrl;

	private Date scheduledDate;
	private Date unpublishDate;
	private Boolean notified = false;
	private String authorCoverUrl;
	private String authorImageUrl;

	public boolean allowComments;
	public boolean allowShare;

	public String urlContent;
	public boolean loadFromUrl;
	public String sharedLink;

	public void setBody(String body) {
		this.body = body;
		if (body == null || body.isEmpty())
			readTime = 0;

		Document doc = Jsoup.parse(body);
		body = doc.text();
		String[] wordArray = body.split("\\s+");
		int words = wordArray.length;
		readTime = 5 * words / 398;
	}
}