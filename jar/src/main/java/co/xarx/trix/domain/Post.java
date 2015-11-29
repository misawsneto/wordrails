package co.xarx.trix.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

@Entity
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(
		name="state",
		discriminatorType=DiscriminatorType.STRING
)
@DiscriminatorValue(value="PUBLISHED")
public class Post implements Serializable, Identifiable {

	public static final String STATE_DRAFT = "DRAFT";
	public static final String STATE_NO_AUTHOR = "NOAUTHOR";
	public static final String STATE_TRASH = "TRASH";
	public static final String STATE_PUBLISHED = "PUBLISHED";
	public static final String STATE_SCHEDULED = "SCHEDULED";

	public Post() {
		state = Post.STATE_PUBLISHED;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer id;

	@Override
	public Integer getId() {
		return id;
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

	@ManyToOne
	public Sponsor sponsor;

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

	@Size(min = 1, max = 15)
	@Column(insertable = false, updatable = false)
	public String state;

	@ManyToOne(fetch = FetchType.EAGER)
	public Image featuredImage;

	@OneToMany
	@JoinTable(
			name="post_video",
			joinColumns = @JoinColumn( name="post_id"),
			inverseJoinColumns = @JoinColumn( name="video_id")
	)
	public Set<Video> videos;

	@OneToMany
	@JoinTable(name = "post_image", joinColumns = @JoinColumn(name = "post_id"))
	public Set<Image> images;

	@NotNull
	@ManyToOne
	@JoinColumn(updatable = false)
	public Person author;

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

	@ManyToMany
	public Set<Term> terms;

	@Column(columnDefinition = "boolean default true", nullable = false)
	public boolean imageLandscape = true;

	@JsonFormat(shape = JsonFormat.Shape.NUMBER)
	@Temporal(TemporalType.TIMESTAMP)
	public Date updatedAt;

	@JsonFormat(shape = JsonFormat.Shape.NUMBER)
	@Temporal(TemporalType.TIMESTAMP)
	@Column(updatable = false)
	public Date createdAt;

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
	public String imageCreditsText;

	public Double lat;

	public Double lng;

	@Lob
	public String imageTitleText;

	public Network network;

	public Integer imageId;
	public Integer imageSmallId;
	public Integer imageMediumId;
	public Integer imageLargeId;

	public String imageHash;
	public String imageSmallHash;
	public String imageMediumHash;
	public String imageLargeHash;

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

		if (featuredImage != null && featuredImage.originalHash != null) {
			imageHash = featuredImage.originalHash;
			imageSmallHash = featuredImage.smallHash;
			imageMediumHash = featuredImage.mediumHash;
			imageLargeHash = featuredImage.largeHash;

			imageId = featuredImage.original.id;
			imageSmallId = featuredImage.small.id;
			imageMediumId = featuredImage.medium.id;
			imageLargeId = featuredImage.large.id;

			imageCaptionText = featuredImage.caption;
			imageCreditsText = featuredImage.credits;
			imageTitleText = featuredImage.title;
		} else {
			imageId = null;
			imageSmallId = null;
			imageMediumId = null;
			imageLargeId = null;

			imageHash = null;
			imageSmallHash = null;
			imageMediumHash = null;
			imageLargeHash = null;
		}
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

}
