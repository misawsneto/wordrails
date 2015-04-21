package com.wordrails.business;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
public class Image {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public Integer id;
	
	@Size(min=1, max=100)
	public String title;
	
	@Lob
	public String caption;
	
	@Lob
	public String credits;
	
	@ManyToOne
	public Comment comment;

	@OneToOne(mappedBy="image")
	public Person person;
	
	@OneToOne(mappedBy="logo")
	public Network network;
	
	@OneToOne(mappedBy="logo")
	public Sponsor logoSponsor;
	
	@ManyToOne
	public Sponsor publicitySponsor;
	
	@NotNull
	@ManyToOne(cascade=CascadeType.REMOVE)
	public File original;
	
	@NotNull
	@ManyToOne(cascade=CascadeType.REMOVE)
	public File small;
	
	@NotNull
	@ManyToOne(cascade=CascadeType.REMOVE)
	public File medium;
	
	@NotNull
	@ManyToOne(cascade=CascadeType.REMOVE)
	public File large;

	@ManyToOne
	public Post post;
	
	@OneToMany(mappedBy="featuredImage")
	public Set<Post> featuringPosts;		
}