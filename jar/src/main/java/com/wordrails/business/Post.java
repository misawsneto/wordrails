package com.wordrails.business;

import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.apache.solr.analysis.EdgeNGramFilterFactory;
import org.apache.solr.analysis.HTMLStripCharFilterFactory;
import org.apache.solr.analysis.KeywordTokenizerFactory;
import org.apache.solr.analysis.LowerCaseFilterFactory;
import org.apache.solr.analysis.NGramFilterFactory;
import org.apache.solr.analysis.PatternReplaceFilterFactory;
import org.apache.solr.analysis.SnowballPorterFilterFactory;
import org.apache.solr.analysis.StandardTokenizerFactory;
import org.apache.solr.analysis.StopFilterFactory;
import org.apache.solr.analysis.WordDelimiterFilterFactory;
import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Analyzer;
import org.hibernate.search.annotations.AnalyzerDef;
import org.hibernate.search.annotations.AnalyzerDefs;
import org.hibernate.search.annotations.CharFilterDef;
import org.hibernate.search.annotations.DateBridge;
import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;
import org.hibernate.search.annotations.Parameter;
import org.hibernate.search.annotations.Resolution;
import org.hibernate.search.annotations.Store;
import org.hibernate.search.annotations.TokenFilterDef;
import org.hibernate.search.annotations.TokenizerDef;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Indexed
@AnalyzerDefs({
	// auto complete 1
	@AnalyzerDef(name = "autocompleteEdgeAnalyzer",
		// Split input into tokens according to tokenizer
		tokenizer = @TokenizerDef(factory = KeywordTokenizerFactory.class),
		
		filters = {
		 // Normalize token text to lowercase, as the user is unlikely to
		 // care about casing when searching for matches
		 @TokenFilterDef(factory = PatternReplaceFilterFactory.class, params = {
		   @Parameter(name = "pattern",value = "([^a-zA-Z0-9\\.])"),
		   @Parameter(name = "replacement", value = " "),
		   @Parameter(name = "replace", value = "all") }),
		 @TokenFilterDef(factory = LowerCaseFilterFactory.class),
		 @TokenFilterDef(factory = StopFilterFactory.class),
		 // Index partial words starting at the front, so we can provide
		 // Autocomplete functionality
		 @TokenFilterDef(factory = EdgeNGramFilterFactory.class, params = {
		   @Parameter(name = "minGramSize", value = "3"),
		   @Parameter(name = "maxGramSize", value = "50") })
		 },
		 charFilters = { @CharFilterDef(factory = HTMLStripCharFilterFactory.class) }),

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
		   @Parameter(name = "maxGramSize", value = "5") }),
		 @TokenFilterDef(factory = PatternReplaceFilterFactory.class, params = {
		   @Parameter(name = "pattern",value = "([^a-zA-Z0-9\\.])"),
		   @Parameter(name = "replacement", value = " "),
		   @Parameter(name = "replace", value = "all") })  
		 },
		 charFilters = { @CharFilterDef(factory = HTMLStripCharFilterFactory.class) }),

	// potuguese, striphtml	    
	@AnalyzerDef(name = "customPostAnalyzer",
	
		tokenizer = @TokenizerDef(factory = StandardTokenizerFactory.class),
		filters = {
		@TokenFilterDef(factory = WordDelimiterFilterFactory.class),
		@TokenFilterDef(factory = LowerCaseFilterFactory.class),
		@TokenFilterDef(factory = SnowballPorterFilterFactory.class, params = {
			@Parameter(name="resource_charset", value = "UTF-8"),
			@Parameter(name = "language", value = "Portuguese")
		}),
		},
		charFilters = { @CharFilterDef(factory = HTMLStripCharFilterFactory.class)   })
})
public class Post {
	
	public static final String STATE_PUBLISHED = "PUBLISHED";
	public static final String STATE_DRAFT = "DRAFT";
	public static final String STATE_SCHEDULED = "SCHEDULED";
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@DocumentId
	public Integer id;
    
    public Integer wordpressId;
	
	@JsonFormat(shape=JsonFormat.Shape.NUMBER)
	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	@Field(analyze = Analyze.NO)
    @DateBridge(resolution = Resolution.SECOND)
	public Date date;
	
	@JsonFormat(shape=JsonFormat.Shape.NUMBER)
	@Temporal(TemporalType.TIMESTAMP)
	public Date lastModificationDate;
	
	@Field(index=Index.YES, analyze=Analyze.YES, store=Store.NO)
	@Analyzer(definition="customPostAnalyzer")
	public String title;

	@Lob
	@Field(index=Index.YES, analyze=Analyze.YES, store=Store.NO)
	@Analyzer(definition="customPostAnalyzer")
	public String body;
	
	public String topper;
	
	@Size(min=1, max=15)
	public String state = STATE_PUBLISHED;
	
	@ManyToOne
	public Sponsor sponsor; 
	
	public String originalSlug;	
	
	@Column(unique=true)
	public String slug;	
	
	@OneToMany(mappedBy="post")
	public Set<Comment> comments;
		
	@ManyToOne
	public Image featuredImage;	
	
	@OneToMany(mappedBy="post")
	public Set<Image> images;
		
	@NotNull
	@ManyToOne
	@JoinColumn(updatable=false)
	public Person author;
		
	@OneToMany(mappedBy="post")
	public Set<Promotion> promotions;
	
	@NotNull
	@ManyToOne
	@IndexedEmbedded
	@JoinColumn(updatable=false)
	public Station station;
	
	@Field
	public int readsCount = 0;
	
	@Field
	public int favoritesCount = 0;

	@ManyToMany
	@IndexedEmbedded
	public Set<Term> terms;
	
	@Field(analyze = Analyze.NO)
	public boolean imageLandscape;
	
	@JsonFormat(shape=JsonFormat.Shape.NUMBER)
	@Temporal(TemporalType.TIMESTAMP)
	@Field(analyze = Analyze.NO)
    @DateBridge(resolution = Resolution.SECOND)
	public Date updatedAt;
	
	@PrePersist
	public void onCreate() {
		if(featuredImage != null && featuredImage.original != null){
			imageId = featuredImage.original.id;
			imageSmallId = featuredImage.small.id;
			imageMediumId = featuredImage.medium.id;
			imageLargeId= featuredImage.large.id;
		}else{
			imageId = null;
			imageSmallId = null;
			imageMediumId = null;
			imageLargeId = null;
		}
	}
	
	@PreUpdate
	public void onUpdate() {
		if(featuredImage != null && featuredImage.original != null){
			imageId = featuredImage.original.id;
			imageSmallId = featuredImage.small.id;
			imageMediumId = featuredImage.medium.id;
			imageLargeId= featuredImage.large.id;
		}else{
			imageId = null;
			imageSmallId = null;
			imageMediumId = null;
			imageLargeId = null;
		}
		
		updatedAt = new Date();
	}
	
	public Integer imageId;
	public Integer imageSmallId;
	public Integer imageMediumId;
	public Integer imageLargeId;
}