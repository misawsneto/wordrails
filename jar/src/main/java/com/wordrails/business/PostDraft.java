package com.wordrails.business;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
//@Indexed
@DiscriminatorValue(value="DRAFT")
public class PostDraft extends Post {

	public PostDraft() {
		state = Post.STATE_DRAFT;
	}
}
