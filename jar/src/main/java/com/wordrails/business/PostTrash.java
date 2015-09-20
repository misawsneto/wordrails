package com.wordrails.business;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(value = "TRASH")
public class PostTrash extends Post {

	public PostTrash() {
		state = Post.STATE_TRASH;
	}
}
