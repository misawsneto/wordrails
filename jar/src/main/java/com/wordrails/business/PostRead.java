package com.wordrails.business;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Table(uniqueConstraints=@UniqueConstraint(columnNames={"post_id", "person_id", "sessionid"}))
public class PostRead {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@DocumentId
	public Integer id;
	
	@ManyToOne
	@IndexedEmbedded(includePaths={"name", "id"})
	public Person person;
	
	@ManyToOne
	@IndexedEmbedded(depth=1, includePaths={"author.name", "author.id", "terms.name", "terms.id"})
	public Post post;

	@JsonFormat(shape=JsonFormat.Shape.NUMBER)
	@Temporal(TemporalType.TIMESTAMP)
	public Date createdAt;

	@JsonFormat(shape=JsonFormat.Shape.NUMBER)
	@Temporal(TemporalType.TIMESTAMP)
	public Date updatedAt;
	
	public String sessionid;

	@PrePersist
	void onCreate() {
		createdAt = new Date();
	}

	@PreUpdate
	void onUpdate() {
		updatedAt = new Date();
	}
	
}
