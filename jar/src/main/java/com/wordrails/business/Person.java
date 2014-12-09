package com.wordrails.business;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

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
	
	@Size(max=2048)
	public String bio;
	
	@NotNull
	@Column(unique=true)
	public String email;
	
	@OneToOne
	public Image image;
	
	@OneToOne
	public Image cover;
	
	@Transient
	public String password;
	
	public Boolean passwordReseted = false;
	
	public String twitterHandle;
}