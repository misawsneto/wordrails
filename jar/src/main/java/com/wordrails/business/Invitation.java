package com.wordrails.business;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Email;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
public class Invitation {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)				
	public Integer id;
	
	@NotNull
	@Column(unique=true)
	public String hash;
	
	@NotNull
	@Email
	public String email;
	
	public String personName;
	
	public boolean active = true;
	
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
}
