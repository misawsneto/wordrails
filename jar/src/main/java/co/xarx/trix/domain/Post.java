package co.xarx.trix.domain;

import co.xarx.trix.domain.event.PostEvent;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.javers.core.metamodel.annotation.DiffIgnore;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

@Entity
public class Post extends BaseEntity implements Serializable, ElasticSearchEntity, Loggable {

    public static final String STATE_DRAFT = "DRAFT";
    public static final String STATE_NO_AUTHOR = "NOAUTHOR";
    public static final String STATE_TRASH = "TRASH";
    public static final String STATE_PUBLISHED = "PUBLISHED";
    public static final String STATE_SCHEDULED = "SCHEDULED";

	private static final long serialVersionUID = 7468718930497246401L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer id;

	@Override
	public Integer getId() {
		return id;
	}

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

	@DiffIgnore
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

	@Column
	@Size(min = 1, max = 15)
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

	@Override
	public boolean equals(Object obj) {
		if(id != null)
			return id.equals(((Post)obj).id);
		return super.equals(obj);
	}

    @Override
    public int hashCode() {
        if(id!=null)
            return id.hashCode() * 31;
        else
            return super.hashCode();
    }
	public Integer getOriginalPostId() {
		return originalPostId;
	}

	public void setOriginalPostId(Integer originalPostId) {
		this.originalPostId = originalPostId;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Date getLastModificationDate() {
		return lastModificationDate;
	}

	public void setLastModificationDate(Date lastModificationDate) {
		this.lastModificationDate = lastModificationDate;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
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

	public String getSubheading() {
		return subheading;
	}

	public void setSubheading(String subheading) {
		this.subheading = subheading;
	}

	public Sponsor getSponsor() {
		return sponsor;
	}

	public void setSponsor(Sponsor sponsor) {
		this.sponsor = sponsor;
	}

	public String getOriginalSlug() {
		return originalSlug;
	}

	public void setOriginalSlug(String originalSlug) {
		this.originalSlug = originalSlug;
	}

	public Date getScheduledDate() {
		return scheduledDate;
	}

	public void setScheduledDate(Date scheduledDate) {
		this.scheduledDate = scheduledDate;
	}

	public String getSlug() {
		return slug;
	}

	public void setSlug(String slug) {
		this.slug = slug;
	}

	public Set<Comment> getComments() {
		return comments;
	}

	public void setComments(Set<Comment> comments) {
		this.comments = comments;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public Image getFeaturedImage() {
		return featuredImage;
	}

	public void setFeaturedImage(Image featuredImage) {
		this.featuredImage = featuredImage;
	}

	public Set<Video> getVideos() {
		return videos;
	}

	public void setVideos(Set<Video> videos) {
		this.videos = videos;
	}

	public Person getAuthor() {
		return author;
	}

	public void setAuthor(Person author) {
		this.author = author;
	}

	public Station getStation() {
		return station;
	}

	public void setStation(Station station) {
		this.station = station;
	}

	public Integer getStationId() {
		return station.id;
	}

	public int getReadsCount() {
		return readsCount;
	}

	public void setReadsCount(int readsCount) {
		this.readsCount = readsCount;
	}

	public void incrementReadCount() {
		this.readsCount++;
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

	public Set<Term> getTerms() {
		return terms;
	}

	public void setTerms(Set<Term> terms) {
		this.terms = terms;
	}

	public Set<String> getTags() {
		return tags;
	}

	public void setTags(Set<String> tags) {
		this.tags = tags;
	}

	public boolean isImageLandscape() {
		return imageLandscape;
	}

	public void setImageLandscape(boolean imageLandscape) {
		this.imageLandscape = imageLandscape;
	}

	public String getExternalFeaturedImgUrl() {
		return externalFeaturedImgUrl;
	}

	public void setExternalFeaturedImgUrl(String externalFeaturedImgUrl) {
		this.externalFeaturedImgUrl = externalFeaturedImgUrl;
	}

	public String getExternalVideoUrl() {
		return externalVideoUrl;
	}

	public void setExternalVideoUrl(String externalVideoUrl) {
		this.externalVideoUrl = externalVideoUrl;
	}

	public int getReadTime() {
		return readTime;
	}

	public void setReadTime(int readTime) {
		this.readTime = readTime;
	}

	public boolean isNotify() {
		return notify;
	}

	public void setNotify(boolean notify) {
		this.notify = notify;
	}

	public String getImageCaptionText() {
		return imageCaptionText;
	}

	public void setImageCaptionText(String imageCaptionText) {
		this.imageCaptionText = imageCaptionText;
	}

	public String getImageCreditsText() {
		return imageCreditsText;
	}

	public void setImageCreditsText(String imageCreditsText) {
		this.imageCreditsText = imageCreditsText;
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

	public String getImageTitleText() {
		return imageTitleText;
	}

	public void setImageTitleText(String imageTitleText) {
		this.imageTitleText = imageTitleText;
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

	@Override
	public PostEvent build(String type, LogBuilder builder) {
		return builder.build(type, this);
	}
}
