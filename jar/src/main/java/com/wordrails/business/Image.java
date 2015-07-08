package com.wordrails.business;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.IndexedEmbedded;

@Entity
public class Image {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@DocumentId
	public Integer id;
	
	@Field
	@Size(min=1, max=100)
	public String title;
	
	@Lob
	@Field
	public String caption;
	
	@Lob
	@Field
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
	public Person owner;
	
	@ManyToOne
	public Station station;
	
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
	@IndexedEmbedded(depth=1, includePaths={"author.name", "author.id", "terms.name", "terms.id"})
	public Post post;
	
	@OneToMany(mappedBy="featuredImage")
	public Set<Post> featuringPosts;		
	
	@Column(columnDefinition = "boolean default false", nullable = false)
	public boolean vertical = false;
	
	public Integer postId;
	
	public Integer commentId;
	
	@PrePersist
	public void onCreate(){
		if(post!=null)
			postId = post.id;
		
		if(comment!=null)
			commentId = comment.id;
	}
	
	@PreUpdate
	public void onUpdate(){
		if(post!=null)
			postId = post.id;
		
		if(comment!=null)
			commentId = comment.id;
	}
}