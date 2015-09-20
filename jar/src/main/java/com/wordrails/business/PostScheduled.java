package com.wordrails.business;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
//@Indexed
@DiscriminatorValue(value = "SCHEDULED")
public class PostScheduled extends Post {

	public PostScheduled() {
		state = Post.STATE_SCHEDULED;
	}
}
