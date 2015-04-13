package com.wordrails.business;

import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
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

import com.fasterxml.jackson.annotation.JsonFormat;

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
	private Set<Favorite> favorites;
	
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

	@JsonFormat(shape=JsonFormat.Shape.NUMBER)
	@Temporal(TemporalType.TIMESTAMP)
	@Column(updatable=false)
	public Date createdAt;

	@PrePersist
	void onCreate() {
		createdAt = new Date();
	}

	@JsonFormat(shape=JsonFormat.Shape.NUMBER)
	@Temporal(TemporalType.TIMESTAMP)
	public Date updatedAt;

	@PreUpdate
	void onUpdate() {
		updatedAt = new Date();
	}

	@Transient
	public String password;

	public Boolean passwordReseted = false;

	public String twitterHandle;
}