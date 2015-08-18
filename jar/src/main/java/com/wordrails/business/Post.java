package com.wordrails.business;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wordrails.util.WordrailsUtil;

import org.apache.solr.analysis.*;
import org.hibernate.search.annotations.*;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Parameter;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import java.util.Date;
import java.util.Set;

@Entity
@Indexed(interceptor=PostIndexingInterceptor.class)
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(
		name="state",
		discriminatorType=DiscriminatorType.STRING
)
@DiscriminatorValue(value="PUBLISHED")
@AnalyzerDefs({
		// auto complete 1
		@AnalyzerDef(name = "autocompleteEdgeAnalyzer",
				// Split input into tokens according to tokenizer
				tokenizer = @TokenizerDef(factory = KeywordTokenizerFactory.class),

				filters = {
						// Normalize token text to lowercase, as the user is unlikely to
						// care about casing when searching for matches
						@TokenFilterDef(factory = PatternReplaceFilterFactory.class, params = {
								@Parameter(name = "pattern", value = "([^a-zA-Z0-9\\.])"),
								@Parameter(name = "replacement", value = " "),
								@Parameter(name = "replace", value = "all")}),
						@TokenFilterDef(factory = LowerCaseFilterFactory.class),
						@TokenFilterDef(factory = StopFilterFactory.class),
						// Index partial words starting at the front, so we can provide
						// Autocomplete functionality
						@TokenFilterDef(factory = EdgeNGramFilterFactory.class, params = {
								@Parameter(name = "minGramSize", value = "3"),
								@Parameter(name = "maxGramSize", value = "50")})
				},
				charFilters = {@CharFilterDef(factory = HTMLStripCharFilterFactory.class)}),

		// auto complete 2
		@AnalyzerDef(name = "autocompleteNGramAnalyzer",

				// Split input into tokens according to tokenizer
				tokenizer = @TokenizerDef(factory = StandardTokenizerFactory.class),

				filters = {
						// Normalize token text to lowercase, as the user is unlikely to
						// care about casing when searching for matches
						@TokenFilterDef(factory = WordDelimiterFilterFactory.class),
						@TokenFilterDef(factory = LowerCaseFilterFactory.class),
						@TokenFilterDef(factory = NGramFilterFactory.class, params = {
								@Parameter(name = "minGramSize", value = "3"),
								@Parameter(name = "maxGramSize", value = "5")}),
						@TokenFilterDef(factory = PatternReplaceFilterFactory.class, params = {
								@Parameter(name = "pattern", value = "([^a-zA-Z0-9\\.])"),
								@Parameter(name = "replacement", value = " "),
								@Parameter(name = "replace", value = "all")})
				},
				charFilters = {@CharFilterDef(factory = HTMLStripCharFilterFactory.class)}),

		// potuguese, striphtml
		@AnalyzerDef(name = "customPostAnalyzer",

				tokenizer = @TokenizerDef(factory = StandardTokenizerFactory.class),
				filters = {
						@TokenFilterDef(factory = WordDelimiterFilterFactory.class),
						@TokenFilterDef(factory = LowerCaseFilterFactory.class),
						@TokenFilterDef(factory = SnowballPorterFilterFactory.class, params = {
								@Parameter(name = "resource_charset", value = "UTF-8"),
								@Parameter(name = "language", value = "Portuguese")
						}),
				},
				charFilters = {@CharFilterDef(factory = HTMLStripCharFilterFactory.class)})
})
@Spatial
//@Table(uniqueConstraints=@UniqueConstraint(columnNames={"slug", "state"}))
public class Post {

	public static final String STATE_DRAFT = "DRAFT";
	public static final String STATE_NO_AUTHOR = "NOAUTHOR";
	public static final String STATE_TRASH = "TRASH";
	public static final String STATE_HISTORY = "HISTORY";
	public static final String STATE_PUBLISHED = "PUBLISHED";
	public static final String STATE_SCHEDULED = "SCHEDULED";

	public Post() {
		state = Post.STATE_PUBLISHED;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@DocumentId
	public Integer id;

	@Field
	public Integer originalPostId;

	@Field
	public Integer wordpressId;

	@JsonFormat(shape = JsonFormat.Shape.NUMBER)
	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	@Field(analyze = Analyze.NO)
	@DateBridge(resolution = Resolution.SECOND)
	public Date date;

	@JsonFormat(shape = JsonFormat.Shape.NUMBER)
	@Temporal(TemporalType.TIMESTAMP)
	public Date lastModificationDate;

	@Field(index = Index.YES, analyze = Analyze.YES, store = Store.NO)
	@Analyzer(definition = "customPostAnalyzer")
	public String title;

	@Lob
	@Field(index = Index.YES, analyze = Analyze.YES, store = Store.NO)
	@Analyzer(definition = "customPostAnalyzer")
	public String body;

	@Lob
	@Field(index = Index.YES, analyze = Analyze.YES, store = Store.NO)
	@Analyzer(definition = "customPostAnalyzer")
	@Column(length = 1024)
	public String topper;

	@Lob
	@Field(index = Index.YES, analyze = Analyze.YES, store = Store.NO)
	@Analyzer(definition = "customPostAnalyzer")
	@Column(length = 1024)
	public String subheading;

	@ManyToOne
	public Sponsor sponsor;

	public String originalSlug;

	@JsonFormat(shape = JsonFormat.Shape.NUMBER)
	@Temporal(TemporalType.TIMESTAMP)
	public Date scheduledDate;

	@Column(unique = true)
	public String slug;

	@OneToMany(mappedBy = "post")
	public Set<Comment> comments;

	@Size(min = 1, max = 15)
	@Field(analyze = Analyze.NO)
	@Column(insertable = false, updatable = false)
	public String state;

	@ManyToOne
	public Image featuredImage;

	@OneToMany(mappedBy = "post")
	public Set<Image> images;

	@NotNull
	@ManyToOne
	@JoinColumn(updatable = false)
	@IndexedEmbedded(includePaths={"name", "id"})
	public Person author;

	@OneToMany(mappedBy = "post")
	public Set<Promotion> promotions;

	@NotNull
	@ManyToOne
	@JoinColumn(updatable = false)
	public Station station;

	@Field
	@NumericField
	public Integer stationId;

	@Field
	@NumericField
	@Column(updatable = false)
	public int readsCount = 0;

	@Field
	@NumericField
	@Column(updatable = false)
	public int bookmarksCount = 0;

	@Field
	@NumericField
	@Column(updatable = false)
	public int recommendsCount = 0;

	@Field
	@NumericField
	@Column(updatable = false)
	public int commentsCount = 0;

	@ManyToMany
	@IndexedEmbedded(includePaths={"name", "id", "taxonomyId"})
	public Set<Term> terms;

	@Field(analyze = Analyze.NO)
	@Column(columnDefinition = "boolean default true", nullable = false)
	public boolean imageLandscape = true;

	@JsonFormat(shape = JsonFormat.Shape.NUMBER)
	@Temporal(TemporalType.TIMESTAMP)
	@Field(analyze = Analyze.NO)
	@DateBridge(resolution = Resolution.SECOND)
	public Date updatedAt;

	@JsonFormat(shape = JsonFormat.Shape.NUMBER)
	@Temporal(TemporalType.TIMESTAMP)
	@Field(analyze = Analyze.NO)
	@DateBridge(resolution = Resolution.SECOND)
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

	@Latitude
	public Double lat;

	@Longitude
	public Double lng;

	@Lob
	public String imageTitleText;

	@ManyToOne
	Post post;

	@PrePersist
	public void onCreate() {
		if (featuredImage != null && featuredImage.original != null) {
			imageId = featuredImage.original.id;
			imageSmallId = featuredImage.small.id;
			imageMediumId = featuredImage.medium.id;
			imageLargeId = featuredImage.large.id;
//			imageLandscape = !featuredImage.vertical;
			imageCaptionText = featuredImage.caption;
			imageCreditsText = featuredImage.credits;
			imageTitleText = featuredImage.title;
		} else {
			imageId = null;
			imageSmallId = null;
			imageMediumId = null;
			imageLargeId = null;
		}

		readTime = WordrailsUtil.calculateReadTime(body);
		if (date == null)
			date = new Date();
		createdAt = new Date();
		stationId = station.id;
	}

	@PreUpdate
	public void onUpdate() {
		if (featuredImage != null && featuredImage.original != null) {
			imageId = featuredImage.original.id;
			imageSmallId = featuredImage.small.id;
			imageMediumId = featuredImage.medium.id;
			imageLargeId = featuredImage.large.id;
//			imageLandscape = !featuredImage.vertical;
			imageCaptionText = featuredImage.caption;
			imageCreditsText = featuredImage.credits;
			imageTitleText = featuredImage.title;
		} else {
			imageId = null;
			imageSmallId = null;
			imageMediumId = null;
			imageLargeId = null;
		}

		updatedAt = new Date();
		lastModificationDate = updatedAt;
		readTime = WordrailsUtil.calculateReadTime(body);
		stationId = station.id;
	}

	public void convertSubtype(Post post) {
		Class<Post> postClass = Post.class;

		for (java.lang.reflect.Field field : postClass.getFields()) {
			String fieldName = field.getName();
			if((field.getModifiers() & java.lang.reflect.Modifier.FINAL) == java.lang.reflect.Modifier.FINAL) {
				continue; //field is final
			}
			if(fieldName.equals("id") || fieldName.equals("state"))
				continue; //state is final and id is autogenerated
			try {
				field.set(this, field.get(post));
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}

	public Integer imageId;
	public Integer imageSmallId;
	public Integer imageMediumId;
	public Integer imageLargeId;

	@Override
	public String toString() {
		return "Post [id=" + id + ", wordpressId=" + wordpressId + ", date=" + date
				+ ", lastModificationDate=" + lastModificationDate + ", title="
				+ title + ", state=" + state + "]";
	}

}
