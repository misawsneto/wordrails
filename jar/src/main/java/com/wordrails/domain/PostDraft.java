package com.wordrails.domain;

//import org.hibernate.search.annotations.Indexed;

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
