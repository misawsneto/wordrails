package com.wordrails.business;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.IndexedEmbedded;

@Entity
public class Image implements Serializable {

	public enum Type {FAVICON, SPLASH, LOGIN, POST, COVER, PROFILE_PICTURE}

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

	@Field
	@Column(columnDefinition = "varchar(255) default 'POST'", nullable = false)
	public String type;
	
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
	@ManyToOne(cascade=CascadeType.ALL)
	public TrixFile original;
	
	@NotNull
	@ManyToOne(cascade=CascadeType.REMOVE)
	public TrixFile small;
	
	@NotNull
	@ManyToOne(cascade=CascadeType.REMOVE)
	public TrixFile medium;
	
	@NotNull
	@ManyToOne(cascade=CascadeType.REMOVE)
	public TrixFile large;

	@ManyToOne
	@IndexedEmbedded(depth=1, includePaths={"author.name", "author.id", "terms.name", "terms.id"})
	public Post post;
	
	@OneToMany(mappedBy="featuredImage")
	public Set<Post> featuringPosts;		
	
	@Column(columnDefinition = "boolean default false", nullable = false)
	public boolean vertical = false;
	
	public Integer postId;
	
	public Integer commentId;

	@JsonFormat(shape=JsonFormat.Shape.NUMBER)
	@Temporal(TemporalType.TIMESTAMP)
	@Column(updatable=false)
	public Date createdAt;

	@JsonFormat(shape=JsonFormat.Shape.NUMBER)
	@Temporal(TemporalType.TIMESTAMP)
	public Date updatedAt;
	
	@PrePersist
	public void onCreate(){
		if(post!=null)
			postId = post.id;
		
		if(comment!=null)
			commentId = comment.id;

		createdAt = updatedAt = new Date();
	}
	
	@PreUpdate
	public void onUpdate(){
		if(post!=null)
			postId = post.id;
		
		if(comment!=null)
			commentId = comment.id;

		updatedAt = new Date();
	}

	public static boolean containsType(String string) {
		for (Image.Type type : Image.Type.values()) {
			if (type.name().equals(string)) {
				return true;
			}
		}
		return false;
	}
}