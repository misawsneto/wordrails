package co.xarx.trix.domain;

import co.xarx.trix.annotation.SdkInclude;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

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
}, allowGetters = true, ignoreUnknown = true)
public class Post extends BaseEntity implements Serializable, ElasticSearchEntity {

	public static final String STATE_DRAFT = "DRAFT";
	public static final String STATE_NO_AUTHOR = "NOAUTHOR";
	public static final String STATE_TRASH = "TRASH";
	public static final String STATE_PUBLISHED = "PUBLISHED";
	public static final String STATE_UNPUBLISHED = "UNPUBLISHED";

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

	@SdkInclude
	@ManyToOne(fetch = FetchType.EAGER)
	public Video featuredVideo;

	@SdkInclude
	@ManyToOne(fetch = FetchType.EAGER)
	public Audio featuredAudio;

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
	public Integer bookmarksCount = 0;

	@Column(updatable = false)
	public Integer recommendsCount = 0;

	@Column(updatable = false)
	public Integer commentsCount = 0;

	@ManyToMany
	@JoinTable(name = "post_term", joinColumns = @JoinColumn(name = "posts_id"))
	public Set<Term> terms;

	@ElementCollection(fetch = FetchType.EAGER)
	@JoinTable(name = "post_tags", joinColumns = @JoinColumn(name = "post_id"))
	public Set<String> tags;

	@Column(columnDefinition = "boolean default true", nullable = false)
	public boolean imageLandscape = true;

	@Column(columnDefinition = "int(11) DEFAULT 0")
	public Integer readTime;

	@Column(columnDefinition = "boolean DEFAULT false")
	public boolean notify = false;

	public Double lat;

	public Double lng;

	@Getter(AccessLevel.NONE)
	public String featuredAudioHash;

	@SdkInclude
	public String getFeaturedVideoHash() {
		return featuredVideo != null ? (featuredVideo.file != null ? featuredVideo.file.hash : null) : null;
	}

	@SdkInclude
	public String getExternalVideoUrl() {
		return featuredVideo != null ? featuredVideo.getExternalVideoUrl() : null;
	}

	@SdkInclude
	public String getFeaturedAudioHash(){
		if(featuredAudioHash != null)
			return featuredAudioHash;

		return featuredAudio != null ? (featuredAudio.file != null ? featuredAudio.file.hash : null) : null;
	}

	@PrePersist
	public void onCreate() {
		onChanges();

		if (date == null)
			date = new Date();
		setCreatedAt(new Date());
	}

	@PreUpdate
	public void onUpdate() {
		onChanges();

		lastModificationDate = getUpdatedAt();
		setUpdatedAt(new Date());
	}

	private void onChanges() {
		stationId = station != null ? station.id : null;
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

	@SdkInclude
	public String getImageCredits(){
		if(featuredImage != null) return featuredImage.getCredits();

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
