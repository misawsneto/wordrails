package com.wordrails.business;

import java.util.Date;
import java.util.Set;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.apache.solr.analysis.LowerCaseFilterFactory;
import org.apache.solr.analysis.StandardTokenizerFactory;
import org.apache.solr.analysis.WordDelimiterFilterFactory;
import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Analyzer;
import org.hibernate.search.annotations.AnalyzerDef;
import org.hibernate.search.annotations.AnalyzerDefs;
import org.hibernate.search.annotations.DateBridge;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.NumericField;
import org.hibernate.search.annotations.Resolution;
import org.hibernate.search.annotations.Store;
import org.hibernate.search.annotations.TokenFilterDef;
import org.hibernate.search.annotations.TokenizerDef;
import org.hibernate.validator.constraints.Email;

import com.fasterxml.jackson.annotation.JsonFormat;

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
@Table(uniqueConstraints=@UniqueConstraint(columnNames={"networkId", "username", "email"}))
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
	@Pattern(regexp="^[a-z0-9\\._-]{3,50}$", message="Invalid username")
	@Field(index=Index.YES, analyze=Analyze.YES, store=Store.NO)
	@Analyzer(definition="customAnalyzer")
	public String username;

	@NotNull
	public Integer networkId;

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
	
	@OneToOne(optional = true)
	public Network network;
	
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
	@Column(updatable=false)
	@Field(analyze = Analyze.NO)
    @DateBridge(resolution = Resolution.SECOND)
	public Date createdAt;

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
