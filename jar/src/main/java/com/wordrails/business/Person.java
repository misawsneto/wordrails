package com.wordrails.business;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.solr.analysis.LowerCaseFilterFactory;
import org.apache.solr.analysis.StandardTokenizerFactory;
import org.apache.solr.analysis.WordDelimiterFilterFactory;
import org.hibernate.search.annotations.*;
import org.hibernate.search.annotations.Index;
import org.hibernate.validator.constraints.Email;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.Set;

@Entity
@Indexed
@AnalyzerDefs({
		@AnalyzerDef(name = "customAnalyzer",
		tokenizer = @TokenizerDef(factory = StandardTokenizerFactory.class),
		filters = {
				@TokenFilterDef(factory = WordDelimiterFilterFactory.class),
				@TokenFilterDef(factory = LowerCaseFilterFactory.class)
		})
})
@Table(name = "person", uniqueConstraints={
		@UniqueConstraint(columnNames={"user_id", "username"})
})
public class Person {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@NumericField
	@Field
	public Integer id;

	@Size(min=1, max=100)
	@Field(index=Index.YES, analyze=Analyze.YES, store=Store.NO)
	@Analyzer(definition="customAnalyzer")
	public String name;

	@Size(max=50)
	@NotNull
	@Column(unique=true)
	@Pattern(regexp="^[a-z0-9\\._-]{3,50}$")
	@Field(index=Index.YES, analyze=Analyze.YES, store=Store.NO)
	@Analyzer(definition="customAnalyzer")
	public String username;

	@OneToMany(mappedBy="author")
	public Set<Comment> comments;

	@OneToMany(mappedBy="person")
	public Set<StationRole> personsStationPermissions;

	@OneToMany(mappedBy="person")
	public Set<NetworkRole> personsNetworkRoles;

	@OneToMany(mappedBy="author")
	public Set<Post> posts;

	@OneToMany(mappedBy="promoter")
	public Set<Promotion> promotions;

	@OneToMany
	public Set<Person> following;

	@OneToMany(mappedBy="person")
	public Set<Bookmark> bookmarks;

	@OneToMany(mappedBy="person")
	public Set<Recommend> recommends;

	@NotNull
	@OneToOne(fetch = FetchType.EAGER)
	public User user;

	@Size(max=2048)
	@Field
	public String bio;

	@NotNull
	@Column(unique=true)
	@Email
	public String email;

	@OneToOne
	public Image image;

	@OneToOne
	public Image cover;

    public Integer wordpressId;

	@JsonFormat(shape=JsonFormat.Shape.NUMBER)
	@Temporal(TemporalType.TIMESTAMP)
	@Field(analyze = Analyze.NO)
    @DateBridge(resolution = Resolution.SECOND)
	public Date createdAt;
	
	@JsonFormat(shape=JsonFormat.Shape.NUMBER)
	@Temporal(TemporalType.TIMESTAMP)
	@Field(analyze = Analyze.NO)
    @DateBridge(resolution = Resolution.SECOND)
	public Date lastLogin;

	@JsonFormat(shape=JsonFormat.Shape.NUMBER)
	@Temporal(TemporalType.TIMESTAMP)
	@Field(analyze = Analyze.NO)
    @DateBridge(resolution = Resolution.SECOND)
	public Date updatedAt;

	@PrePersist
	public void onCreate() {
		if(image != null && image.original != null){
			imageId = image.original.id;
			imageSmallId = image.small.id;
			imageMediumId = image.medium.id;
			imageLargeId= image.large.id;
		}else{
			imageId = null;
			imageSmallId = null;
			imageMediumId = null;
			imageLargeId = null;
		}

		if(cover != null && cover.original != null){
			coverId = cover.original.id;
			coverLargeId= cover.large.id;
		}else{
			coverId = null;
			coverLargeId = null;
		}

		createdAt = new Date();
	}

	@PreUpdate
	public void onUpdate() {
		if(image != null && image.original != null){
			imageId = image.original.id;
			imageSmallId = image.small.id;
			imageMediumId = image.medium.id;
			imageLargeId= image.large.id;
		}else{
			imageId = null;
			imageSmallId = null;
			imageMediumId = null;
			imageLargeId = null;
		}

		if(cover != null && cover.original != null){
			coverId = cover.original.id;
			coverLargeId= cover.large.id;
		}else{
			coverId = null;
			coverLargeId = null;
		}

		updatedAt = new Date();
	}

	public Integer imageId;
	public Integer imageSmallId;
	public Integer imageMediumId;
	public Integer imageLargeId;

	public Integer coverLargeId;
	public Integer coverId;

	@Transient
	public String password;

	public Boolean passwordReseted = false;

	public String twitterHandle;

	public Integer coverMediumId;
}
