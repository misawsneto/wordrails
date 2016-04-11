package co.xarx.trix.domain;

import co.xarx.trix.annotation.SdkInclude;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AccessLevel;
import lombok.Setter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.data.rest.core.annotation.RestResource;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

@lombok.Getter @lombok.Setter
@Entity
@JsonIgnoreProperties(value = {
		"imageHash", "imageLargeHash", "imageMediumHash", "imageSmallHash"
}, allowGetters = true)
public class Post extends BaseEntity implements Serializable, ElasticSearchEntity{

	public static final String STATE_DRAFT = "DRAFT";
	public static final String STATE_NO_AUTHOR = "NOAUTHOR";
	public static final String STATE_TRASH = "TRASH";
	public static final String STATE_PUBLISHED = "PUBLISHED";
	public static final String STATE_SCHEDULED = "SCHEDULED";

	private static final long serialVersionUID = 7468718930497246401L;

	@Id
	@Setter(AccessLevel.NONE)
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer id;

	public Post() {
		state = Post.STATE_PUBLISHED;
	}

	@Override
	public String getType() {
		return "post";
	}

	public Integer originalPostId;

	@JsonFormat(shape = JsonFormat.Shape.NUMBER)
	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	public Date date;

	@JsonFormat(shape = JsonFormat.Shape.NUMBER)
	@Temporal(TemporalType.TIMESTAMP)
	public Date lastModificationDate;

	@Lob
	@Column(length = 1024)
	public String title;

	@Lob
	public String body;

	@Lob
	@Column(length = 1024)
	public String topper;

	@Lob
	@Column(length = 1024)
	public String subheading;

	@Lob
	public String originalSlug;

	@JsonFormat(shape = JsonFormat.Shape.NUMBER)
	@Temporal(TemporalType.TIMESTAMP)
	public Date scheduledDate;

	@Lob
	@Column(unique = true)
	public String slug;

	@OneToMany(mappedBy = "post")
	public Set<Comment> comments;

	@Column
	@Size(min = 1, max = 15)
	public String state;

	@SdkInclude
	@ManyToOne(fetch = FetchType.EAGER)
	public Image featuredImage;

	@RestResource(exported = false)
	@OneToMany
	@JoinTable(
			name="post_video",
			joinColumns = @JoinColumn( name="post_id"),
			inverseJoinColumns = @JoinColumn( name="video_id")
	)
	public Set<Video> videos;

	@SdkInclude
	@NotNull
	@ManyToOne
	@JoinColumn(updatable = false)
	public Person author;

	@SdkInclude
	@NotNull
	@ManyToOne
	@JoinColumn(updatable = false)
	public Station station;

	public Integer stationId;

	@Column(updatable = false)
	public int readsCount = 0;

	@Column(updatable = false)
	public int bookmarksCount = 0;

	@Column(updatable = false)
	public int recommendsCount = 0;

	@Column(updatable = false)
	public int commentsCount = 0;

	@ManyToMany(fetch = FetchType.EAGER)
	public Set<Term> terms;

	@ElementCollection(fetch = FetchType.EAGER)
	@JoinTable(name = "post_tags", joinColumns = @JoinColumn(name = "post_id"))
	public Set<String> tags;

	@Column(columnDefinition = "boolean default true", nullable = false)
	public boolean imageLandscape = true;

	@Column(length = 1024)
	public String externalFeaturedImgUrl;

	@Column(length = 1024)
	public String externalVideoUrl;

	@Column(columnDefinition = "int(11) DEFAULT 0")
	public int readTime;

	@Column(columnDefinition = "boolean DEFAULT false")
	public boolean notify = false;

	@Lob
	public String imageCaptionText;

	@Lob
	@Deprecated
	public String imageCreditsText;

	public Double lat;

	public Double lng;

	@Lob
	@Deprecated
	public String imageTitleText;

	public String featuredVideoHash;
	public String featuredAudioHash;

	@PrePersist
	public void onCreate() {
		onChanges();

		if (date == null)
			date = new Date();
		createdAt = new Date();
	}

	@PreUpdate
	public void onUpdate() {
		onChanges();

		updatedAt = new Date();
		lastModificationDate = updatedAt;
	}

	private void onChanges() {
		stationId = station.id;
		readTime = calculateReadTime(body);
	}

	public static int countWords(String string) {
		if (string == null || string.isEmpty()) return 0;

		Document doc = Jsoup.parse(string);
		string = doc.text();
		String[] wordArray = string.split("\\s+");
		return wordArray.length;
	}

	public static int calculateReadTime(String string) {
		int words = countWords(string);
		int minutes = 5 * words / 398;
		return minutes;
	}

	@Override
	public String toString() {
		return "Post [id=" + id + ", date=" + date
				+ ", lastModificationDate=" + lastModificationDate + ", title=" + title + ", state=" + state + "]";
	}

	@SdkInclude
	public String getImageHash() {
		if (featuredImage != null) return featuredImage.getOriginalHash();

		return null;
	}

	@Deprecated
	@SdkInclude
	public String getImageLargeHash() {
		if (featuredImage != null) return featuredImage.getLargeHash();

		return null;
	}

	@Deprecated
	@SdkInclude
	public String getImageMediumHash() {
		if (featuredImage != null) return featuredImage.getMediumHash();

		return null;
	}

	@Deprecated
	@SdkInclude
	public String getImageSmallHash() {
		if (featuredImage != null) return featuredImage.getSmallHash();

		return null;
	}

	@Deprecated
	@SdkInclude // fuck me!!
	public Integer getImageId() {
		if (featuredImage != null) return featuredImage.getId();

		return null;
	}
}
