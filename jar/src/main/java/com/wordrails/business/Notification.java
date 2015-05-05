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
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
public class Notification {
	
	enum Type{
		ADDED_TO_STATION, REMOVED_FROM_STATION, POST_COMMENTED, POST_DELETED, POST_ADDED, BREAKING_NEWS,
	}
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)				
	public Integer id;
	
	@ManyToOne
	@NotNull
	public Person person;
	
	@ManyToOne
	@NotNull
	public Network network;
	
	@ManyToOne
	public Station station;
	
	@ManyToOne
	public Post post;
	
	public Integer postId;
	
	public boolean seen = false;
	
	@NotNull
	@Size(min=1,max=500)
	@Pattern(regexp="\\S", message="Empty string validation")
	public String message;
	
	@NotNull
	@Pattern(regexp="\\S", message="Empty string validation")
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
