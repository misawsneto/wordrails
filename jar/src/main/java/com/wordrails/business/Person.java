package com.wordrails.business;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;

@Entity
public class Person {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)				
	public Integer id;

	@Size(min=1, max=100)
	public String name;

	@Size(max=50)
	@NotNull
	@Column(unique=true)
	@Pattern(regexp="^[a-z0-9\\._-]{3,50}$", message="Invalid username")
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
	public Set<Favorite> favorites;
	
	@OneToMany(mappedBy="person")
	public Set<Bookmark> bookmarks;
	
	@OneToMany(mappedBy="person")
	public Set<Recommend> recommends;
	
	@Size(max=2048)
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
	public Date createdAt;

	@JsonFormat(shape=JsonFormat.Shape.NUMBER)
	@Temporal(TemporalType.TIMESTAMP)
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
