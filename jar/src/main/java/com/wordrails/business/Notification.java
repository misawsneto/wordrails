package com.wordrails.business;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;
import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Indexed
public class Notification {
	
	public enum Type{
		ADDED_TO_STATION, REMOVED_FROM_STATION, POST_COMMENTED, POST_DELETED, POST_ADDED, BREAKING_NEWS, MESSAGE, IREPORT_INVITE, IREPORT_REVOKE
	}
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@DocumentId
	public Integer id;
	
	@ManyToOne
	@NotNull
	@IndexedEmbedded
	public Person person;
	
	@ManyToOne
	@NotNull
	public Network network;
	
	@NotNull
	public String hash;
	
	@ManyToOne
	public Station station;
	
	@ManyToOne
	@IndexedEmbedded
	public Post post;
	
	public Integer postId;
	
	public boolean seen = false;
	
	@NotNull
	@NotEmpty
	@Size(min=1,max=500)
	public String message;
	
	@NotNull
	@NotEmpty
	public String type;
	
	@JsonFormat(shape=JsonFormat.Shape.NUMBER)
	@Temporal(TemporalType.TIMESTAMP)
	@Column(updatable=false)
	public Date createdAt;

	@PrePersist
	void onCreate() {
		createdAt = new Date();
		if(!contains(type)){
			throw new BadRequestException("Invalid notification type");
		}
		if(post!=null){
			postId = post.id;
		}
	}

	@JsonFormat(shape=JsonFormat.Shape.NUMBER)
	@Temporal(TemporalType.TIMESTAMP)
	public Date updatedAt;

	@PreUpdate
	void onUpdate() {
		updatedAt = new Date();
	}
	
	private boolean contains(String test) {

	    for (Type t : Type.values()) {
	        if (t.name().equals(test)) {
	            return true;
	        }
	    }

	    return false;
	}
}
