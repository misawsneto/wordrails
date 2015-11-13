package com.wordrails.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Email;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Table(uniqueConstraints=@UniqueConstraint(columnNames={"hash", "network_id"}))
public class Invitation {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)				
	public Integer id;
	
	@NotNull
	@Column(unique=true)
	public String hash;
	
	@Email
	public String email;
	
	public String personName;
	
	public String invitationUrl;
	
	public boolean active = true;
	
	@ManyToOne
	@JoinColumn(name = "station_id")
	public Station station;
	
	@ManyToOne
	@JoinColumn(name = "network_id")
	public Network network;
	
	@JsonFormat(shape=JsonFormat.Shape.NUMBER)
	@Temporal(TemporalType.TIMESTAMP)
	@Column(updatable=false)
	public Date createdAt;

	@PrePersist
	void onCreate() {
		createdAt = new Date();
		invitationUrl = "http://" + network.subdomain + ".trix.rocks/" + "invitation?hash=" + hash;
	}

	@JsonFormat(shape=JsonFormat.Shape.NUMBER)
	@Temporal(TemporalType.TIMESTAMP)
	public Date updatedAt;

	@PreUpdate
	void onUpdate() {
		updatedAt = new Date();
	}
}
