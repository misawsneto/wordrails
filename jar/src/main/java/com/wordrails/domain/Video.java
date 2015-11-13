package com.wordrails.domain;

import javax.persistence.Entity;

@Entity
public class Video extends BaseEntity {

	public Integer duration;

	public File file;

}
