package com.wordrails.domain;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;
import java.util.Set;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
public class Sponsor {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer id;

	@NotNull
	@ManyToOne
	public Network network;

	@Size(min = 1, max = 200)
	public String name;

	@Size(max = 1000)
	public String keywords;

	@Lob
	public String link;

	@OneToOne
	public Image logo;
	public Integer logoId;
	public Integer logoMediumId;
	public Integer logoLargeId;

	@OneToMany
	public Set<Ad> ads;

	@JsonFormat(shape = JsonFormat.Shape.NUMBER)
	@Temporal(TemporalType.TIMESTAMP)
	public Date updatedAt;

	@JsonFormat(shape = JsonFormat.Shape.NUMBER)
	@Temporal(TemporalType.TIMESTAMP)
	@Column(updatable = false)
	public Date createdAt;

	@PrePersist
	public void onCreate() {
		if (logo != null && logo.original != null) {
			logoId = logo.original.id;
			logoMediumId = logo.medium.id;
			logoLargeId = logo.large.id;
		} else {
			logoId = null;
			logoMediumId = null;
			logoLargeId = null;
		}

		createdAt = updatedAt = new Date();
	}

	@PreUpdate
	public void onUpdate() {
		if (logo != null && logo.original != null) {
			logoId = logo.original.id;
			logoMediumId = logo.medium.id;
			logoLargeId = logo.large.id;
		} else {
			logoId = null;
			logoMediumId = null;
			logoLargeId = null;
		}

		updatedAt = new Date();
	}
}