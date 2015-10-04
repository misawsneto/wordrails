package com.wordrails.business;

import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
public class Comment {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public Integer id;
	
	@JsonFormat(shape=JsonFormat.Shape.NUMBER)
	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	@Column(updatable=false)
	public Date date;
	
	@JsonFormat(shape=JsonFormat.Shape.NUMBER)
	@Temporal(TemporalType.TIMESTAMP)
	public Date lastModificationDate;
	
	@Size(min=1, max=100)
	public String title;

	@Lob
	@NotNull
	public String body;
	
	@OneToMany(mappedBy="comment")
	public Set<Image> images;

	@NotNull
	@ManyToOne
	@JoinColumn(updatable=false)
	public Person author;
	
	@NotNull
	@ManyToOne
	@JoinColumn(updatable=false)
	public Post post;
	
	@PrePersist
	public void onCreate() {
		if (date == null)
			date = new Date();
	}

	@PreUpdate
	public void onUpdate() {
		lastModificationDate = new Date();
	}
}